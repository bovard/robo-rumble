
package team122;
import battlecode.common.*;
import java.util.ArrayList;

/**
 * The behavioral logic for BuilderScouts.
 * @see BuilderSensorRobotSystem
 * @author bovard
 */
public class RSBuilderScout extends BuilderSensorRobotSystem {
  protected ArrayList<Mine> uncoveredMines;
  protected Mine currentMine;

  public RSBuilderScout(RobotController robotControl, SensorSystem sensorSys, BuilderSystem buildSys) {
    super(robotControl, sensorSys, buildSys);
    uncoveredMines = new ArrayList<Mine>();
    gameEvents = new RSBuilderScoutGE(robotControl, comSys, sensorSys, this);
  }

  /**
   * Main loop for the BuilderScout, should never retern anything
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
        while(sensorSys.senseObjectAtLocation(robotControl.getLocation().add(robotControl.getDirection()), RobotLevel.ON_GROUND) == null
                || !(robotControl.getDirection() == Direction.NORTH || robotControl.getDirection() == Direction.EAST ||
                robotControl.getDirection() == Direction.SOUTH || robotControl.getDirection() == Direction.WEST)) {
          while(navSys.isActive()) {
            yield();
          }
          actTurn(robotControl.getDirection().rotateRight());
          while(navSys.isActive()) {
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
    //===========================================
    // MAIN LOOP
    //===========================================
    while(true) {
      yield();
      //if we have a combat game event, run away!
      if(gameEvents.checkGameEventsAbove(GameEventLevel.DIRECTIVE)) {
        currentGameEventLevel = GameEventLevel.COMBAT;
        seqFlee();
      }
      //if we received a directive, build it!
      else if(((SensorGameEvents)gameEvents).hasDirective()) {
        currentGameEventLevel = GameEventLevel.DIRECTIVE;
        Message buildDirective = comSys.getLastDirective(PlayerConstants.MESSAGE_BUILD_DIRECTIVE);
        BuildOrder toBuild = BuildOrderID.getBuildOrderFromID(comSys.getBuildOrderIDFromBuildDirective(buildDirective));
        seqBuildAtLocation(toBuild, comSys.getMapLocationFromBuildDirective(buildDirective));
        yield();
      }
      //if we still know where some uncovered mines are, go to them and build something there
      else if(!uncoveredMines.isEmpty()) {
        currentGameEventLevel = GameEventLevel.NORMAL;
        //if we build a recycler on it
        currentMine = getClosestMine();
        if(seqBuildAtLocation(BuildOrder.RECYCLER, currentMine.getLocation())) {
          //remove it from the queue
          while(navSys.isActive()) {
            yield();
          }
          Direction toTurn = robotControl.getLocation().directionTo(currentMine.getLocation());
          if(!robotControl.getDirection().equals(toTurn)) {
            navSys.setTurn(toTurn);
          }
          uncoveredMines.remove(currentMine);
          currentMine = null;

          yield();
        }
      }
      //otherwise look for mines
      else {
        currentGameEventLevel = GameEventLevel.LOW;
        seqScoutAndTurn();
      }
    }
  }

  /**
   * checks to see how many uncovered mines are left in the queue
   * @return the number of mines left in the queue
   */
  public int getNumberOfUncoveredMines() {
    if(uncoveredMines != null) {
      return uncoveredMines.size();
    }
    else {
      return 0;
    }
  }

  /**
   * Detects if there is an uncovered mine within sensor range and adds it to the uncoverdMines
   * list if it isn't already there
   * @return If an uncovered mine was found within sensor range
   */
  protected void trackUncoveredMines() {
    MapLocation myLocation = robotControl.getLocation();
    if (((SensorGameEvents)gameEvents).canSeeMine()) {
      Mine[] mines = sensorSys.getMines();

      for (int i=0; i < mines.length; i++) {
        //check to see if these is anything on the mine (besides possibly me)
        if(!mines[i].getLocation().equals(myLocation)
                && sensorSys.senseObjectAtLocation(mines[i].getLocation(), RobotLevel.ON_GROUND)==null) {
          //if this is a newly uncovered mine
          if (!uncoveredMines.contains(mines[i])) {
            uncoveredMines.add(mines[i]);
          }
        }
        //if there is soemthing there, remoe it from the list if it was on the list
        else {
          if(uncoveredMines.contains(mines[i])) {
            uncoveredMines.remove(mines[i]);
          }
        }
      }
    }
  }

  /**
   * Looks through all the uncoveredMines and returns the closest
   * @return the closest Mine
   */
  private Mine getClosestMine() {
    Mine toReturn = null;
    int distance = Integer.MAX_VALUE;
    MapLocation currentLoc = robotControl.getLocation();
    for (int i=0;i<uncoveredMines.size();i++) {
      int currentDistance = currentLoc.distanceSquaredTo(uncoveredMines.get(i).getLocation());
      if(currentDistance < distance) {
        toReturn = uncoveredMines.get(i);
        distance = currentDistance;
      }
    }
    return toReturn;
  }



  /**
   * Couples the yield function with game events, resetting the game events just before
   * calling yield and calculating them again as the first action for the robot
   */
  @Override
  protected void yield() {
    gameEvents.resetGameEvents();
    robotControl.yield();
    gameEvents.calcGameEvents();
    trackUncoveredMines();
    robotControl.setIndicatorString(0, "ScoutDirection: " + scoutDirection + " - Location: "+robotControl.getLocation().toString() + "- numMines: "+uncoveredMines.size());
  }


}