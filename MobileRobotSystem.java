package team122;
import battlecode.common.*;

/**
 * Serves as a base class for all mobile robots. 
 * @author bovard
 */
public class MobileRobotSystem extends RobotSystem {

  protected NavigationSystem navSys;

  public MobileRobotSystem() {

  }

  public MobileRobotSystem(RobotController robotControl) {
    super(robotControl);
    navSys = new NavigationSystem(moveControl);
  }

  

  private boolean actMove(MapLocation dest) {
    navSys.setDestination(dest);

    boolean safeToMove = true;
    boolean done = false;
    while(safeToMove && !done) {

      done = navSys.nextMove();
      yield();
    }

    //TODO: check to make sure the bot isn't under attack here
    return done;
  }

}
