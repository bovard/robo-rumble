package team122;
import battlecode.common.*;

/**
 * Serves as a base class for all mobile robots (robots able to move)
 * @author bovard
 */
public class MobileRobotSystem extends RobotSystem {

  protected NavigationSystem navSys;

  /**
   * creates a new MobileRobotSystem, takes the moveControl and adds a navSystem
   * @param robotControl The robotController
   */
  public MobileRobotSystem(RobotController robotControl) {
    super(robotControl);
    navSys = new NavigationSystem(moveControl);
  }

  /**
   * Called to move multiple times to a destination
   * @param dest place to move to
   * @return if the destination was reached safely
   */
  protected boolean seqMove(MapLocation dest) {
    navSys.setDestination(dest);

    boolean safeToMove = true;
    boolean done = false;
    while(safeToMove && !done) {
      //TODO: check to make sure the bot isn't under attack here
      done = actMove();
    }
    return done;
  }

  /**
   * Called to move multiple times to a destination, assumes the destination is already set
   * @param dest place to move to
   * @return if the destination was reached safely
   */
  protected boolean seqMove() {
    boolean safeToMove = true;
    boolean done = false;
    while(safeToMove && !done) {
      //TODO: check to make sure the bot isn't under attack here
      done = actMove();
    }
    return done;
  }

  /**
   * Called to move once (and yield) Assumes the robot already has a destination
   * @return if robot is in its current destination
   */
  protected boolean actMove() {
    if(navSys.getDestination()==null)
      return false;
    boolean done = navSys.setNextMove();
    yield();
    return done;
  }

  protected boolean actMoveForward() {
    if(moveControl.canMove(robotControl.getDirection()) && !moveControl.isActive()) {
      try {
        //System.out.println("Moving out!");
        moveControl.moveForward();
        yield();
        return true;
      } catch (Exception e) {
        System.out.println("caught exception:");
        e.printStackTrace();
        return false;
      }
    }
    return false;
  }

  /**
   * checks to see if the robot can move backwards and move backwards if it can
   * @return if the move was performed successfully
   */
  protected boolean actMoveBackward() {
    if(moveControl.canMove(robotControl.getDirection().opposite())
            && !moveControl.isActive()) {
      try {
        moveControl.moveBackward();
        yield();
        return true;
      } catch (Exception e) {
        System.out.println("caught exception:");
        e.printStackTrace();
        return false;
      }
    }
    return false;
  }

}
