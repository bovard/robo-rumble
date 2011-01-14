
package team122;
import battlecode.common.*;

/**
 *
 * @author bovard
 */
public class BuildingRobotSystem extends RobotSystem {
  SensorController sensorControl;
  SensorSystem sensorSys;

  public BuildingRobotSystem() {

  }

  public BuildingRobotSystem(RobotController robotControl) {
    super(robotControl);
    sensorControl = (SensorController)robotControl.components()[1];
    sensorSys = new SensorSystem(sensorControl);
  }

}
