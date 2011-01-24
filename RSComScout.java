
package team122;
import battlecode.common.*;

/**
 * The scout class is for a robot with a sensor and a broadcastSys, it will go round the map looking
 * for uncovered mines and enemies and broadcast their locations in BuildDirectives or
 * FightDirectives
 * @author bovard
 */
public class RSComScout extends SensorRobotSystem {

  protected BroadcastSystem bcSys;

  public RSComScout(RobotController robotControl, SensorSystem sensorSys, BroadcastSystem broadcastSys) {
    super(robotControl, sensorSys);
    bcSys = broadcastSys;
  }


  public void go() {
    while(true) {
      //if we've seen an enemy or been shot at
      if(gameEvents.checkGameEventsAbovePriority(GameEventLevel.DIRECTIVE.priority)) {
        currentGameEventLevel = GameEventLevel.COMBAT;
        //if we can see the enemy
        if(((SensorGameEvents)gameEvents).canSeeEnemy()) {
          if(((SensorGameEvents)gameEvents).canSeeMine()) {
            MapLocation enemyMineLoc = null;
            Mine[] mines = sensorSys.getMines();

            //for each mine, see if there is a robot there and if it's on the opposite team
            for(int i =0; i<mines.length; i++) {
              Robot bot = (Robot)sensorSys.senseObjectAtLocation(mines[i].getLocation(), RobotLevel.ON_GROUND);
              if(bot != null && bot.getTeam() != robotControl.getTeam()) {
                enemyMineLoc = mines[i].getLocation();
                break;
              }
            }

            //if we found an enemy mine, call it in, otherwise just call in the nearest enemy
            if(enemyMineLoc != null) {
              bcSys.setSendFightDirective(enemyMineLoc);
            }
            else {
              bcSys.setSendFightDirective(sensorSys.getNearestOpponentLocation());
            }

          }
          else {
            bcSys.setSendFightDirective(sensorSys.getNearestOpponentLocation());
          }
        }
        //if we've taken fire or lost health just call in our location
        else {
          bcSys.setSendFightDirective(robotControl.getLocation());
        }
        //then run!
        seqFlee();
      }
      else {
        currentGameEventLevel = GameEventLevel.NORMAL;
        seqScout();
      }
    }
  }
}
