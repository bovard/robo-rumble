
package team122;
import battlecode.common.*;

/**
 * RobotSystem for all buildings, pulls off their BuildingSensor component that all buildings
 * automatically get in component slot 1
 * @author bovard
 */
public class BuildingRobotSystem extends RobotSystem {
  SensorController sensorControl;
  SensorSystem sensorSys;


  /**
   * The constructor for the BuildingRobotSystem, one would hope it would be only called
   * by buildings
   * @param robotControl the RobotController of the Robot
   */
  public BuildingRobotSystem(RobotController robotControl) {
    super(robotControl);
    sensorControl = (SensorController)robotControl.components()[1];
    sensorSys = new SensorSystem(sensorControl, robotControl.getTeam());
  }

}
