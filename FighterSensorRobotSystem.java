
package team122;
import battlecode.common.*;

/**
 * BaseClass for robots with move, sensor and weapon
 * @author bovard
 */
public class FighterSensorRobotSystem extends SensorRobotSystem {
  protected WeaponSystem weaponSys;

  public FighterSensorRobotSystem(RobotController robotControl, SensorSystem sensorSys, WeaponSystem weaponSys) {
    super(robotControl, sensorSys);
    this.weaponSys = weaponSys;
  }



  protected boolean seqEngageEnemy(Robot bot) {
    while(sensorSys.canSenseObject(bot)) {
      Direction toEnemy = robotControl.getLocation().directionTo(sensorSys.senseLocationOfObject(bot));
      //if not facing enemy, do it
      if( toEnemy != robotControl.getDirection()) {
        if(!navSys.isActive()) {
          navSys.setTurn(toEnemy);
        }
      }
      weaponSys.fire();
      yield();
    }

    return false;
  }


}
