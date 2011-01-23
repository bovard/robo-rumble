
package team122;
import battlecode.common.*;

/**
 * A kickass recycler that can defend itself pretty well
 * @author bovard
 */
public class RSComCycler extends FighterBuilderSensorRobotSystem {
  protected BroadcastController broadcastControl;
  protected BroadcastSystem broadcastSys;
  protected WeaponSystem weaponSys;
  protected GameObject guardTower;
  protected MapLocation guardTowerLoc;

  public RSComCycler(RobotController robotControl, SensorSystem sensorSys,
          BuilderSystem buildSys, BroadcastSystem broadcastSys, WeaponSystem weaponSys) {
    super(robotControl, sensorSys, buildSys, weaponSys);
    robotControl.setIndicatorString(0, "ComCycler");
    this.broadcastSys = broadcastSys;
  }

  @Override
  public void go() {
    while(true) {
      seqRotateAndEngage();
      if (Clock.getRoundNum() > PlayerConstants.START_BUILDING_GUARD_TOWERS)
      {
        currentGameEventLevel = GameEventLevel.MISSION;
        if(!seqMonitorGuardTower()) {
          seqBuildGaurd();
        }
      }
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
          guardTowerLoc = birthPlace.add(robotControl.getDirection());
          guardTower = sensorSys.senseObjectAtLocation(guardTowerLoc, RobotLevel.ON_GROUND);
          broadcastSys.sendBuildDirective(BuildOrder.GUARD_TOWER_1.id, guardTowerLoc);
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
    if(guardTower != null) {
      while(navSys.isActive()) {
        yield();
      }
      actTurn(robotControl.getLocation().directionTo(guardTowerLoc));
      if(sensorSys.canSenseObject(guardTower)) {
        return true;
      }
      else {
        return false;
      }
    }
    return false;
  }

}
