
package team122;
import battlecode.common.*;
import java.util.Random;

/**
 * A scout robot system is assumed to be mobile and have a sensor in component slot 1
 * @author bovard
 */
public class SensorRobotSystem extends MobileRobotSystem {
  //we don't know the bounds of the map to start with, we'll fill these in as we find them
  protected int minX=-1, maxX=Integer.MAX_VALUE, minY=-1, maxY=Integer.MAX_VALUE;
  
  protected SensorController sensorControl;
  protected SensorSystem sensorSys;
  protected Random rand = new Random();

  /**
   * Creates a new SensorRobotSystem, requires that the robot have a movementcontroller
   * and a sensor of some sort
   * @param robotControl
   */
  public SensorRobotSystem(RobotController robotControl) {
    super(robotControl);

    //for our scouts we want to make sure we build the sensor as the first thing after
    //building the chasis, thus they will always be in teh components[1] slot
    //the bot we start with, however, has the sensor, SIGHT, in components[2], hence
    //the extra if statement
    if (robotControl.components()[2].type() == ComponentType.SIGHT)
      sensorControl = (SensorController)robotControl.components()[2];
    else
      sensorControl = (SensorController)robotControl.components()[1];
    sensorSys = new SensorSystem(sensorControl);

    

  }
  


  /**
   * sends to bot to find an enemy unit, when one is sensed, returns true
   * returns false if scouting is stopped for any other reason (ex taking fire from
   * an unseen enemy)
   * @return if an enemy is sensed
   */
  protected boolean seqScoutEnemy() {
    navSys.setDestination(chooseNextDestination());
    //TODO: Implement this
    return false;
  }
  
  /**
   * Chooses the next destination to go to based on birthplace and previous destinations
   * currently chooses a random location within 30 squares horizontally and vertically
   * of the robots birthplace
   */
  protected MapLocation chooseNextDestination() {
    MapLocation next;
    int x, y;
    do {
      next = birthPlace;
      //TODO: make these not hard-coded values
      x = rand.nextInt(70);
      y = rand.nextInt(70);
      if (rand.nextBoolean())
        x *= -1;
      if (rand.nextBoolean())
        y *= -1;
      next = next.add(x, y);
    } while (next.x < minX || next.x > maxX || next.y < minY || next.y > maxY);
    return next;
  }

  /**
   * the bot runs! 
   * right now it's pretty dumb, just tries to run back to it's birthplace
   * @return if it has fleed sucessfully
   */
  protected boolean actFlee() {
    robotControl.setIndicatorString(1, "actFlee!!");
    navSys.setDestination(birthPlace);

    boolean done = false;
    while(!done){
      done = navSys.nextMove();
    }

    return done;
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
    navSys.setDestination(location);
    boolean safe = true;
    while(!sensorControl.canSenseSquare(location) && safe) {
      //TODO: add code here check to see if enemies are seen or under attack
      actMove();
    }

    robotControl.setIndicatorString(1, "seqApproachLocation - canSee");
    boolean done = false;
    while(safe && !done) {
      for (int x = -1; x < 2; x++) {
        for (int y = -1; y < 2; y++) {
          if(sensorControl.canSenseSquare(location.add(x,y))) {
            try {
              if(sensorControl.senseObjectAtLocation(location.add(x,y), level)==null) {
                navSys.setDestination(location.add(x,y));
                done = true;
              }
            } catch (Exception e) {
              System.out.println("caught exception:");
              e.printStackTrace();
            }
          }
        }
      }
    }

    robotControl.setIndicatorString(1, "seqApproachLocation - foundFree");
    //if we found a location, try to move to that location and return the results
    if(done) {
      return seqMove();
    }
    return false;
  }

  /**
   * Called to move multiple times to a destination
   * @param dest place to move to
   * @return if the destination was reached safely
   */
  @Override
  protected boolean seqMove(MapLocation dest) {
    navSys.setDestination(dest);

    boolean safeToMove = true;
    boolean done = false;
    while(safeToMove && !done) {
      //TODO: check to make sure the bot isn't under attack here
      done = actMove();
    }
    return done;
  }

  /**
   * Called to move multiple times to a destination, assumes the destination is already set
   * @param dest place to move to
   * @return if the destination was reached safely
   */
  @Override
  protected boolean seqMove() {
    boolean safeToMove = true;
    boolean done = false;
    while(safeToMove && !done) {
      //TODO: check to make sure the bot isn't under attack here
      done = actMove();
    }
    return done;
  }

 /**
   * Called to move once (and yield) Assumes the robot already has a destination
   * @return if robot is in its current destination
   */
  @Override
  protected boolean actMove() {
    if(navSys.getDestination()==null)
      return false;
    boolean done = navSys.nextMove();
    yield();
    //check for map boundary conditions
    updateMapExtrema();
    //update your position for the GUI
    robotControl.setIndicatorString(0, robotControl.getLocation().toString());
    done = !checkDestinationValidity() || done;
    return done;
  }

  /**
   * checks to see if the destination is a valid one
   * @return the validity of the destination
   */
  protected boolean checkDestinationValidity() {
    //if the destination is out of x bounds it's invalid, return false
    if (navSys.getDestination().x < minX || navSys.getDestination().x > maxX) {
      robotControl.setIndicatorString(2, "BAD X DEST: "+minX+"<"+navSys.getDestination().x+"<"+maxX );
      return false;
    }
    //if the destination is out of y bounds it's invalid, return false
    if (navSys.getDestination().y < minY || navSys.getDestination().y > maxY) {
      robotControl.setIndicatorString(2, "BAD Y DEST: "+minY+"<"+navSys.getDestination().y+"<"+maxY );
      return false;
    }
    //check if we can currently sense the destination
    if (sensorControl.canSenseSquare(navSys.getDestination()))
      try {
        //check to see if there is someone there at the robots level
        if(sensorControl.senseObjectAtLocation(navSys.getDestination(), robotControl.getRobot().getRobotLevel())==null) {
          //if there isn't check to see if the location can be traversed and return the result
          return robotControl.senseTerrainTile(navSys.getDestination()).isTraversableAtHeight(robotControl.getRobot().getRobotLevel());
        }
        else {
          //if there is someone there, the destination is invalid
          return false;
        }
      } catch (Exception e) {
        System.out.println("caught exception:");
        e.printStackTrace();
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
        if( sensorControl.canSenseSquare(canSee.add(1, 0)) && robotControl.senseTerrainTile(canSee.add(1,0))!=TerrainTile.OFF_MAP) {
          if (canSee.x > minX) {
            System.out.println("UPDATING minX: "+minX+" to "+canSee.x);
            minX = canSee.x;
          }
        }
        //looking at the maxX edge
        else if (sensorControl.canSenseSquare(canSee.add(-1, 0)) && robotControl.senseTerrainTile(canSee.add(-1,0))!=TerrainTile.OFF_MAP) {
          if(canSee.x < maxX) {
            System.out.println("UPDATING maxX: "+maxX+" to "+canSee.x);
            maxX = canSee.x;
          }
        }

        //Check the Y edges
        //looking at the minY edge
        if( sensorControl.canSenseSquare(canSee.add(0, 1)) && robotControl.senseTerrainTile(canSee.add(0,1))!=TerrainTile.OFF_MAP) {
          if (canSee.y > minY) {
            System.out.println("UPDATING minY: "+minY+" to "+canSee.y);
            minY = canSee.y;
          }
        }
        //looking at the maxY edge
        else if (sensorControl.canSenseSquare(canSee.add(0, -1)) && robotControl.senseTerrainTile(canSee.add(0, -1))!=TerrainTile.OFF_MAP) {
          if(canSee.y < maxY) {
            System.out.println("UPDATING maxY: "+maxY+" to "+canSee.y);
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
