
package team122;
import battlecode.common.*;

/**
 * Implements a ComRecyclerRobotSystem, these are recyclers with some for of communication
 * (placed in the components[3]). They will attempt to build and maintain a turret 4 at an
 * adjacent location.
 * @deprecated from alpha v2.0
 * @author bovard
 */
public class ComRecyclerRobotSystem extends RecyclerRobotSystem {
  protected BroadcastController broadcastControl;
  protected BroadcastSystem broadcastSys;
  protected GameObject guardTower;

  public ComRecyclerRobotSystem(RobotController robotControl, SensorSystem sensorSys,
          BuilderSystem buildSys, BroadcastSystem broadcastSys) {
    super(robotControl, sensorSys, buildSys);
    robotControl.setIndicatorString(0, "ComRecycler");
    this.broadcastSys = broadcastSys;
  }

  @Override
  public void go() {
    while(true) {
      seqBuildGaurd();
    }
  }

  /**
   * builds a gaurd tower, then checks to make sure it's till there
   * @return if it was completed succesfully
   */
  protected boolean seqBuildGaurd() {
    robotControl.setIndicatorString(1, "selBuildGuard");
    //if we have a guardtower and we can sense it nearby OR if we can build a guard tower
    if((guardTower != null && sensorSys.canSenseObject(guardTower)) || seqBuildGaurdTower()) {
      //monitor that guard tower
      return seqMonitorGuardTower();
    }
    return false;
  }

  /**
   * Tries to build a guardtower (TURRET_4) on an adjacent square
   * @return if the tower was built sucessfully
   */
  protected boolean seqBuildGaurdTower() {
    robotControl.setIndicatorString(1, "seqBuildGuardTower");
    //turn to an open space
    try {
      //while the square is occupied or there is a mine in the square
      while(!navSys.canMove(robotControl.getDirection())
              || sensorSys.senseObjectAtLocation(birthPlace.add(robotControl.getDirection()), RobotLevel.MINE) != null) {
        //rotate
        actTurn(robotControl.getDirection().rotateRight());
      }

      boolean done = false;
      while (!done) {
        //While there is nothing there, broadcast a building directive and sleep for a while
        do {
          guardTower = sensorSys.senseObjectAtLocation(birthPlace.add(robotControl.getDirection()), RobotLevel.ON_GROUND);
          broadcastSys.sendBuildDirective(BuildOrder.GUARD_TOWER_1.id, birthPlace.add(robotControl.getDirection()));
          for (int i=0; i<5; i++) {
            yield();
          }
        } while(guardTower == null);
        
        //something is there, wait for up to turnsToWait turns, see that it doesn't leave
        int i = 0;
        int turnsToWait = 10;
        while( i < turnsToWait && !navSys.canMove(robotControl.getDirection())) {
          i++;
          yield();
        }
        //if we've gone through turnsToWait turns... it check to see if it's the same thing
        if (i==turnsToWait) {
          //check to see if the same thing is still there
          int id = sensorSys.senseObjectAtLocation(birthPlace.add(robotControl.getDirection()), RobotLevel.ON_GROUND).getID();
          if (id == guardTower.getID()) {
            //if it is, chances are it's the building base we've been looking for
            done = true;
          }
        }

      }

      guardTower = sensorSys.senseObjectAtLocation(birthPlace.add(robotControl.getDirection()), RobotLevel.ON_GROUND);

      while(robotControl.getTeamResources() < BuildOrder.GUARD_TOWER_1.cost) {
        yield();
      }

      return seqBuild(BuildOrder.GUARD_TOWER_1, birthPlace.add(robotControl.getDirection()));
    } catch (Exception e) {
      System.out.println("caught exception:");
      e.printStackTrace();
      return false;
    }
  }

  /**
   * monitor the tower
   * @return false if the tower has been destroyed
   */
  protected boolean seqMonitorGuardTower() {
    robotControl.setIndicatorString(1, "seqMonitorGuardTower");
    while(sensorSys.canSenseObject(guardTower)) {
      for (int i =0; i < 10; i++) {
        yield();
      }
    }
    return false;
  }

}
