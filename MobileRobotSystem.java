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

  protected boolean actMove(MapLocation dest) {
    navSys.setDestination(dest);

    boolean safeToMove = true;
    boolean done = false;
    while(safeToMove && !done) {
      //TODO: check to make sure the bot isn't under attack here
      done = navSys.nextMove();
      yield();
    }
    return done;
  }
}
