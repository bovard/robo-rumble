
package team122;
import battlecode.common.*;

/**
 * A base class for all robots will a constructor, sensor and move controller.
 * @author bovard
 */
public class BuilderSensorRobotSystem extends SensorRobotSystem {
  
  protected BuilderSystem buildSys;

  public BuilderSensorRobotSystem(RobotController robotControl, SensorSystem sensorSys, BuilderSystem buildSys) {
    super(robotControl, sensorSys);

    this.buildSys = buildSys;
    gameEvents = new BuilderSensorGameEvents(robotControl, comSys, sensorSys);

    //check out PlayerConstants to see what each of these filters
    comSys.setAcceptMessageType(PlayerConstants.MESSAGE_INFO);
    comSys.setAcceptMessageType(PlayerConstants.MESSAGE_BUILD_DIRECTIVE);
  }


  /**
   * Approaches a location and builds the RobotBuild there
   * @param toBuild The RobotBuild to build
   * @param location The Location to build it at
   * @return if the action was performed succesfully
   */
  protected boolean seqBuildAtLocation(BuildOrder toBuild, MapLocation location) {
    robotControl.setIndicatorString(1, "seqBuildAtLocation");
    boolean success = false;

    //try to approach the location
    if(seqApproachLocation(location, robotControl.getRobot().getRobotLevel())) {

      //wait for funds, falling out if a higher priority event happens
      robotControl.setIndicatorString(1, "seqBuildAtLocation - waiting for funds");
      while(robotControl.getTeamResources() < PlayerConstants.MINIMUM_FLUX + toBuild.cost
              && !gameEvents.checkGameEventsAbove(currentGameEventLevel)) {
        yield();
      }

      //start building if there isn't a high priority game event
      if(!gameEvents.checkGameEventsAbove(currentGameEventLevel)) {
        robotControl.setIndicatorString(1, "seqBuildAtLocation - Building");
        success =  seqBuild(toBuild, location);
      }
    }
    return success;
  }



 /**
  * Takes a RobotBuildOrder and builds a robot to match the specification
  * Note: at the moment this won't be interrupted by any game events... I think that's
  * probably a good idea
  * @param buildOrder an array of Object, the first being the chasis the rest being components
  * @param location the location to build the robot
  * @return if the build was successful
  */
  protected boolean seqBuild(BuildOrder toBuild, MapLocation location) {
    robotControl.setIndicatorString(1, "seqBuild");
    boolean success = true;

    //build the chassis
    //wait for funds, and to not be active
    while(buildSys.isActive()) {
      robotControl.yield();
    }
    if(buildSys.type() == toBuild.chassisBuilder) {
      success = actBuildChasis(toBuild.chassis, location);
    }

    //build the components, falling out if one fails
    int i = 0;
    while (i < toBuild.getComponents(buildSys.type()).length && success) {
      //wait until there is enough resources
      while(robotControl.getTeamResources() < toBuild.getComponents(buildSys.type())[i].cost
              + PlayerConstants.MINIMUM_FLUX && buildSys.isActive()) {
        robotControl.yield();
      }
      //build component
      success = actBuildComponent(toBuild.getComponents(buildSys.type())[i],location,toBuild.chassis.level);
      i++;
    }
    return success;
  }


  /**
   * Builds a chassis at the specified location
   * @param toBuild the Chassis to build
   * @param location the MapLocation to build it on
   * @return if the build was successful
   */
  protected boolean actBuildChasis(Chassis toBuild, MapLocation location) {
    if (robotControl.getTeamResources() >= toBuild.cost + PlayerConstants.MINIMUM_FLUX
            && buildSys.canBuild(toBuild, location)) {
      if(buildSys.setBuildChassis(toBuild, location)){
        yield();
        return true;
      }
    }
    System.out.println("WARNING: Tried to build a chassis but fell through");
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
      if (buildSys.setBuildComponent(toBuild, location, level)) {
        yield();
        return true;
      }
      System.out.println("WARNING: Tried to build "+toBuild+" but failed to set the component");
      return false;
    }
    System.out.println("WARNING: Tried to build "+toBuild+" but didn't have enough funds");
    return false;
  }


}
