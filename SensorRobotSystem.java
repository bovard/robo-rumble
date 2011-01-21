
package team122;
import battlecode.common.*;
import java.util.Random;

/**
 * Base behavoir class for robots with movement and sensor controllers (but no weapon or building)
 * @author bovard
 */
public class SensorRobotSystem extends RobotSystem {

  protected SensorSystem sensorSys;
  protected Random rand = new Random();

  //edges of the map, unkown initially
  protected int minX=-1, maxX=Integer.MAX_VALUE, minY=-1, maxY=Integer.MAX_VALUE;
  //new destination constants, used in chooseNewDestination()
  protected static final int NEW_DEST_RANGE = 20;

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
    robotControl.setIndicatorString(1, "chooseNextDest");
    if (navSys.getDestination()==null) {
      navSys.setDestination(birthPlace);
    }
    MapLocation next;
    int loops = 0;
    int x, y;
    do {
      next = navSys.getDestination();
      x = ((rand.nextInt(2*NEW_DEST_RANGE+1) * Clock.getRoundNum()) % (2*NEW_DEST_RANGE))
              - NEW_DEST_RANGE;
      y = ((rand.nextInt(2*NEW_DEST_RANGE+1) * Clock.getRoundNum()) % (2*NEW_DEST_RANGE))
              - NEW_DEST_RANGE;
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
    navSys.setDestination(chooseNextDestination());
    return seqMove();
  }

  /**
   * the bot runs until there are no more combat events happening
   * @return if it has fleed sucessfully
   */
  protected boolean seqFlee() {
    robotControl.setIndicatorString(1, "actFlee!!");
    navSys.setDestination(birthPlace);

    while(gameEvents.checkGameEvents(GameEventLevel.COMBAT.priority)) {
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
                && gameEvents.checkGameEvents(GameEventLevel.COMBAT.priority)) {
          navSys.setTurn(robotControl.getDirection().rotateRight());
          yield();
        }
      }
    }
    return !gameEvents.checkGameEvents(GameEventLevel.COMBAT.priority);
  }

  /**
   * Tell the robot to move to a square adjacent to location that is free of units at the
   * imputted level
   * @param location the target square
   * @param level the level to approach at
   * @return if the approach was successful
   */
  protected boolean seqApproachLocation(MapLocation location, RobotLevel level) {
    if(robotControl.getLocation()==location) {
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
    robotControl.setIndicatorString(1, "seqApproachLocation");
    navSys.setDestination(location);
    boolean hasGameEvents = gameEvents.checkGameEvents(currentGameEventLevel.priority);
    while(!sensorSys.canSenseLocation(location) && !hasGameEvents) {
      actMove();
      hasGameEvents = gameEvents.checkGameEvents(currentGameEventLevel.priority);
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
        actMove();
      }
      hasGameEvents = gameEvents.checkGameEvents(currentGameEventLevel.priority);
      loop++;
    }
    //if we max out the loops or find a gameEvent, pull out
    if (loop == maxLoops || hasGameEvents) {
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
  * @return if robot is in its current destination
  */
  @Override
  protected boolean actMove() {
    if(navSys.getDestination()==null) {
      return false;
    }
    else if(!navSys.isAtDestination() && !gameEvents.checkGameEvents(currentGameEventLevel.priority)) {
      if (navSys.setNextMove()) {
        yield();
        //check for map boundary conditions
        updateMapExtrema();
        //update your position for the GUI
        robotControl.setIndicatorString(0, "ID: " + robotControl.getRobot().getID() + " - Location: "+robotControl.getLocation().toString());
        return checkDestinationValidity();
      }
    }
    System.out.println("WARNING: Bad call to actMove");
    return false;
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

        //Check the X edges
        //looking at the minX edge
        if( minX == -1 && sensorSys.canSenseLocation(canSee.add(1, 0)) && robotControl.senseTerrainTile(canSee.add(1,0))!=TerrainTile.OFF_MAP) {
          if (canSee.x > minX) {
            //System.out.println("UPDATING minX: "+minX+" to "+canSee.x);
            minX = canSee.x;
          }
        }
        //looking at the maxX edge
        else if (maxX == Integer.MIN_VALUE && sensorSys.canSenseLocation(canSee.add(-1, 0)) && robotControl.senseTerrainTile(canSee.add(-1,0))!=TerrainTile.OFF_MAP) {
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
}
