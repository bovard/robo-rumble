package team122;
import battlecode.common.*;

/**
 * Implements functions for any construction device (buildercontroller). Allows anything in
 * RobotBuildOrder to be built
 * @author bovard
 */
public class BuilderSystem {

  private RobotController robotControl;
  private BuilderController buildControl;
  private MovementController moveControl;

  /**
   * Constructor for the BuildSystem
   * @param robotControl the RobotController
   * @param buildControl the BuilderController
   * @param moveControl the MovementController (used to see if you can build in a space)
   */
  public BuilderSystem(RobotController robotControl, BuilderController buildControl, MovementController moveControl) {
   this.robotControl = robotControl;
   this.buildControl = buildControl;
   this.moveControl = moveControl;
  }

    /**
   * Takes a RobotBuildOrder and builds a robot to match the specification
   * @param buildOrder an array of Object, the first being the chasis the rest being components
   * @return if the build was successful
   */
  protected boolean seqBuild(Object[] buildOrder, MapLocation location) {


    try {
      //build the chassis
      while(buildControl.isActive()) {
        robotControl.yield();
      }
      boolean success = actBuildChasis((Chassis)buildOrder[0],location);

      //build the components, falling out if one fails
      int i = 1;
      while (i < buildOrder.length && success) {
        //wait until there is enough resources
        while(robotControl.getTeamResources() < ((ComponentType)buildOrder[i]).cost &&
                buildControl.isActive()) {
          robotControl.yield();
        }
        //build component
        success = actBuildComponent((ComponentType)buildOrder[i],location,((Chassis)buildOrder[0]).level);
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
    if (robotControl.getTeamResources() >= toBuild.cost && moveControl.canMove(robotControl.getLocation().directionTo(location))) {
     try {
       buildControl.build(toBuild, location);
       robotControl.yield();
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
   * Builds a component at location and level (there better be a chassis there!)
   * @param toBuild the ComponentType to build
   * @param location the MapLocation to build it
   * @param level the RobotLevel of the Chasis
   * @return if the build was successful
   */
  protected boolean actBuildComponent(ComponentType toBuild, MapLocation location, RobotLevel level) {
    if (robotControl.getTeamResources() >= toBuild.cost) {
     try {
       buildControl.build(toBuild, location, level);
       robotControl.yield();
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
