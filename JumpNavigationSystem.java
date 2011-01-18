
package team122;
import battlecode.common.*;

/**
 * A navigation system for robots with a jump component
 * extends NavigationSystem
 * //TODO: Implement this class
 * @author bovard
 */
public class JumpNavigationSystem extends NavigationSystem {
  protected JumpController jumpControl;

  public JumpNavigationSystem(MovementController moveControl, JumpController jumpControl) {
    super(moveControl);
    this.jumpControl = jumpControl;
  }



}
