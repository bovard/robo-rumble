
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
public class RecyclerRobotSystem extends BuildingRobotSystem {
  protected BuilderController buildControl;
  protected BuilderSystem buildSys;
  private Random rand;


  public RecyclerRobotSystem(RobotController robotControl) {
    super(robotControl);

    buildControl = (BuilderController)robotControl.components()[2];
    buildSys = new BuilderSystem(robotControl, buildControl);
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
      //wait until we have enough resources
      while(robotControl.getTeamResources() < RobotBuildOrders.BUILDER_SCOUT_COST ) {
        yield();
      }
      //build
      if(seqBuild(RobotBuildOrders.BUILDER_SCOUT))
        return true;
    }
    else {
      //wait until we have enough resources
      while(robotControl.getTeamResources() < RobotBuildOrders.FIGHTER_SCOUT_COST ) {
        yield();
      }
      //build
      if(seqBuild(RobotBuildOrders.FIGHTER_SCOUT))
        return true;
    }
    return false;
  }

  /**
   * Takes a RobotBuildOrder and builds a robot to match the specification
   * @param buildOrder an array of Object, the first being the chasis the rest being components
   * @return if the build was successful
   */
  protected boolean seqBuild(Object[] buildOrder) {

  //keep rotating until we find a free square and have enough resources
    while(!moveControl.canMove(robotControl.getDirection()) ||
            robotControl.getTeamResources() < ((Chassis)buildOrder[0]).cost) {
      actTurn(robotControl.getDirection().rotateRight());
    }
    //call to the build order to initiate the build sequence
    return buildSys.seqBuild(buildOrder,robotControl.getLocation().add(robotControl.getDirection()));
  }

}
