
package team122;
import battlecode.common.*;

/**
 * Navigation with a sensor
 * //TODO: add some navigation methods that take advantage of a sensor here
 * @author bovard
 */
public class SensorNavigationSystem extends NavigationSystem {
  protected SensorSystem sensorSys;

  public SensorNavigationSystem(RobotController robotControl, MovementController moveControl, SensorSystem sensorSys) {
    super(robotControl, moveControl);
    this.sensorSys = sensorSys;

  }

}
