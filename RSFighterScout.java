

package team122;
import battlecode.common.*;

/**
 *
 * @author bovard
 */
public class RSFighterScout extends FighterSensorRobotSystem {
  public RSFighterScout(RobotController robotControl, SensorSystem sensorSys, WeaponSystem weaponSys) {
    super(robotControl, sensorSys, weaponSys);
  }

  @Override
  public void go() {
    while(true) {
      seqPatrolAndEngage();
    }
  }
}
