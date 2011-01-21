
package team122;
import battlecode.common.*;

/**
 * A navigation system for robots with a jump component
 * extends BlindNavigationSystem
 * //TODO: Implement this class
 * @author bovard
 */
public class JumpNavigationSystem extends BlindNavigationSystem {
  protected JumpController jumpControl;
  protected SensorSystem sensorSys;

  public JumpNavigationSystem(MovementController moveControl, SensorSystem sensorSys, JumpController jumpControl) {
    super(moveControl);
    this.jumpControl = jumpControl;
    this.sensorSys = sensorSys;
  }



}
