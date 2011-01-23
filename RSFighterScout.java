

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
      //if we're in combat
      if(gameEvents.checkGameEventsAbovePriority(GameEventLevel.MISSION.priority)) {
        if(((SensorGameEvents)gameEvents).canSeeEnemy()) {
          currentGameEventLevel = GameEventLevel.COMBAT;
          seqEngageEnemy(sensorSys.getNearestOpponent());
        }
        else {
          currentGameEventLevel = GameEventLevel.COMBAT;
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
