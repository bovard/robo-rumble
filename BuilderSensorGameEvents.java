
package team122;
import battlecode.common.*;

/**
 *
 * @author bovard
 */
public class BuilderSensorGameEvents extends SensorGameEvents {

  public BuilderSensorGameEvents(RobotController robotControl, CommunicationsSystem comSys, SensorSystem sensorSys) {
    super(robotControl, comSys, sensorSys);
    
  }




  /**
   * Overridden to remove the calculation of the canSeeDebris game event
   * Here we should calculate all the game events that will matter to the BuilderSensorRobotSystem
   */
  @Override
  public void calcGameEvents() {
    calcLostHealth();
    //Note: calcRecentlyLostHeath() must be called AFTER calcLostHealth()
    calcLowHealth();
    calcHasDirective();
    calcSeeMine();
    calcSeeEnemy();
  }

}
