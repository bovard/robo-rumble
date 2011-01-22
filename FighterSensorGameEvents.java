
package team122;
import battlecode.common.*;

/**
 * GameEvents class for the FighterSensorRobotSystem class
 * @see FighterSensorRobotSystem
 * @author bovard
 */
public class FighterSensorGameEvents extends SensorGameEvents {

  public FighterSensorGameEvents(RobotController robotControl, CommunicationsSystem comSys, SensorSystem sensorSys) {
    super(robotControl, comSys, sensorSys);
    
  }

  /**
   * Overridden to remove the calculation of the canSeeMine game event
   * Here we should calculate all the game events that will matter to the FighterSensorRobotSystem
   */
  @Override
  public void calcGameEvents() {
    calcLostHealth();
    //Note: calcRecentlyLostHeath() must be called AFTER calcLostHealth()
    calcLowHealth();
    calcHasDirective();
    calcSeeDebris();
    calcSeeEnemy();
  }
}
