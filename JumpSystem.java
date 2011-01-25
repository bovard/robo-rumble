

package team122;
import battlecode.common.*;

/**
 * Class to wrap up at least one jump controller
 * @author bovard
 */
public class JumpSystem {
  private JumpController[] jumpControls;
  
  public JumpSystem(JumpController[] jumpControls) {
    this.jumpControls = jumpControls;
  }
  
  public JumpSystem(JumpController jumpControl) {
    this.jumpControls = new JumpController[] {jumpControl};
  }

  /**
   * Checks to see if all jumpControls are active
   * @return if all jumpControls are active
   */
  public boolean allActive() {
    boolean active = true;
    for (int i=0; i < jumpControls.length; i++) {
      active = active && jumpControls[i].isActive();
    }
    return active;
  }

  /**
   * Checks to see if at least one jumpControl is active
   * @return if at least one jump control is active
   */
  public boolean atLeastOneActive() {
    boolean active = false;
    for (int i=0; i < jumpControls.length; i++) {
      active = active || jumpControls[i].isActive();
    }
    return active;
  }

  public boolean jumpTo(MapLocation loc) {
    if(!allActive()) {
      //find a non-active jump controller
      int i=0;
      boolean done = false;
      while(!done && i<jumpControls.length) {
        done = !jumpControls[i].isActive();
      }
      try {
        jumpControls[i].jump(loc);
        return true;
      } catch (Exception e) {
        System.out.println("caught exception:");
        e.printStackTrace();
        System.out.println("WARNING: tried to jump but failed!");
        return false;
      }
    }
    System.out.println("WARNING: tried to jump when all controllers were busy!");
    return false;
  }


}
