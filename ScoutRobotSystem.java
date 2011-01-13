
package team122;
import battlecode.common.*;

/**
 * A scout robot system is assumed to be mobile and have a sensor in component slot 1
 * @author bovard
 */
public class ScoutRobotSystem extends MobileRobotSystem {
  
  protected MapLocation birthPlace;
  protected SensorController sensorControl;
  protected SensorSystem sensorSys;
  protected MapLocation[] oldDestinations;

  public ScoutRobotSystem() {
    
  }
  
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
  }
  
  /**
   * sends the bot to find an uncovered mine, when one is sensed, returns true
   * returns false if scouting is stopped for any other reason
   * @return if an uncovered mine was found
   */
  protected boolean actScoutUncoveredMine() {
    //TODO: Implement this
    
    
    return false;
  }

  /**
   * sends to bot to find an enemy unit, when one is sensed, returns true
   * returns false if scouting is stopped for any other reason (taking fire from unseen enemy)
   * @return if an enemy is sensed
   */
  protected boolean actScoutEnemy() {
    //TODO: Implement this
    return false;
  }
  
  /**
   * Chooses the next destination to go to based on birthplace and previous destinations
   */
  protected boolean actChooseNextDestination() {
    //TODO: Implement this

    return false;
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
}
