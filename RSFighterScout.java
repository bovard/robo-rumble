

package team122;
import battlecode.common.*;

/**
 *
 * @author bovard
 */
public class RSFighterScout extends WeaponBuilderSensorRobotSystem {
  public RSFighterScout(RobotController robotControl, SensorSystem sensorSys, WeaponSystem weaponSys) {
    super(robotControl, sensorSys, weaponSys);
  }

  public RSFighterScout(RobotController robotControl, SensorSystem sensorSys, WeaponController weapon) {
    super(robotControl, sensorSys, weapon);
  }

  @Override
  public void go() {
    while(true) {

      //if we're in combat
      if(gameEvents.checkGameEventsAbove(GameEventLevel.MISSION)) {
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
