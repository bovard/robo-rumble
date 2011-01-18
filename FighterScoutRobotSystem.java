

package team122;
import battlecode.common.*;

/**
 * this controls our scout fighters, they have the build lightmotor, radar, blaster, shield
 * @author bovard
 */
public class FighterScoutRobotSystem extends SensorRobotSystem {
  protected WeaponSystem weaponSys;


  /**
   * Makes a FighterScoutRobot, currently only looks for 1 weapon (slot 2)
   * @param robotControl The RobotController
   */
  public FighterScoutRobotSystem(RobotController robotControl) {
    super(robotControl);
    robotControl.setIndicatorString(0,"FighterScoutConstructor");
    WeaponController weaponControl = (WeaponController)robotControl.components()[2];
    WeaponController[] weapons = new WeaponController[1];
    weapons[0] = weaponControl;
    weaponSys = new WeaponSystem(weapons, sensorSys, sensorGameEvents);

  }

  /**
   * Makes a FighterScoutRobot, given the weapons in a WeaponsController array
   * @param robotControl The RobotController
   */
  public FighterScoutRobotSystem(RobotController robotControl, WeaponController[] weapons) {
    super(robotControl);
    robotControl.setIndicatorString(0,"FighterScoutConstructor");
    weaponSys = new WeaponSystem(weapons, sensorSys, sensorGameEvents);

  }

  /**
   * the yield method overridden for soldiers, doesn't check for the seeMine game event
   * to save bytecode
   */
  @Override
  protected void yield() {
    sensorGameEvents.resetGameEvents();
    robotControl.yield();
    sensorGameEvents.calcSoldierGameEvents();
    robotControl.setIndicatorString(0, robotControl.getLocation().toString());
  }

  /**
   * the main loop for a FigtherScout
   */
  @Override
  public void go() {
    //TODO: Implement this!
    robotControl.setIndicatorString(0, "FighterScout");
    while(true) {
      selFightNScout();
      yield();
    }
  }

  /**
   * the basic selector for the fighter class. decides when to patrol and when to engage
   * @return if one action was performed sucessfully
   */
  protected boolean selFightNScout() {
    //TODO: when a fighter sees an enemy or is shot he should engage with the enemy
    robotControl.setIndicatorString(1, "selFightNScout");
    if(seqScoutEnemy()) {
      return seqEngageEnemy();
    }
    return false;
  }

  /**
   * Finds the enemy and engages until the enemy is destroyed (or moves away)
   * //TODO: redo this to have to robot keep track of the enemy instead of the current implementation/hack
   * @return if there is still an enemy
   */
  protected boolean seqEngageEnemy() {
    robotControl.setIndicatorString(1, "seqEngageEnemy");
    //if we can see the enemy or we can rotate to see them
    if(sensorGameEvents.canSeeEnemy() || seqRotateToEnemy()) {
      //while we can see the enemy, fire at them or move toward them
      try {
        while(sensorGameEvents.canSeeEnemy()) {
          MapLocation toFire = weaponSys.fire();
          if(toFire != null) {
            robotControl.setIndicatorString(1, "seqEngageEnemy - turnAndFire!");
            actTurn(robotControl.getLocation().directionTo(toFire));
          }
          else {
            if (weaponSys.allActive())
            {
              robotControl.setIndicatorString(1, "seqEngageEnemy - wait");
              yield();
            }
            else {
              robotControl.setIndicatorString(1, "seqEngageEnemy - closeTheGap");
              if (sensorSys.getNearestOpponent() != null) {
                //find the nearest opponent
                MapLocation nearestOpponent = sensorSys.getSensor().senseLocationOf(sensorSys.getNearestOpponent());
                //move toward it
                if(nearestOpponent!=null) {
                  //if not facing the opponent, turn toward them
                  if(robotControl.getDirection() != robotControl.getLocation().directionTo(nearestOpponent)) {
                    actTurn(robotControl.getLocation().directionTo(nearestOpponent));
                  }
                  //otherwise move forward if you can
                  else {
                    if(moveControl.canMove(robotControl.getDirection()) && !moveControl.isActive()) {
                      moveControl.moveForward();
                      yield();
                    }
                    //if you can't move forward just sit there like a slub
                    else {
                      yield();
                    }
                  }
                }
                else {
                  yield();
                  return false;
                }
              }
              else {
                yield();
                return false;
              }
            }
          }
        }
      } catch (Exception e) {
        System.out.println("caught exception:");
        e.printStackTrace();
        return false;
      }
    }
    return false;
  }

  /**
   * Turns the robot and continues to look for the enemy
   * //TODO: replace these hard coded values with constants (shared with the SensorSystem.java ones)
   * @return if an enemy is seens
   */
  protected boolean seqRotateToEnemy() {
    robotControl.setIndicatorString(1, "seqRotateToEnemy");
    switch(sensorSys.getBreadth()) {
      case 8:
        for (int i=0; i<8; i++) {
          if(!sensorGameEvents.seeEnemy) {
            actTurn(robotControl.getDirection().rotateRight());
          }
        }
        break;
      case 4:
        for (int i=0; i<4; i++) {
          if(!sensorGameEvents.seeEnemy) {
            actTurn(robotControl.getDirection().rotateRight().rotateRight());
          }
        }
        break;
      case 2:
        for (int i=0; i<2; i++) {
          if(!sensorGameEvents.seeEnemy) {
            actTurn(robotControl.getDirection().opposite());
          }
        }
        break;
      case 1:
        //in this case the sensor can see in all directions so the bot doesn't need to rotate
        break;
    }
    return sensorGameEvents.seeEnemy;
  }

   /**
   * Called to move once (and yield) Assumes the robot already has a destination
   * Overridded to provide for weapons capabilities
   * @return if robot is in its current destination
   */
  @Override
  protected boolean actMove() {
    if(navSys.getDestination()==null)
      return false;
    boolean done = navSys.nextMove();
    if(sensorGameEvents.canSeeDebris() || sensorGameEvents.canSeeEnemy()) {
      weaponSys.fire();
    }
    yield();
    //check for map boundary conditions
    updateMapExtrema();
    //update your position for the GUI
    robotControl.setIndicatorString(0, robotControl.getLocation().toString());
    done = !checkDestinationValidity() || done;
    return done;
  }

}
