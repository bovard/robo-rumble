
package team122;
import battlecode.common.*;

/**
 * RobotSystem for all buildings, pulls off their BuildingSensor component that all buildings
 * automatically get in component slot 1
 * @deprecated after alpha 2.0
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
    sensorSys = new SensorSystem(robotControl, sensorControl);
  }

  /**
   * overloaded constructor for things wishing not to use the building sensor
   * @param robotControl the RobotController
   * @param sensorControl the SensorController to use
   */
  public BuildingRobotSystem(RobotController robotControl, SensorController sensorControl) {
    super(robotControl);
    this.sensorControl = sensorControl;
    sensorSys = new SensorSystem(robotControl, sensorControl);
  }

}
