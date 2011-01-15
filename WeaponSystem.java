

package team122;
import battlecode.common.*;

/**
 * The WeaponSystem takes care of shooting for the robot. It relies on imput from
 * sensors as well as radio broadcasts to find targets. It requires access to at least one
 * WeaponController to work.
 *
 *
 * //TODO: radio input not implemented
 *
 * Note: might have to have access to the sensor to directly call output
 *
 * @author bovard
 */
public class WeaponSystem {

  private WeaponController[] weapons;
  private SensorSystem sensorSys;
  private int mode;

  /**
   * Creates a new WeaponSystem, capable of handling multiple weapons
   * Note: WeaponSystem doesn't currently support bots that have weapons but not a sensor
   * TODO: change WeaponSystem to support working without a sensor (from just radio)
   *
   * @param weapons an array of the weapons currently held by the robot
   * @param sensor the SensorSystem for the robot
   */
  public WeaponSystem(WeaponController[] weapons, SensorSystem sensor) {
    this.weapons = weapons;
    this.sensorSys = sensor;
    mode = WeaponMode.OPEN_FIRE;
  }

  /**
   * Adds a weapon to the weapon system
   * @param weapon WeaponController of the weapon to add
   */
  public void addWeapon(WeaponController weapon) {
    WeaponController[] updated = new WeaponController[weapons.length+1];
    updated[weapons.length]=weapon;
    for (int i=0; i<weapons.length;i++) {
      updated[i]=weapons[i];
    }
    weapons = updated;
  }

  /**
   * Changes the WeaponSystem mode from WeaponMode.java
   * @param mode
   */
  public void changeWeaponMode(int mode) {
    this.mode = mode;
  }

  /**
   * cycles through each weapon, if they aren't busy finds a target for it
   */
  public void fire() {
    if (mode!=WeaponMode.HOLD_FIRE) {
      try {
        //gets enemy bots in sensor range
        Robot[] targets = sensorSys.getBots(weapons[0].getRC().getTeam().opponent());
        MapLocation[] locations = new MapLocation[targets.length];

        //pulls their location
        for (int i = 0; i<targets.length;i++) {
          locations[i] = sensorSys.getSensor().senseLocationOf(targets[i]);
        }


        //TODO: implement targetting logic (this is too simple)
        for (int j=0; j<weapons.length; j++){
          if (!weapons[j].isActive()) {
            for (int i=0; i<targets.length; i++) {
              if(weapons[j].withinRange(locations[i])) {
                weapons[j].attackSquare(locations[i], sensorSys.getSensor().senseRobotInfo(targets[i]).chassis.level);
                break;
              }
            }
          }
        }
      } catch (Exception e) {
        System.out.println("caught exception:");
        e.printStackTrace();
      }
    }
  }

}
