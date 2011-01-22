

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
  private int minRange=Integer.MAX_VALUE, maxRange=-1;

  /**
   * Creates a new WeaponSystem, capable of handling multiple weapons
   * Note: WeaponSystem doesn't currently support bots that have weapons but not a sensor
   * TODO: change WeaponSystem to support working without a sensor (from just radio)
   *
   * @param weapons an array of the weapons currently held by the robot
   * @param sensorSys the SensorSystem for the robot
   */
  public WeaponSystem(WeaponController[] weapons, SensorSystem sensorSys) {
    this.weapons = weapons;
    this.sensorSys = sensorSys;
    mode = WeaponMode.OPEN_FIRE;
    for (int i = 0 ; i < weapons.length ; i++) {
      updateRange(weapons[i]);
    }
  }

  /**
   * Changes the minRange or maxRange if need be
   * @param weaponControl the weapon to measure against
   */
  private void updateRange(WeaponController weaponControl) {
    int range = calcRange(weaponControl);
    if (range > maxRange) {
      maxRange = range;
    }
    if (range < minRange) {
      minRange = range;
    }
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
      updateRange(updated[i]);
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
   * Checks to see if ALL weapons system are active
   * @return
   */
  public boolean allActive() {
    boolean isActive = true;
    for (int i=0; i<weapons.length; i++) {
      isActive = isActive && weapons[i].isActive();
    }
    return isActive;
  }

  /**
   * Checks to see if at least one weapon system are active
   * @return
   */
  public boolean isActive() {
    boolean isActive = false;
    for (int i=0; i<weapons.length; i++) {
      isActive = isActive || weapons[i].isActive();
    }
    return isActive;
  }


  /**
   * cycles through each weapon, if they aren't busy finds a target for it (targets enemies
   * when they are in site, debris if they are out of site)
   * @return the MapLocation where (at least one) weapon is firing
   */
  public MapLocation setFireAtRandom() {
    MapLocation toFire = null;
    RobotLevel level = null;
    if (mode!=WeaponMode.HOLD_FIRE) {
      try {
        //gets enemy bots in sensor range
        Robot[] targets;
        MapLocation[] locations;

        //if we can see an enemy robot
        if (sensorSys.getBots(sensorSys.getSensor().getRC().getTeam().opponent()).length > 0) {


          targets = sensorSys.getBots(sensorSys.getSensor().getRC().getTeam().opponent());
          locations = new MapLocation[targets.length];

          //pulls their location
          for (int i = 0; i<targets.length;i++) {
            locations[i] = sensorSys.getSensor().senseLocationOf(targets[i]);
          }



          //TODO: implement targetting logic (this is too simple)
          for (int j=0; j<weapons.length; j++){
            if (!weapons[j].isActive()) {
              if(toFire!=null) {
                if(weapons[j].withinRange(toFire)) {
                  weapons[j].attackSquare(toFire,level);
                  break;
                }
              }
              for (int i=0; i<targets.length; i++) {
                if(weapons[j].withinRange(locations[i])) {
                  toFire = locations[i];
                  level = sensorSys.getSensor().senseRobotInfo(targets[i]).chassis.level;
                  weapons[j].attackSquare(toFire,level);
                  break;
                }
              }
            }
          }
        }

        //if you can't see the enemy, but can see debris
        else if (sensorSys.getBots(Team.NEUTRAL).length > 0) {
          //Fire at neutrals
          //gets enemy bots in sensor range
          targets = sensorSys.getBots(Team.NEUTRAL);
          locations = new MapLocation[targets.length];

          //pulls their location
          for (int i = 0; i<targets.length;i++) {
            locations[i] = sensorSys.getSensor().senseLocationOf(targets[i]);
          }


          //TODO: implement targetting logic (this is too simple)
          for (int j=0; j<weapons.length; j++){
            if (!weapons[j].isActive()) {
              if(toFire!=null) {
                if(weapons[j].withinRange(toFire)) {
                  weapons[j].attackSquare(toFire,level);
                  break;
                }
              }
              for (int i=0; i<targets.length; i++) {
                if(weapons[j].withinRange(locations[i])) {
                  toFire = locations[i];
                  level = sensorSys.getSensor().senseRobotInfo(targets[i]).chassis.level;
                  weapons[j].attackSquare(toFire,level);
                  break;
                }
              }
            }
          }
        }

      } catch (Exception e) {
        System.out.println("caught exception:");
        e.printStackTrace();
      }
    }
    return toFire;
  }

  /**
   * Tells the robot to fire all avaliable weapons at MapLocatin loc
   * @param loc the MapLocation to fire at
   * @return if any weapons were set
   */
  public boolean setFireAtLocation(MapLocation loc, RobotLevel level) {
    boolean set = false;
    if(!allActive()) {
      for (int i = 0; i < weapons.length; i++) {
        if(!weapons[i].isActive() && weapons[i].withinRange(loc)) {
          try {
            weapons[i].attackSquare(loc, level);
            set = true;
          } catch (Exception e) {
            System.out.println("caught exception:");
            e.printStackTrace();
          }
        }
      }
      return set;
    }
    else {
      System.out.println("WARNING: Called setFireAtLoc when all weapons were busy");
      return false;
    }
  }


  /**
   * Returns the range of the offensive weapon in the weapon system with the shortest range
   * @param dir the direction that we're firing in
   * @return the # of squares we can setFireAtRandom
   */
  public int getMinRange() {
    return minRange;
  }
  
  /**
   * Returns the weaponSystem's max offensive range in given direction
   * @param dir the direction the robot is pointed in
   * @return the maximum # of squares the robot can shoot in
   */
  public int getMaxRange() {
    return maxRange;
  }


  /**
   * Get's the ranges associated with different weapons
   * @param weapon the WeaponController
   * @return the range
   */
  protected int calcRange(WeaponController weapon) {
    if (weapon.type() == ComponentType.SMG) {
      return 36;
    }
    else if(weapon.type() == ComponentType.BLASTER) {
      return 16;
    }
    else if (weapon.type() == ComponentType.BEAM) {
      return 36;
    }
    else if (weapon.type() == ComponentType.RAILGUN) {
      return 25;
    }
    else if (weapon.type() == ComponentType.HAMMER) {
      return 4;
    }
    System.out.println("WARNING: Fell through weaponSystem.getRange()");
    return 0;
  }
  
}
