

package team122;
import battlecode.common.*;

/**
 * this controls our scout fighters, they have the build lightmotor, radar, blaster, shield
 * //TODO: flesh this class out
 * @author bovard
 */
public class FighterScoutRobotSystem extends ScoutRobotSystem {
  protected WeaponSystem weaponSys;
  protected WeaponController weaponControl;


  public FighterScoutRobotSystem(RobotController robotControl) {
    super(robotControl);
    weaponControl = (WeaponController)robotControl.components()[2];
    WeaponController[] weapons = new WeaponController[1];
    weapons[0] = weaponControl;
    weaponSys = new WeaponSystem(weapons, sensorSys);

  }

}
