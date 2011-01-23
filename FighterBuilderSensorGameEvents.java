
package team122;
import battlecode.common.*;

/**
 * The GameEvents class for the FighterBuidlerSensorRobotClass
 * @author bovard
 */
public class FighterBuilderSensorGameEvents extends BuilderSensorGameEvents {

  public FighterBuilderSensorGameEvents(RobotController robotControl, CommunicationsSystem comSys,
          SensorSystem sensorSys) {
    super(robotControl, comSys, sensorSys);
  }

  /**
   * Overridden to add back calculating the seeDebirs game event
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
    calcSeeDebris();
  }
}
