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
   * Checks to see if the build controller is active
   * @return if the controller is active
   */
  public boolean isActive() {
    return buildControl.isActive();
  }

  /**
   * checks to see the type of the build controller
   * @return the ComponentType of the buildcontroller
   */
  public ComponentType type() {
    return buildControl.type();
  }

  /**
   * Checks to see if the Chassis can be built at that location
   * @param chassis the chassis to build
   * @param loc the MapLocation to build it at
   * @return if the chassis can be built (not taking into account money or active status)
   */
  public boolean canBuild(Chassis chassis, MapLocation loc) {
    return buildControl.canBuild(chassis, loc);
  }

  /**
   * sets the build controller to build a Chassis at MapLocation next time yield is called
   * @param chassis The Chassis to build
   * @param loc The MapLocation to build it
   * @return if the set was sucessful
   */
  public boolean setBuildChassis(Chassis chassis, MapLocation loc) {
    try {
      buildControl.build(chassis, loc);
      return true;
    } catch (Exception e) {
      System.out.println("caught exception:");
      e.printStackTrace();
    }
    return false;
  }

  /**
   * sets the build controller to build a Component at MapLocation and RobotLevel next
   * time yield is called
   * @param toBuild the ComponentType to build
   * @param loc The MapLocation to build it
   * @param level The RobotLevel to build it
   * @return if the build was successful
   */
  public boolean setBuildComponent(ComponentType toBuild, MapLocation loc, RobotLevel level) {
    try {
      buildControl.build(toBuild, loc, level);
      return true;
    } catch (Exception e) {
      System.out.println("caught exception:");
      e.printStackTrace();
    }
    return false;
  }





}
