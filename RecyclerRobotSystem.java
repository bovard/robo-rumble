
package team122;
import battlecode.common.*;
import java.util.Random;

/**
 * This is the System for a basic Recycler. It'll be needing to pump out two types of robots initially
 * BuilderScouts and FighterScouts (see RobotBuildOrders on how to build each, order matters)
 *
 *
 * @author bovard
 */
public class RecyclerRobotSystem extends RobotSystem {
  protected BuilderController buildControl;
  private Random rand;

  public RecyclerRobotSystem() {

  }

  public RecyclerRobotSystem(RobotController robotControl) {
    super(robotControl);

    buildControl = (BuilderController)robotControl.components()[1];
    rand = new Random();

  }

  public void go() {
    while(true) {
      selBuildScouts();
    }
  }

  /**
   * Builds FighterScouts and BuilderScouts
   * //TODO: pick a better method of deciding which one to build besides random,
   * ideally we should select between the two based on past build history and game turn
   * @return if one has been successfully made
   */
  protected boolean selBuildScouts() {
    if(rand.nextBoolean()) {
      if(robotControl.getTeamResources() > RobotBuildOrders.BUILDER_SCOUT_COST ) {
        if(actBuild(RobotBuildOrders.BUILDER_SCOUT))
          return true;
      }
    }
    else {
      if(robotControl.getTeamResources() > RobotBuildOrders.FIGHTER_SCOUT_COST ) {
        if(actBuild(RobotBuildOrders.FIGHTER_SCOUT))
          return true;
      }
    }
    return false;
  }

  /**
   * Takes a RobotBuildOrder and builds a robot to match the specification
   * @param buildOrder an array of Object, the first being the chasis the rest being components
   * @return if the build was successful
   */
  protected boolean actBuild(Object[] buildOrder) {
    //TODO: implement this

    return false;
  }
}
