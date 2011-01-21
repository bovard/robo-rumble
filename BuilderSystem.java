package team122;
import battlecode.common.*;

/**
 * Implements functions for any construction device (buildercontroller). Allows anything in
 * RobotBuildOrder to be built
 * //Note: can only be used with v1.08 installed or later
 * @author bovard
 */
public class BuilderSystem {

  private RobotController robotControl;
  private BuilderController buildControl;

  /**
   * Constructor for the BuildSystem
   * @param robotControl the RobotController
   * @param buildControl the BuilderController
   */
  public BuilderSystem(RobotController robotControl, BuilderController buildControl) {
   this.robotControl = robotControl;
   this.buildControl = buildControl;
  }

  /**
   * Takes a RobotBuildOrder and builds a robot to match the specification
   * @param buildOrder an array of Object, the first being the chasis the rest being components
   * @param location the location to build the robot
   * @return if the build was successful
   */
  protected boolean seqBuild(BuildOrder toBuild, MapLocation location) {
    robotControl.setIndicatorString(1, "selBuild");

    try {
      //build the chassis
      while(buildControl.isActive()) {
        robotControl.yield();
      }
      boolean success = true;
      if(buildControl.type() == toBuild.chassisBuilder) {
        success = actBuildChasis(toBuild.chassis,location);
      }

      //build the components, falling out if one fails
      int i = 0;
      while (i < toBuild.getComponents(buildControl.type()).length && success) {
        //wait until there is enough resources
        while(robotControl.getTeamResources() < toBuild.getComponents(buildControl.type())[i].cost
                + PlayerConstants.MINIMUM_FLUX && buildControl.isActive()) {
          robotControl.yield();
        }
        //build component
        success = actBuildComponent(toBuild.getComponents(buildControl.type())[i],location,toBuild.chassis.level);
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
    if (robotControl.getTeamResources() >= toBuild.cost + PlayerConstants.MINIMUM_FLUX
            && buildControl.canBuild(toBuild, location)) {
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
