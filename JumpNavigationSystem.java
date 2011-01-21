
package team122;
import battlecode.common.*;

/**
 * A navigation system for robots with a jump component
 * extends SensorNavigationSystem
 * //TODO: Implement this class
 * @author bovard
 */
public class JumpNavigationSystem extends SensorNavigationSystem {
  protected JumpController jumpControl;

  public JumpNavigationSystem(RobotController robotControl, MovementController moveControl, SensorSystem sensorSys, JumpController jumpControl) {
    super(robotControl, moveControl, sensorSys);
    this.jumpControl = jumpControl;
  }
}
