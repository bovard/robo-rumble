
package team122;
import battlecode.common.*;

/**
 * BuilderScouts are robots that have at least a sensor and a constructor component
 * They will roam the board looking for mines to build recyclers on.
 * @author bovard
 */
public class BuilderScoutRobotSystem extends OldSensorRobotSystem {
  protected BuilderController buildControl;
  protected BuilderSystem buildSys;
  protected MapLocation uncoveredMineLoc;
  protected Message buildDirective;

  /**
   * Constructor for the BuilderScout, assumes a Constructor component on the bot
   * @param robotControl
   */
  public BuilderScoutRobotSystem(RobotController robotControl) {

    super(robotControl);
    robotControl.setIndicatorString(0,"BuilderScoutConstructor");

    //on scouts, build systems should be in component[2], added another if clause to catch
    //our starting bot, who has it in components[1]
    if(robotControl.components()[1].type() == ComponentType.CONSTRUCTOR)
      buildControl = (BuilderController)robotControl.components()[1];
    else
      buildControl = (BuilderController)robotControl.components()[2];
    buildSys = new BuilderSystem(robotControl, buildControl);
    comSys.setFilter(new int[] {1, 1, 0});

  }

  /**
   * The main loop, called to run a BuilderScout (or child)
   */
  @Override
  public void go() {
    
    // hack that ensures that we'll point at our mines before waking up our first bot
    // (so we can find the other two mines right off the bat)
    if (Clock.getRoundNum() < 20)
    {
      yield();
      yield();
      try {
        while(sensorControl.senseObjectAtLocation(robotControl.getLocation().add(robotControl.getDirection()), RobotLevel.ON_GROUND) == null
                || !(robotControl.getDirection() == Direction.NORTH || robotControl.getDirection() == Direction.EAST ||
                robotControl.getDirection() == Direction.SOUTH || robotControl.getDirection() == Direction.WEST)) {
          while(moveControl.isActive()) {
            yield();
          }
          actTurn(robotControl.getDirection().rotateRight());
          while(moveControl.isActive()) {
            yield();
          }
        }
        //System.out.println("Direction is "+robotControl.getDirection());
        //System.out.println("Sensed: "+sensorControl.senseObjectAtLocation(robotControl.getLocation().add(robotControl.getDirection()), RobotLevel.ON_GROUND));
      } catch (Exception e) {
        System.out.println("caught exception:");
        e.printStackTrace();
      }
    }

    //main loop
    robotControl.setIndicatorString(0,"BuilderScout");
    while(true) {
      selScout();
    }
  }

  /**
   * the bot attempts to perform the ScoutBuild sequence
   * @return if the action was sucessfull
   */
  protected boolean selScout() {
    robotControl.setIndicatorString(1, "selScout");
    if(sensorGameEvents.hasDirective) {
      seqBuildDirective();
    }
    else if(seqScoutBuild()) {
      return true;
    }
    return false;
  }

  /**
   * sets the robot off to fufill it's directive
   * @return if it was completed successfully
   */
  protected boolean seqBuildDirective() {
    robotControl.setIndicatorString(1, "seqBuildDirective");
    buildDirective = comSys.getLastDirective(PlayerConstants.MESSAGE_BUILD_DIRECTIVE);
    MapLocation location = new MapLocation(buildDirective.ints[5],buildDirective.ints[6]);
    if(super.seqApproachLocation(location, RobotLevel.ON_GROUND)) {
      if(robotControl.getLocation() == location) {
        actMoveBackward();
      }
      robotControl.setIndicatorString(1, "seqBuildDirective - waiting for funds");
      BuildOrder toBuild = BuildOrderID.getBuildOrderFromID(buildDirective.ints[4]);
      while(robotControl.getTeamResources() < toBuild.cost
              + PlayerConstants.MINIMUM_FLUX + BuildOrder.RECYCLER.cost) {
        yield();
      }
      if(robotControl.getLocation().isAdjacentTo(location)) {
        return buildSys.seqBuild(toBuild, location);
      }
      System.out.println("didn't end up at location");
    }
    return false;
  }

  /**
   * sends the robot scouting to find an uncovered mine which it will build a recycler on
   * @return if this was performed successfully
   */
  protected boolean seqScoutBuild() {
    robotControl.setIndicatorString(1, "selScoutBuild");
    //move around until you find an uncoverd mine
    if (seqScoutUncoveredMine()) {
      //try to build a recycler on that mine
      if(seqBuildRecycler()) {
        return true;
      }
    }
    return false;
  }

  /**
   * sends the bot to find an uncovered mine, when one is sensed, returns true and also
   * set unCoveredMineLoc to the location of the uncovered mine.
   * returns false if scouting is stopped for any other reason
   * @return if an uncovered mine was found
   */
  protected boolean seqScoutUncoveredMine() {
    uncoveredMineLoc = null;
    robotControl.setIndicatorString(1, "seqScoutUncoveredMine");
    navSys.setDestination(chooseNextDestination());
    boolean done = seqSenseMine();
    if(done) {
      return done;
    }
    robotControl.setIndicatorString(1, "seqScoutUncoveredMine - newDest");
    //while we haven't found an uncovered mine and we aren't at our destination
    while(!done && !actMove()) {
      done = seqSenseMine();
      if(sensorGameEvents.checkCriticalGameEvents()) {
        return false;
      }
    }
    return done;
  }



  /**
   * builds a recycler on the newly scouted uncovered mine
   * @return whether or not the action was completeled sucessfully
   */
  protected boolean seqBuildRecycler() {
    robotControl.setIndicatorString(1, "selBuildRecycler");
    // find a free square adjacent to target mine and move there
    if (seqApproachLocation(uncoveredMineLoc, robotControl.getRobot().getRobotLevel())
            && robotControl.getLocation().isAdjacentTo(uncoveredMineLoc)) {
      //wait till we have enough money
      while(robotControl.getTeamResources() < PlayerConstants.MINIMUM_FLUX + BuildOrder.RECYCLER.cost) {
        robotControl.setIndicatorString(1, "selBuildRecycler -waiting for funds");
        yield();
      }
      if (!moveControl.isActive()) {
        try {
          moveControl.setDirection(robotControl.getLocation().directionTo(uncoveredMineLoc));
        } catch (Exception e) {
          System.out.println("caught exception:");
          e.printStackTrace();
        }

      }

      //build the recycler
      if (buildSys.seqBuild(BuildOrder.RECYCLER, uncoveredMineLoc)) {
        //once it's been built we should reset the uncovered mine location to null
        actTurn(robotControl.getLocation().directionTo(uncoveredMineLoc));
        uncoveredMineLoc = null;
        return true;
      }
    }
    return false;
  }
  

  /**
   * Detects if there is an uncovered mine within sensor range
   * @return If an uncovered mine was found within sensor range
   */
  protected boolean seqSenseMine() {
    if (sensorGameEvents.seeMine) {
      boolean foundNewMine = false;
      Mine[] mines = sensorSys.getMines();

      boolean oldMineInSight = false;
      //check to see if we can still see the old mine
      for (int j=0; j<mines.length; j++) {
        if (mines[j].getLocation()==uncoveredMineLoc) {
          oldMineInSight = true;
        }
      }
      //if we can't see the old mine, check if we can see a new mine
      if (!oldMineInSight) {
        int i = 0;
        while (i < mines.length && !foundNewMine) {
          try {
            //check to see if these is anything built on the mine
            if(sensorControl.senseObjectAtLocation(mines[i].getLocation(), RobotLevel.ON_GROUND)==null) {
              //if this is a new uncovered mine
              if (mines[i].getLocation()!=uncoveredMineLoc) {
                uncoveredMineLoc = mines[i].getLocation();
                foundNewMine = true;
                return true;
              }
              //otherwise we've just detected the same mine and should return
              else {
                return false;
              }
            }

          } catch (Exception e) {
            System.out.println("caught exception:");
            e.printStackTrace();
          }
          i++;
        }
      }
      if (foundNewMine) {
        uncoveredMineLoc = null;
      }
      return foundNewMine;
    }
    else {
      return false;
    }
  }

    /**
   * Tell the robot to move to a square adjacent to location that is free of units at the
   * imputted level
   * Overridden to stop when it hits mines
   * @param location the target square
   * @param level the level to approach at
   * @return if the approach was successful
   */
  @Override
  protected boolean seqApproachLocation(MapLocation location, RobotLevel level) {
    robotControl.setIndicatorString(1, "seqApproachLocation");
    navSys.setDestination(location);
    boolean keepGoing = true;
    while(!sensorControl.canSenseSquare(location) && keepGoing) {
      //TODO: add code here check to see if enemies are seen or under attack
      keepGoing = keepGoing && !seqSenseMine();
      actMove();
    }

    robotControl.setIndicatorString(1, "seqApproachLocation - canSee");
    boolean done = false;
    int loops = 0;
    int maxLoops = 25;
    while(keepGoing && !done && loops<maxLoops) {
      for (int x = -1; x < 2; x++) {
        if (done) {
          break;
        }
        for (int y = -1; y < 2; y++) {
          if (done) {
            break;
          }
          if(sensorControl.canSenseSquare(location.add(x,y)) && !(x==0 && y==0)) {
            try {
              if(sensorControl.senseObjectAtLocation(location.add(x,y), level)==null &&
                      robotControl.senseTerrainTile(location.add(x,y)).isTraversableAtHeight(robotControl.getRobot().getRobotLevel())) {
                navSys.setDestination(location.add(x,y));
                done = true;
                break;
              }
            } catch (Exception e) {
              System.out.println("caught exception:");
              e.printStackTrace();
            }
          }
        }
      }
      if (!done) {
        actMove();
      }
      loops++;
    }
    if (loops >= maxLoops)
    {
      return false;
    }

    robotControl.setIndicatorString(1, "seqApproachLocation - foundFree");
    //if we found a location, try to move to that location and return the results
    if(done) {
      return seqMove();
    }
    return false;
  }

    /**
   * Called to move multiple times to a destination, assumes the destination is already set
   * @param dest place to move to
   * @return if the destination was reached safely
   */
  @Override
  protected boolean seqMove() {
    boolean keepGoing = true;
    boolean done = false;
    while(keepGoing && !done) {
      //TODO: check to make sure the bot isn't under attack here
      done = actMove();
      //stop if you find a new mine
      keepGoing = !seqSenseMine();
      //stop if you see the mine is now covered
      if(uncoveredMineLoc !=null ) {
        if(sensorControl.canSenseSquare(uncoveredMineLoc)) {
          try {
            keepGoing = keepGoing && (sensorControl.senseObjectAtLocation(uncoveredMineLoc, robotControl.getRobot().getRobotLevel())==null);
          } catch (Exception e) {
            System.out.println("caught exception:");
            e.printStackTrace();
          }

        }
      }
      //you're done if you are next to the mine
      if(uncoveredMineLoc != null) {
        if (robotControl.getLocation().isAdjacentTo(uncoveredMineLoc)) {
          done = true;
          yield();
        }
      }
    }
    return done && keepGoing;
  }
}
