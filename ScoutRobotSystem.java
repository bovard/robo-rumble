
package team122;
import battlecode.common.*;
import java.util.Random;

/**
 * A scout robot system is assumed to be mobile and have a sensor in component slot 1
 * @author bovard
 */
public class ScoutRobotSystem extends MobileRobotSystem {
  
  protected MapLocation birthPlace;
  protected SensorController sensorControl;
  protected SensorSystem sensorSys;
  protected Random rand = new Random();

  
  public ScoutRobotSystem(RobotController robotControl) {
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

    birthPlace = robotControl.getLocation();

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
    MapLocation next = birthPlace;
    int x = rand.nextInt(30);
    int y = rand.nextInt(30);
    if (rand.nextBoolean())
      x *= -1;
    if (rand.nextBoolean())
      y *= -1;

    next.add(x, y);
    return next;
  }

  /**
   * the bot runs! 
   * right now it's pretty dumb, just tries to run back to it's birthplace
   * @return if it has fleed sucessfully
   */
  protected boolean actFlee() {
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
    navSys.setDestination(location);
    boolean safe = true;
    while(!sensorControl.canSenseSquare(location) && safe) {
      //TODO: add code here check to see if enemies are seen or under attack
      actMove();
    }

    boolean done = false;
    while(safe && !done) {
      for (int x = -1; x < 2; x++) {
        for (int y = -1; y < 2; y++) {
          if(sensorControl.canSenseSquare(location.add(x,y))) {
            try {
              if(sensorControl.senseObjectAtLocation(location.add(x,y), level)==null) {
                navSys.setDestination(location.add(x,y));
              }
            } catch (Exception e) {
              System.out.println("caught exception:");
              e.printStackTrace();
            }
          }
        }
      }
    }
    //if we found a location, try to move to that location and return the results
    if(done) {
      return seqMove();
    }
    return false;
  }
}
