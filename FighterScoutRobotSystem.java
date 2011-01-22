

package team122;
import battlecode.common.*;

/**
 * this controls our scout fighters, they have the build lightmotor, sensor, weapons
 * @deprecated from alpha 2.0
 * @author bovard
 */
public class FighterScoutRobotSystem extends SensorRobotSystem {
  protected WeaponSystem weaponSys;


  /**
   * Makes a FighterScoutRobot, currently only looks for 1 weapon (slot 2)
   * @param robotControl The RobotController
   */
  public FighterScoutRobotSystem(RobotController robotControl, SensorSystem sensorSys) {
    super(robotControl, sensorSys);
    robotControl.setIndicatorString(0,"FighterScoutConstructor");
    WeaponController weaponControl = (WeaponController)robotControl.components()[2];
    WeaponController[] weapons = new WeaponController[1];
    weapons[0] = weaponControl;
    weaponSys = new WeaponSystem(weapons, sensorSys);

  }

  /**
   * Makes a FighterScoutRobot, given the weapons in a WeaponsController array
   * @param robotControl The RobotController
   */
  public FighterScoutRobotSystem(RobotController robotControl, SensorSystem sensorSys, WeaponController[] weapons) {
    super(robotControl, sensorSys);
    robotControl.setIndicatorString(0,"FighterScoutConstructor");
    weaponSys = new WeaponSystem(weapons, sensorSys);

  }


  /**
   * the main loop for a FigtherScout
   */
  @Override
  public void go() {
    robotControl.setIndicatorString(0, "FighterScout");
    while(true) {
      se1FightNScout();
      yield();
    }
  }

  /**
   * the basic seq for the fighter class. patrols and engages
   * @return if an enemy was fought
   */
  protected boolean se1FightNScout() {
    //TODO: when a fighter sees an enemy or is shot he should engage with the enemy
    robotControl.setIndicatorString(1, "selFightNScout");
    if(seqScout()) {
      //TODO: This is broken!
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
    if(((SensorGameEvents)gameEvents).canSeeEnemy() || seqRotateToEnemy()) {
      //while we can see the enemy, setFireAtRandom at them or move toward them
      try {
        while(((SensorGameEvents)gameEvents).canSeeEnemy()) {
          MapLocation toFire = weaponSys.setFireAtRandom();
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
                    if(navSys.canMove(robotControl.getDirection()) && !navSys.isActive()) {
                      navSys.setMoveForward();
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
   * @return if an enemy is seens
   */
  protected boolean seqRotateToEnemy() {
    robotControl.setIndicatorString(1, "seqRotateToEnemy");
    switch(sensorSys.getBreadth()) {
      case PlayerConstants.TELESCOPE_TURNS:
        for (int i=0; i<PlayerConstants.TELESCOPE_TURNS; i++) {
          if(!((SensorGameEvents)gameEvents).canSeeEnemy()) {
            actTurn(robotControl.getDirection().rotateRight());
          }
        }
        break;
      /**
       * a robot with a sight sensor is the only type of sensor that can't see the enemy
       * after turning 360 degrees, so we'll have to make it move a bit as well
       * Currently it's moving in a spiral outwards. This should help it 'run away' if it
       * isn't pointed in the right direction
       * //TODO: tweak this to make it better...
       * it works some of the time now but it's not ideal. Better than standing there though
       */
      case PlayerConstants.SIGHT_TURNS:
        robotControl.setIndicatorString(1, "seqRotateToEnemy - sightTurns");
        int j = 1;
        while( gameEvents.recentlyLostHealth) {
          actTurn(robotControl.getDirection().rotateRight().rotateRight());
          if(((SensorGameEvents)gameEvents).canSeeEnemy()) {
            return true;
          }
          for (int k = 0; k < j; k++) {
            actMoveForward();
            if(((SensorGameEvents)gameEvents).canSeeEnemy()) {
              return true;
            }
            while(navSys.isActive()) {
              yield();
            }
          }
          j++;
        }
        break;
      case PlayerConstants.RADAR_TURNS:
        for (int i=0; i<PlayerConstants.RADAR_TURNS; i++) {
          if(!((SensorGameEvents)gameEvents).canSeeEnemy()) {
            actTurn(robotControl.getDirection().opposite());
          }
        }
        break;
      case PlayerConstants.SATELLITE_TURNS:
        //in this case the sensor can see in all directions so the bot doesn't need to rotate
        break;
    }
    return ((SensorGameEvents)gameEvents).canSeeEnemy();
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
    boolean done = navSys.isAtDestination();
    if (!done) {
      navSys.setNextMove();
    }
    if(((SensorGameEvents)gameEvents).canSeeDebris() || ((SensorGameEvents)gameEvents).canSeeEnemy()) {
      weaponSys.setFireAtRandom();
    }
    yield();
    //check for map boundary conditions
    updateMapExtrema();
    done = !checkDestinationValidity() || done;
    return done;
  }

}
