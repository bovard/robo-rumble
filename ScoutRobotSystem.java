/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
    
    sensorControl = (SensorController)robotControl.components()[1];
    sensorSys = new SensorSystem(sensorControl);
  }
  
  /**
   * sends the bot to find an uncovered mine, when one is sensed, returns true
   * returns false if scouting is stopped for any other reason
   * @return if an uncovered mine was found
   */
  protected boolean actScoutMine() {
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

}
