
package team122;
import battlecode.common.*;

/**
 * SensorGameEvents, the events in GameEvents plus those that can be found with a sensor
 * @author bovard
 */
public class SensorGameEvents extends GameEvents implements GameEventsInterface {
  SensorSystem sensorSys;

  /**
   * Creates a SensorGameEvents class to manage GameEvents based from Sensor
   * @param robotControl the robotcontroller
   * @param sensorControl the sensorcontroller
   */
  public SensorGameEvents(RobotController robotControl, SensorSystem sensorSys) {
    super(robotControl);
    this.sensorSys = sensorSys;
  }


}
