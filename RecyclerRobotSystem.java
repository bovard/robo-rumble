
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
  private Random rand;

  public RecyclerRobotSystem(RobotController robotControl) {
    super(robotControl);

    buildControl = (BuilderController)robotControl.components()[2];
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
    try {
      while(!moveControl.canMove(robotControl.getDirection()) ||
              robotControl.getTeamResources() < ((Chassis)buildOrder[0]).cost) {
        actTurn(robotControl.getDirection().rotateRight());
      }
      //build the chassis
      boolean success = actBuildChasis((Chassis)buildOrder[0],robotControl.getLocation().add(robotControl.getDirection()));

      //build the components, falling out if one fails
      int i = 1;
      while (i < buildOrder.length && success) {
        //wait until there is enough resources
        while(robotControl.getTeamResources() < ((ComponentType)buildOrder[i]).cost ) {
          yield();
        }
        //build component
        success = actBuildComponent((ComponentType)buildOrder[i],robotControl.getLocation().add(robotControl.getDirection()), 
                ((Chassis)buildOrder[0]).level);
        i++;
      }
      return success;
    } catch (Exception e) {
      System.out.println("caught exception:");
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Builds a chassis at the specified location
   * @param toBuild the Chassis to build
   * @param location the MapLocation to build it on
   * @return if the build was successful
   */
  protected boolean actBuildChasis(Chassis toBuild, MapLocation location) {
    if (robotControl.getTeamResources() >= toBuild.cost) {
     try {
       buildControl.build(toBuild, location);
       yield();
       return true;
     }
     catch (Exception e) {
      System.out.println("caught exception:");
      e.printStackTrace();
     }
    }
    return false;
  }

  /**
   * Builds a component on the chassis location at location and level
   * @param toBuild the ComponentType to build
   * @param location the MapLocation to build it
   * @param level the RobotLevel of the Chasis
   * @return if the build was successful
   */
  protected boolean actBuildComponent(ComponentType toBuild, MapLocation location, RobotLevel level) {
    if (robotControl.getTeamResources() >= toBuild.cost) {
     try {
       buildControl.build(toBuild, location, level);
       yield();
       return true;
     }
     catch (Exception e) {
      System.out.println("caught exception:");
      e.printStackTrace();
     }
    }
    return false;
  }
}
