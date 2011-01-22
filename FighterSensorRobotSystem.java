
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
    this.gameEvents = new FighterSensorGameEvents(robotControl, comSys, sensorSys);
  }

  /**
   * The main loop for the FighterSensorRobotSystem, it should never fall out
   */
  @Override
  public void go() {
    while(true) {
      seqPatrolAndEngage();
    }
  }



  /**
   * Scouts the map and engages any enemies it sees
   * @return true
   */
  protected boolean seqPatrolAndEngage() {
    robotControl.setIndicatorString(1, "seqPatrolAndEngage");
    currentGameEventLevel = GameEventLevel.NORMAL;
    seqScout();
    if(((SensorGameEvents)gameEvents).canSeeEnemy()) {
      seqEngageEnemy(sensorSys.getNearestOpponent());
    }
    return true;
  }






  /**
   * While the enemy bot is in range, engage it
   * @param bot The enemy bot to engage
   * @return if we can still see the enemy bot
   */
  protected boolean seqEngageEnemy(Robot bot) {
    robotControl.setIndicatorString(1, "seqEngageEnemy");
    currentGameEventLevel = GameEventLevel.COMBAT;

    while(sensorSys.canSenseObject(bot)) {
      MapLocation enemyLoc = sensorSys.senseLocationOfObject(bot);
      MapLocation ourLoc = robotControl.getLocation();
      Direction toEnemy = ourLoc.directionTo(enemyLoc);
      //Set a movement action
      if(!navSys.isActive()) {
        //if not facing the enemy, turn to face them
        if( toEnemy != robotControl.getDirection()) {
            navSys.setTurn(toEnemy);
        }
        //if we're too far away, close the distance
        else if (ourLoc.distanceSquaredTo(enemyLoc) > weaponSys.getMinRange()) {
          if(navSys.canMove(robotControl.getDirection())) {
            navSys.setMoveForward();
          }
        }
        //if we're too close back off a bit
        else if (ourLoc.distanceSquaredTo(ourLoc) < weaponSys.getMinRange()) {
          if(navSys.canMove(robotControl.getDirection().opposite())) {
            navSys.setMoveBackward();
          }
        }
      }
      //Now set our weapons to fire
      //try to fire at the robot
      if(!weaponSys.allActive()) {
        weaponSys.setFireAtLocation(enemyLoc, bot.getRobotLevel());
      }
      //if you can't fire all weapons at the enemy, just try to fire at any enemy robot
      if(!weaponSys.allActive()) {
        weaponSys.setFireAtRandom();
      }

      //finally yield
      yield();
    }

    return !sensorSys.canSenseObject(bot);
  }

  /**
   * Overrides SensorRobotSystem.actMove() to allow for weapon capabilities
   * @return if it was performed correctly
   */
  @Override
  protected boolean actMove() {
    weaponSys.setFireAtRandom();
    return super.actMove();
  }


}
