
package team122;
import battlecode.common.*;

/**
 * A building chassis with weapon(s) and sensors
 * @author bovard
 */
public class RSGuardTower extends WeaponBuilderSensorRobotSystem {

  public RSGuardTower(RobotController robotControl, SensorSystem sensorSys, WeaponSystem weaponSys) {
    super(robotControl, sensorSys, weaponSys);

  }

  public RSGuardTower(RobotController robotControl, SensorSystem sensorSys, WeaponController weapon) {
    super(robotControl, sensorSys, weapon);

  }

  @Override
  public void go() {
    while(true) {
      seqRotateAndEngage();
    }
  }

}
