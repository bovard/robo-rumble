
package team122;
import battlecode.common.*;

/**
 * A building chassis with weapon(s) and sensors
 * @author bovard
 */
public class RSGuardTower extends FighterBuilderSensorRobotSystem {

  public RSGuardTower(RobotController robotControl, SensorSystem sensorSys, WeaponSystem weaponSys) {
    super(robotControl, sensorSys, weaponSys);

  }

  @Override
  public void go() {
    while(true) {
      seqRotateAndEngage();
    }
  }

}
