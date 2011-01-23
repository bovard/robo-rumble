
package team122;
import battlecode.common.*;

/**
 * For robots that just have to have to have it all, weapon(s), builder, sensor and movement
 * controllers
 *
 * Important: Now all fighters are inheriting from this class instead of FighterSensorRobotSystem
 * we'll just set the buildSystem to null if we aren't using it (see the second constructor)
 * 
 * @author bovard
 */
public class FighterBuilderSensorRobotSystem extends BuilderSensorRobotSystem {
  protected WeaponSystem weaponSys;

  public FighterBuilderSensorRobotSystem(RobotController robotControl, SensorSystem sensorSys,
          BuilderSystem buildSys, WeaponSystem weaponSys) {
    super(robotControl, sensorSys, buildSys);
    this.weaponSys = weaponSys;
    gameEvents = new FighterBuilderSensorGameEvents(robotControl, comSys, sensorSys);
  }

  public FighterBuilderSensorRobotSystem(RobotController robotControl, SensorSystem sensorSys,
          WeaponSystem weaponSys) {
    super(robotControl, sensorSys, null);
    this.weaponSys = weaponSys;
    gameEvents = new FighterBuilderSensorGameEvents(robotControl, comSys, sensorSys);
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
      currentGameEventLevel = GameEventLevel.COMBAT;
      seqEngageEnemy(sensorSys.getNearestOpponent());
    }
    return true;
  }

  /**
   * Roates and engages any enemies it sees
   * @return true
   */
  protected boolean seqRotateAndEngage() {
    robotControl.setIndicatorString(1, "seqRotateAndEngage");
    currentGameEventLevel = GameEventLevel.NORMAL;
    seqRotate();
    if(((SensorGameEvents)gameEvents).canSeeEnemy()) {
      currentGameEventLevel = GameEventLevel.COMBAT;
      seqStaticEngageEnemy(sensorSys.getNearestOpponent());
    }
    return true;
  }

  /**
   * Turns the robot and continues to look for the enemy
   * @return if the action was completed scuessfully (an enemy was't seen)
   */
  protected boolean seqRotate() {
    robotControl.setIndicatorString(1, "seqRotate");
    while(!gameEvents.checkGameEventsAbovePriority(currentGameEventLevel.priority))
    {
      setCheckNewWeapons();
      switch(sensorSys.getBreadth()) {
        case PlayerConstants.TELESCOPE_TURNS:
          weaponSys.setFireAtRandom();
          actTurn(robotControl.getDirection().rotateRight());
          break;
        case PlayerConstants.SIGHT_TURNS:
          weaponSys.setFireAtRandom();
          actTurn(robotControl.getDirection().rotateRight().rotateRight());
          break;
        case PlayerConstants.RADAR_TURNS:
          weaponSys.setFireAtRandom();
          actTurn(robotControl.getDirection().opposite());
          break;
        case PlayerConstants.SATELLITE_TURNS:
          weaponSys.setFireAtRandom();
          yield();
          //in this case the sensor can see in all directions so the bot doesn't need to rotate
          break;
      }
    }
    return !gameEvents.checkGameEventsAbovePriority(currentGameEventLevel.priority);
  }




  /**
   * While the enemy bot is in range, engage it
   * @param bot The enemy bot to engage
   * @return if we can still see the enemy bot
   */
  protected boolean seqEngageEnemy(Robot bot) {
    robotControl.setIndicatorString(1, "seqEngageEnemy");
    currentGameEventLevel = GameEventLevel.COMBAT;

    do {

      boolean canSee = sensorSys.canSenseObject(bot);
      MapLocation ourLoc = robotControl.getLocation();
      MapLocation enemyLoc = null;
      Direction toEnemy = null;

      if(canSee) {
        enemyLoc = sensorSys.senseLocationOfObject(bot);
        toEnemy = ourLoc.directionTo(enemyLoc);
      }


      //Set a movement action
      if(!navSys.isActive()) {
        //if you can't sense them move forward
        if (!canSee) {
          if(navSys.canMove(robotControl.getDirection())) {
            navSys.setMoveForward();
          }
        }
        //if not facing the enemy, turn to face them
        else if( toEnemy != robotControl.getDirection()) {
            navSys.setTurn(toEnemy);
        }
        //if we're too far away, close the distance
        else if (ourLoc.distanceSquaredTo(enemyLoc) > weaponSys.getMinRange()) {
          if(navSys.canMove(robotControl.getDirection())) {
            navSys.setMoveForward();
          }
        }
        //if we're too close back off a bit
        else if (ourLoc.distanceSquaredTo(enemyLoc) < weaponSys.getMinRange()) {
          if(navSys.canMove(robotControl.getDirection().opposite())) {
            navSys.setMoveBackward();
          }
        }
      }
      //Now set our weapons to fire
      //try to fire at the robot
      if(!weaponSys.allActive() && canSee) {
        weaponSys.setFireAtLocation(enemyLoc, bot.getRobotLevel());
      }
      //if you can't fire all weapons at the enemy, just try to fire at any enemy robot
      if(!weaponSys.allActive()) {
        //Need to rescan because if we killed anything it won't show up on the new scan
        sensorSys.reScanForBots();
        weaponSys.setFireAtRandom();
      }

      //finally yield
      yield();
    } while(sensorSys.canSenseObject(bot) || navSys.isActive());

    return !sensorSys.canSenseObject(bot);
  }

  /**
   * While the enemy bot is in range, engage it (but don't move besides turning)
   * @param bot The enemy bot to engage
   * @return if we can still see the enemy bot
   */
  protected boolean seqStaticEngageEnemy(Robot bot) {
    robotControl.setIndicatorString(1, "seqStaticEngageEnemy");

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
      }

      //Now set our weapons to fire
      //try to fire at the robot
      if(!weaponSys.allActive()) {
        weaponSys.setFireAtLocation(enemyLoc, bot.getRobotLevel());
      }
      //if you can't fire all weapons at the enemy, just try to fire at any enemy robot
      if(!weaponSys.allActive()) {
        //Need to rescan because if we killed anything it won't show up on the new scan
        sensorSys.reScanForBots();
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
    if((((SensorGameEvents)gameEvents).canSeeDebris() || ((SensorGameEvents)gameEvents).canSeeEnemy())
            && !weaponSys.allActive()) {
      sensorSys.reScanForBots();
      weaponSys.setFireAtRandom();
    }
    return super.actMove();
  }

    /**
   * Overrides actTurn() to allow for weapon capabilities
   * @return if it was performed correctly
   */
  @Override
  protected boolean actTurn(Direction dir) {
    if((((SensorGameEvents)gameEvents).canSeeDebris() || ((SensorGameEvents)gameEvents).canSeeEnemy())
            && !weaponSys.allActive()) {
      sensorSys.reScanForBots();
      weaponSys.setFireAtRandom();
    }
    return super.actTurn(dir);
  }

  /**
   * Checks for new weapons and adds them to WeaponSys if it finds any
   * @return if completed successfully
   */
  protected boolean setCheckNewWeapons() {
    //grab any new components
    ComponentController[] components = robotControl.newComponents();
    //if there are some
    if (components.length>0) {
      //if they are the right type (Weapon of some sort)
      for (int i = 0; i<components.length;i++) {
        if (components[i].type() == ComponentType.SMG
                || components[i].type() == ComponentType.BLASTER
                || components[i].type() == ComponentType.RAILGUN
                || components[i].type() == ComponentType.MEDIC
                || components[i].type() == ComponentType.BEAM
                || components[i].type() == ComponentType.HAMMER ) {
          //add them to the weaponSystem
          weaponSys.addWeapon((WeaponController)components[i]);
        }
      }
    }
    return true;
  }


  /**
   * Called if the robot comes under fire, it should turn around, looking for the enemy
   * @return if an enemy is seen
   */
  protected boolean seqRotateToUnSeenEnemy() {
    robotControl.setIndicatorString(1, "seqRotateToEnemy");
    while(!((SensorGameEvents)gameEvents).canSeeEnemy()
            && gameEvents.checkGameEventsAbovePriority(GameEventLevel.DIRECTIVE.priority)) {
      while(navSys.isActive()) {
        yield();
      }
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

          //move forward twice
          for (int k = 0; k < 2; k++) {
            while(navSys.isActive()) {
              yield();
            }
            if(navSys.canMove(robotControl.getDirection())) {
              actMoveForward();
            }
            if(((SensorGameEvents)gameEvents).canSeeEnemy()) {
              return true;
            }
          }

          //turn right
          while(navSys.isActive()) {
            yield();
          }
          actTurn(robotControl.getDirection().rotateRight().rotateRight());

          break;
        case PlayerConstants.RADAR_TURNS:
          for (int i=0; i<PlayerConstants.RADAR_TURNS; i++) {
            if(!((SensorGameEvents)gameEvents).canSeeEnemy()) {
              actTurn(robotControl.getDirection().opposite());
            }
          }
          break;
        case PlayerConstants.SATELLITE_TURNS:
          yield();
          //in this case the sensor can see in all directions so the bot doesn't need to rotate
          break;
      }
    }
    return ((SensorGameEvents)gameEvents).canSeeEnemy();
  }


}
