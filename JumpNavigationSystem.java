
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

  public JumpNavigationSystem(MovementController moveControl, SensorSystem sensorSys, JumpController jumpControl) {
    super(moveControl, sensorSys);
    this.jumpControl = jumpControl;
  }
}
