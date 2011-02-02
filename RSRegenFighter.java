

package team122;
import battlecode.common.*;

/**
 *
 * @author bovard
 */
public class RSRegenFighter extends WeaponBuilderSensorRobotSystem {
  public RSRegenFighter(RobotController robotControl, SensorSystem sensorSys, WeaponSystem weaponSys) {
    super(robotControl, sensorSys, weaponSys);
  }

  public RSRegenFighter(RobotController robotControl, SensorSystem sensorSys, WeaponController weapon) {
    super(robotControl, sensorSys, weapon);
  }

  public RSRegenFighter(RobotController robotControl, SensorSystem sensorSys) {
    super(robotControl, sensorSys);
  }

  @Override
  public void go() {
    while(true) {
      //check to see if a CRITICAL EVENT has happened (aka we have low health)
      if(gameEvents.checkGameEventsAbove(GameEventLevel.CRITICAL)) {
        currentGameEventLevel = GameEventLevel.CRITICAL;
        while(gameEvents.isBelowHalfHeath()) {
          if(navSys.canMove(robotControl.getDirection().opposite())) {
            navSys.setMoveBackward();
          }
          else {
            //try to force a move somewhere backwards
            setForceMove(false);
          }
          yield();
        }
      }
      //if we're in combat
      else if(gameEvents.checkGameEventsAbove(GameEventLevel.MISSION)) {
        currentGameEventLevel = GameEventLevel.COMBAT;
        if(((SensorGameEvents)gameEvents).canSeeEnemy()) {
          seqEngageEnemy(sensorSys.getNearestOpponent());
        }
        else {
          seqRotateToUnSeenEnemy();
        }
      }
      else {
        currentGameEventLevel = GameEventLevel.NORMAL;
        seqScout();
      }
    }
  }
}
