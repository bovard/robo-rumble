
package team122;
import battlecode.common.*;

/**
 * AttackTurrets are buildings with lots (or at least 1) of weapons and a sensor
 * @deprecated From Alpha 2.0
 * @author bovard
 */
public class AttackTurretRobotSystem extends FighterSensorRobotSystem {


  /**
   * The AttackTurretRobotSystem, it will continue to check for any new weapons and add
   * them to the weapon system
   * @param robotControl The robotController
   * @param sensorControl the sensorController
   * @param weapons the WeaponController[]
   */
  public AttackTurretRobotSystem(RobotController robotControl, SensorSystem sensorSys, WeaponSystem weaponSys) {
    super(robotControl, sensorSys, weaponSys);
    gameEvents = new FighterSensorGameEvents(robotControl, comSys, sensorSys);
    
    
  }

  @Override
  public void go() {
    while(true) {
      seqEngageEnemy();
      seqCheckNewWeapons();
    }
  }


  /**
   * Finds the enemy and engages until the enemy is destroyed (or moves away)
   * @return if there is still an enemy
   */
  protected boolean seqEngageEnemy() {
    robotControl.setIndicatorString(1, "seqEngageEnemy");
    //if we can see the enemy or we can rotate to see them
    if(((SensorGameEvents)gameEvents).canSeeEnemy()) {
      robotControl.setIndicatorString(1, "seqEngageEnemy - enemy in sight");
      //while we can see the enemy, setFireAtRandom at them or move toward them
      try {
        while(((SensorGameEvents)gameEvents).canSeeEnemy()) {
          MapLocation toFire = weaponSys.setFireAtRandom();
          if(toFire != null) {
            robotControl.setIndicatorString(1, "seqEngageEnemy - Fire!");
            actTurn(robotControl.getLocation().directionTo(toFire));
          }
          else {
            robotControl.setIndicatorString(1, "seqEngageEnemy - Turn");
            MapLocation enemyLoc = sensorSys.getNearestOpponentLocation();
            actTurn(robotControl.getLocation().directionTo(enemyLoc));
          }
        }
      } catch (Exception e) {
        System.out.println("caught exception:");
        e.printStackTrace();
        return false;
      }
    }
    else {
      seqRotate();
    }
    return false;
  }


  /**
   * Checks for new weapons and adds them to WeaponSys if it finds any
   * @return if completed successfully
   */
  protected boolean seqCheckNewWeapons() {
    //grab any new components
    ComponentController[] components = robotControl.newComponents();
    //if there are some
    if (components.length>0) {
      //if they are the right type (Weapon of some sort)
      for (int i = 0; i<components.length;i++) {
        if (components[i].type() == ComponentType.SMG
                || components[i].type() == ComponentType.BLASTER
                || components[i].type() == ComponentType.RAILGUN
                || components[i].type() == ComponentType.MEDIC
                || components[i].type() == ComponentType.BEAM
                || components[i].type() == ComponentType.HAMMER ) {
          //add them to the weaponSystem
          weaponSys.addWeapon((WeaponController)components[i]);
        }
      }
    }
    return true;
  }



  /**
   * Turns the robot and continues to look for the enemy
   * @return if an enemy is seens
   */
  protected boolean seqRotate() {
    robotControl.setIndicatorString(1, "seqRotate");
    switch(sensorSys.getBreadth()) {
      case PlayerConstants.TELESCOPE_TURNS:
        weaponSys.setFireAtRandom();
        actTurn(robotControl.getDirection().rotateRight());
        break;
      case PlayerConstants.SIGHT_TURNS:
        weaponSys.setFireAtRandom();
        actTurn(robotControl.getDirection().rotateRight().rotateRight());
        break;
      case PlayerConstants.RADAR_TURNS:
        weaponSys.setFireAtRandom();
        actTurn(robotControl.getDirection().opposite());
        break;
      case PlayerConstants.SATELLITE_TURNS:
        //in this case the sensor can see in all directions so the bot doesn't need to rotate
        break;
    }
    return true;
  }
}
