
package team122;
import battlecode.common.*;

/**
 * The GameEventSystem for the RSRegenFighter class. Noteably this adds back calculating the
 * CRITICAL game event hasLowHealth. This allows the robot to drop out of combat if it has low health
 * @author bovard
 */
public class RSRegenFighterGE extends WeaponBuilderSensorGameEvents {

  public RSRegenFighterGE(RobotController robotControl, CommunicationsSystem comSys, SensorSystem sensorSys) {
    super(robotControl, comSys, sensorSys);
    
  }

  /**
   * Overridden to add back low-health
   * Here we should calculate all the game events that will matter to the RSRegenFighter System
   */
  @Override
  public void calcGameEvents() {
    calcLowHealth();
    super.calcGameEvents();
  }


}
