
package team122;
import battlecode.common.*;

/**
 * The scout class is for a robot with a sensor and a comSys, it will go round the map looking
 * for uncovered mines and enemies and broadcast their locations in BuildDirectives or
 * FightDirectives
 * @author bovard
 */
public class RSScout extends SensorRobotSystem {

  public RSScout(RobotController robotControl, SensorSystem sensorSys, BroadcastSystem broadcastSys) {
    super(robotControl, sensorSys);
  }


  public void go() {
    while(true) {
      //TODO: Implement this class
      System.out.println("WARNING: class not implemented");
      yield();
    }
  }
}
