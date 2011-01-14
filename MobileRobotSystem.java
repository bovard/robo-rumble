package team122;
import battlecode.common.*;

/**
 * Serves as a base class for all mobile robots. 
 * @author bovard
 */
public class MobileRobotSystem extends RobotSystem {

  protected NavigationSystem navSys;


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
    robotControl.setIndicatorString(1, "seqMove");
    navSys.setDestination(dest);

    boolean safeToMove = true;
    boolean done = false;
    while(safeToMove && !done) {
      //TODO: check to make sure the bot isn't under attack here
      done = actMove();
    }
    robotControl.setIndicatorString(1, "seqMove -exit");
    return done;
  }

  /**
   * Called to move multiple times to a destination, assumes the destination is already set
   * @param dest place to move to
   * @return if the destination was reached safely
   */
  protected boolean seqMove() {
    robotControl.setIndicatorString(1, "seqMove");
    boolean safeToMove = true;
    boolean done = false;
    while(safeToMove && !done) {
      //TODO: check to make sure the bot isn't under attack here
      done = actMove();
    }
    robotControl.setIndicatorString(1, "seqMove -ext");
    return done;
  }

  /**
   * Called to move once (and yield) Assumes the robot already has a destination
   * @return if robot is in its current destination
   */
  protected boolean actMove() {
    robotControl.setIndicatorString(1, "actMove");
    if(navSys.getDestination()==null)
      return false;
    boolean done = navSys.nextMove();
    yield();
    robotControl.setIndicatorString(1, "actMove - exit");
    return done;
  }


}
