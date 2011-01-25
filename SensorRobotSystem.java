
package team122;
import battlecode.common.*;
import java.util.ArrayList;

/**
 * Base behavoir class for robots with movement and sensor controllers (but no weapon or building)
 * @author bovard
 */
public class SensorRobotSystem extends RobotSystem {

  protected SensorSystem sensorSys;

  //edges of the map, unkown initially
  protected int minX=-1, maxX=Integer.MAX_VALUE, minY=-1, maxY=Integer.MAX_VALUE;
  //new destination constants, used in chooseNewDestination()
  protected static final int NEW_DEST_RANGE = 15;
  protected static final int NEW_DEST_VARIANCE = 5;
  //the direction to Scout in
  protected Direction scoutDirection;
  private int lastChangedDirection;
  protected ArrayList<Mine> mines;

  /**
   * A SensorRobotSystem has everything a RobotSystem does, plus a SensorSystem
   * @param robotControl the robotcontroller of the robot
   * @param sensorSys the sensorsystem of the robot
   */
  public SensorRobotSystem(RobotController robotControl, SensorSystem sensorSys) {
    super(robotControl);
    this.sensorSys = sensorSys;
    gameEvents = new SensorGameEvents(robotControl, comSys, sensorSys);
    this.navSys = new SensorNavigationSystem(robotControl, (MovementController)robotControl.components()[0], sensorSys);
  }



  /**
   * Chooses the next destination to go to based on birthplace and previous destinations
   * currently chooses a random location within NEXT_DEST_RANGE squares horizontally and vertically
   * of the robots last destination
   * @return the new destination
   */
  protected MapLocation chooseNextDestination() {
    //if we don't have a destination, set it as the robots birthplace
    if (navSys.getDestination()==null) {
      navSys.setDestination(birthPlace);
    }
    //if the scout direction is not valid, choose another
    if (!checkScoutDirectionValidity()) {
      changeScoutDirection();
    }
    MapLocation next;
    int loops = 0;
    int x, y;
    do {
      next = navSys.getDestination();
      //make the next destination in the direction of scoutDirection
      next = next.add(scoutDirection, NEW_DEST_RANGE - NEW_DEST_VARIANCE);

      //add a slight variance
      x = ((rand.nextInt(2*NEW_DEST_VARIANCE+1) * Clock.getRoundNum()) % (2*NEW_DEST_VARIANCE))
              - NEW_DEST_VARIANCE;
      y = ((rand.nextInt(2*NEW_DEST_VARIANCE+1) * Clock.getRoundNum()) % (2*NEW_DEST_VARIANCE))
              - NEW_DEST_VARIANCE;
      next = next.add(x, y);
      loops++;
    } while ((next.x < minX || next.x > maxX || next.y < minY || next.y > maxY) && loops<5);
    if (loops<5) {
      return next;
    }
    else {
      return birthPlace;
    }
  }

  /**
   * Tells the bot to choose a new destination and move there
   * @return true if the bot reaches the dest, false if it's interrupted
   */
  protected boolean seqScout() {
    robotControl.setIndicatorString(1, "seqScout");
    navSys.setDestination(chooseNextDestination());
    return seqMove();
  }

  /**
   * the bot runs until there are no more combat events happening
   * @return if it has fleed sucessfully
   */
  protected boolean seqFlee() {
    
    robotControl.setIndicatorString(1, "seqFlee!!");

    while(gameEvents.checkGameEventsAbovePriority(GameEventLevel.DIRECTIVE.priority)) {
      //wait for a motor to be active
      while(navSys.isActive()) {
        yield();
      }
      //if you can move backward, do it
      if(navSys.canMove(robotControl.getDirection().opposite())) {
        navSys.setMoveBackward();
        yield();
      }
      //otherwise turn
      else {
        while(!navSys.canMove(robotControl.getDirection().opposite())
                && gameEvents.checkGameEventsAbovePriority(GameEventLevel.MISSION.priority)) {
          navSys.setTurn(robotControl.getDirection().rotateRight());
          yield();
        }
      }
    }
    //change our scout direction
    scoutDirection = robotControl.getDirection().opposite();
    //move in the opposite direction
    navSys.setDestination(chooseNextDestination());

    return !gameEvents.checkGameEventsAbovePriority(GameEventLevel.MISSION.priority);
  }

  /**
   * Tell the robot to move to a square adjacent to location that is free of units at the
   * imputted level
   * @param location the target square
   * @param level the level to approach at
   * @return if the approach was successful
   */
  protected boolean seqApproachLocation(MapLocation location, RobotLevel level) {
    robotControl.setIndicatorString(1, "seqApproachLocation");

    //note: when comparing two locations for equality, you have to use the .equals!
    //if we are at the location we want to approach, turn until we can back out
    if(robotControl.getLocation().equals(location)) {
      while(!navSys.canMove(robotControl.getDirection().opposite())) {
        while(navSys.isActive()) {
          yield();
        }
        actTurn(robotControl.getDirection().rotateRight());
      }
      while(navSys.isActive()) {
        yield();
      }
      return actMoveBackward();
    }

    //if we are already adjacent to the location, return true;
    if(robotControl.getLocation().isAdjacentTo(location)) {
      return true;
    }

    robotControl.setIndicatorString(1, "seqApproachLocation");
    navSys.setDestination(location);
    boolean hasGameEvents = gameEvents.checkGameEventsAbovePriority(currentGameEventLevel.priority);
    while(!sensorSys.canSenseLocation(location) && !hasGameEvents) {
      while(navSys.isActive()) {
        yield();
        hasGameEvents = gameEvents.checkGameEventsAbovePriority(currentGameEventLevel.priority);
        if(hasGameEvents) {
          return false;
        }
      }
      actMove();
      hasGameEvents = gameEvents.checkGameEventsAbovePriority(currentGameEventLevel.priority);
    }
    //if we encounter a game event, pull out
    if(hasGameEvents) {
      return false;
    }

    //otherwise we can see the location, need to find a free square now
    robotControl.setIndicatorString(1, "seqApproachLocation - canSee");
    MapLocation freeSquare = null;
    int maxLoops = 25;
    int loop = 0;
    while(!hasGameEvents && freeSquare == null && loop < maxLoops) {
      freeSquare = sensorSys.findClosestUnoccupiedAdjacentSquareToLocation(location);
      if(freeSquare == null) {
        while(navSys.isActive()) {
          yield();
          hasGameEvents = gameEvents.checkGameEventsAbovePriority(currentGameEventLevel.priority);
          if(hasGameEvents) {
            return false;
          }
        }
        actMove();
      }
      hasGameEvents = gameEvents.checkGameEventsAbovePriority(currentGameEventLevel.priority);
      loop++;
    }
    //if we max out the loops or find a gameEvent, pull out
    if (loop == maxLoops || hasGameEvents) {
      return false;
    }

    if(hasGameEvents) {
      return false;
    }

    //otherwise we've found a free square and need to approach it
    navSys.setDestination(freeSquare);
    robotControl.setIndicatorString(1, "seqApproachLocation - foundFree");
    //if we found a location, try to move to that location and return the results
    return seqMove();
  }


 /**
  * Called to move once (and yield) Assumes the robot already has a destination
  * Overriden to allow for map bounds checks
  *
  * //TODO: integrate this into RobotSystem.actMove() with a super.actMove() call in here
  *
  * @return if robot is in its current destination
  */
  @Override
  protected boolean actMove() {
    if(navSys.getDestination()==null) {
      return false;
    }
    else if(!navSys.isAtDestination()) {
      if (navSys.setNextMove()) {
        yield();
        //check for map boundary conditions
        updateMapExtrema();
        //update your position for the GUI
        robotControl.setIndicatorString(0, "ScoutDirection: " + scoutDirection + " - Location: "+robotControl.getLocation().toString());
        return checkDestinationValidity() && checkScoutDirectionValidity();
      }
    }
    System.out.println("WARNING: Bad call to actMove");
    return false;
  }



  /**
   * Checks the scout direction to see if it's valid
   * @return the validity of scoutDirection
   */
  protected boolean checkScoutDirectionValidity() {
    MapLocation currentPos = robotControl.getLocation();
    if(scoutDirection == null) {
      return false;
    }
    else if(scoutDirection.equals(Direction.NORTH)) { //Direction.NORTH
      if(currentPos.add(Direction.NORTH, NEW_DEST_RANGE).y > minY) {
        return true;
      }
      else  {
        return false;
      }
    }
    else if(scoutDirection.equals(Direction.EAST)) {
      if(currentPos.add(Direction.EAST, NEW_DEST_RANGE).x < maxX) {
        return true;
      }
      else {
        return false;
      }
    }
    else if (scoutDirection.equals(Direction.SOUTH)) {
      if(currentPos.add(Direction.SOUTH, NEW_DEST_RANGE).y < maxY) {
        return true;
      }
      else {
        return false;
      }
    }
    else if (scoutDirection.equals(Direction.WEST)) {
      if(currentPos.add(Direction.WEST, NEW_DEST_RANGE).x > minX) {
        return true;
      }
      else {
        return false;
      }
    }
    else if (scoutDirection.equals(Direction.NORTH_EAST)) {
      if(currentPos.add(Direction.NORTH, NEW_DEST_RANGE).y > minY
              && currentPos.add(Direction.EAST, NEW_DEST_RANGE).x < maxX) {
        return true;
      }
      else {
        return false;
      }
    }
    else if (scoutDirection.equals(Direction.SOUTH_EAST)) {
      if(currentPos.add(Direction.SOUTH, NEW_DEST_RANGE).y < maxY
              && currentPos.add(Direction.EAST, NEW_DEST_RANGE).x < maxX) {
        return true;
      }
      else {
        return false;
      }
    }
    else if (scoutDirection.equals(Direction.SOUTH_WEST)) {
      if(currentPos.add(Direction.SOUTH, NEW_DEST_RANGE).y < maxY
              && currentPos.add(Direction.WEST, NEW_DEST_RANGE).x > minX) {
        return true;
      }
      else {
        return false;
      }
    }
    else if (scoutDirection.equals(Direction.NORTH_WEST)) {
      if(currentPos.add(Direction.NORTH, NEW_DEST_RANGE).y > minY
              && currentPos.add(Direction.WEST, NEW_DEST_RANGE).x > minX) {
        return true;
      }
      else {
        return false;
      }
    }
    System.out.println("WARNING: fell through checkScoutDirectionValidity with Scoutdirection="+scoutDirection);
    return false;
  }


  /**
   * Chooses a new direction to scout in based on know map bounds and previous scouting direction
   */
  public void changeScoutDirection() {
    lastChangedDirection = Clock.getRoundNum();
    /*
     * North ( 0,-1)
     * South ( 0, 1)
     * East  ( 1, 0)
     * West  (-1, 0)
     */
    boolean done = false;
    MapLocation currentPos = robotControl.getLocation();
    int loops = 0;
    while(!done) {
      int nextDir = Clock.getRoundNum() + loops;
      nextDir = nextDir%8;
      switch(nextDir) {
        case 0: //Direction.NORTH
          if(scoutDirection != Direction.NORTH && currentPos.add(Direction.NORTH, NEW_DEST_RANGE).y > minY) {
            scoutDirection = Direction.NORTH;
            done = true;
          }
          break;
        case 1: //Direction.EAST
          if(scoutDirection != Direction.EAST && currentPos.add(Direction.EAST, NEW_DEST_RANGE).x < maxX) {
            scoutDirection = Direction.EAST;
            done = true;
          }
          break;
        case 2: //Direction.SOUTH
          if(scoutDirection != Direction.SOUTH && currentPos.add(Direction.SOUTH, NEW_DEST_RANGE).y < maxY) {
            scoutDirection = Direction.SOUTH;
            done = true;
          }
          break;
        case 3: //Direction.WEST
          if(scoutDirection != Direction.WEST && currentPos.add(Direction.WEST, NEW_DEST_RANGE).x > minX) {
            scoutDirection = Direction.WEST;
            done = true;
          }
          break;
        case 4: //Direction.NORTH_EAST
          if(scoutDirection != Direction.NORTH_EAST && currentPos.add(Direction.NORTH, NEW_DEST_RANGE).y > minY
                  && currentPos.add(Direction.EAST, NEW_DEST_RANGE).x < maxX) {
            scoutDirection = Direction.NORTH_EAST;
            done = true;
          }
          break;
        case 5: //Direction.SOUTH_EAST
          if(scoutDirection != Direction.SOUTH_EAST && currentPos.add(Direction.SOUTH, NEW_DEST_RANGE).y < maxY
                  && currentPos.add(Direction.EAST, NEW_DEST_RANGE).x < maxX) {
            scoutDirection = Direction.SOUTH_EAST;
            done = true;
          }
          break;
        case 6: //Direction.SOUTH_WEST
          if(scoutDirection != Direction.SOUTH_WEST && currentPos.add(Direction.SOUTH, NEW_DEST_RANGE).y < maxY
                  && currentPos.add(Direction.WEST, NEW_DEST_RANGE).x > minX) {
            scoutDirection = Direction.SOUTH_WEST;
            done = true;
          }
          break;
        case 7: //Direction.NORTH_WEST
          if(scoutDirection != Direction.NORTH_WEST && currentPos.add(Direction.NORTH, NEW_DEST_RANGE).y > minY
                  && currentPos.add(Direction.WEST, NEW_DEST_RANGE).x > minX) {
            scoutDirection = Direction.NORTH_WEST;
            done = true;
          }
          break;
      }
      loops++;
      if (loops > 8) {
        System.out.println("WARNING: In changeScoutDirection and loops="+loops+" and scoutDirection = "+scoutDirection);
        System.out.println("(minx,miny),(maxx, maxy): "+"("+minX+","+minY+"),("+maxX+","+maxY+")");
        scoutDirection = Direction.NONE;
        done = true;
      }
    }
  }

  /**
   * checks to see if the destination is a valid one
   * @return the validity of the destination
   */
  protected boolean checkDestinationValidity() {
    //if the destination is out of x bounds it's invalid, return false
    if (navSys.getDestination().x < minX || navSys.getDestination().x > maxX) {
      //robotControl.setIndicatorString(2, "BAD X DEST: "+minX+"<"+navSys.getDestination().x+"<"+maxX );
      return false;
    }
    //if the destination is out of y bounds it's invalid, return false
    if (navSys.getDestination().y < minY || navSys.getDestination().y > maxY) {
      //robotControl.setIndicatorString(2, "BAD Y DEST: "+minY+"<"+navSys.getDestination().y+"<"+maxY );
      return false;
    }
    //check if we can currently sense the destination
    if (sensorSys.canSenseLocation(navSys.getDestination()))
    {
        //check to see if there is someone there at the robots level
        if(sensorSys.senseObjectAtLocation(navSys.getDestination(), robotControl.getRobot().getRobotLevel())==null) {
          //if there isn't check to see if the location can be traversed and return the result
          return robotControl.senseTerrainTile(navSys.getDestination()).isTraversableAtHeight(robotControl.getRobot().getRobotLevel());
        }
        else {
          //if there is someone there, the destination is invalid
          return false;
        }
    }
    //if we fall through everything, the destination is valid, return true
    return true;
  }


  /**
   * used to update the information that we know about the edges of the map
   */
  protected void updateMapExtrema() {
    MapLocation canSee = robotControl.getLocation().add(robotControl.getDirection(),
            sensorSys.getRange(robotControl.getDirection()));

    try {
      //if we're looking at a tile off map, update the min/max x/y as needed
      if(robotControl.senseTerrainTile(canSee)==TerrainTile.OFF_MAP) {
        //figure what edge we're looking over
        //System.out.println("trying to updater! "+canSee);
        //System.out.println("current "+minX+" "+maxX+" "+minY+" "+maxY);

        //Check the X edges
        //looking at the minX edge
        if( minX == -1 && sensorSys.canSenseLocation(canSee.add(1, 0)) && robotControl.senseTerrainTile(canSee.add(1,0))!=TerrainTile.OFF_MAP) {
          if (canSee.x > minX) {
            //System.out.println("UPDATING minX: "+minX+" to "+canSee.x);
            minX = canSee.x;
          }
        }
        //looking at the maxX edge
        else if (maxX == Integer.MAX_VALUE && sensorSys.canSenseLocation(canSee.add(-1, 0)) && robotControl.senseTerrainTile(canSee.add(-1,0))!=TerrainTile.OFF_MAP) {
          if(canSee.x < maxX) {
            //System.out.println("UPDATING maxX: "+maxX+" to "+canSee.x);
            maxX = canSee.x;
          }
        }

        //Check the Y edges
        //looking at the minY edge
        if( minY == -1 && sensorSys.canSenseLocation(canSee.add(0, 1)) && robotControl.senseTerrainTile(canSee.add(0,1))!=TerrainTile.OFF_MAP) {
          if (canSee.y > minY) {
            //System.out.println("UPDATING minY: "+minY+" to "+canSee.y);
            minY = canSee.y;
          }
        }
        //looking at the maxY edge
        else if ( maxY == Integer.MAX_VALUE && sensorSys.canSenseLocation(canSee.add(0, -1)) && robotControl.senseTerrainTile(canSee.add(0, -1))!=TerrainTile.OFF_MAP) {
          if(canSee.y < maxY) {
            //System.out.println("UPDATING maxY: "+maxY+" to "+canSee.y);
            maxY = canSee.y;
          }
        }
      }
    } catch (Exception e) {
      System.out.println("caught exception:");
      e.printStackTrace();
    }
  }


  /**
   * Looks for the RSBaseBuilder who (should) have turned on this on.
   * It should be a light chassis that is turned on with maxHP = 12 hp
   *
   * This should be used when creating a base at the beginning
   * Note: should only be called immediately after waking up
   * @return
   */
  protected MapLocation findBaseBuilder() {
    System.out.println("sensing nearby Bots");
    Robot[] bots = sensorSys.getBots(robotControl.getTeam());
    System.out.println("Detected "+bots.length+" bots");
    for (int i=0; i<bots.length; i++) {
      RobotInfo info = sensorSys.sensorRobotInfo(bots[i]);
      System.out.println("with info "+info.toString());
      if(info.on && info.maxHp == (double)12) {
        System.out.println("found the little bastard!");
        System.out.println("he's " + robotControl.getLocation().directionTo(info.location)+" of us");
        System.out.println("location: "+info.location.toString());
        return info.location;
      }
    }
    return null;
  }
}
