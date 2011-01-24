package team122;

import battlecode.common.*;

/**
 * The first thing that will be loaded by a robot waking up,
 * This should hold the robot until a suitable RobotSystem is found for it
 *
 * Note: to build a submission file navigate to the base install file of BC and type
 * "ant jar -Dteam=team122"
 *
 * Note: for Linux users you'll need to turn off the sound for the client to work correctly
 * in the BattleCode main directory open bc.conf and set bc.client.sound-on=false
 * @author bovard
 */
public class RobotPlayer implements Runnable {

  private final RobotController myRC;
  private final int strategy = PlayerConstants.LIGHT_RUSH;

  /**
   * constructor
   * @param rc the robotController
   */
  public RobotPlayer(RobotController rc) {
    myRC = rc;
  }

  /**
   * Called after the constructor, if we return from this method the robot explodes
   * we can switch strategies by editing the strategy integer (find strategy options in PlayerConstants)
   */
  public void run() {
    if(strategy == PlayerConstants.LIGHT_RUSH) {
      lightRushStaging();
    }
    else if (strategy == PlayerConstants.HEAVY_RUSH) {
      heavyRushStaging();
    }
  }

  /**
   * lightRushStaging will house a robot until it has enough components to be a recognized system
   * after that it will pass the robotControl to the system and call the go() method
   * This depends heavily on the order in which the components of the robot are built (the
   * order in which their built is the order that they'll return in when you call the
   * component() function). Making sure there isn't any ambigouity is important.
   *
   * Note: requires version 1.07 or later to work correctly
   * Note: not very efficient at the moment, we'll have to clean it up later
   */
  public void lightRushStaging() {
    ComponentController [] components;
    //one the code enters here it waits until it recognizes one of the builds, then loads
    //the appropriate system
    myRC.setIndicatorString(0, "Staging...");
    while(true) {

      components = myRC.components();

      if (myRC.getChassis() == Chassis.LIGHT) {
        //our BUILDER_SCOUT
        if (components.length == 3 && components[1].type()==ComponentType.SIGHT &&
                components[2].type()==ComponentType.CONSTRUCTOR) {
          RSBuilderScout system = new RSBuilderScout(myRC, new SensorSystem(myRC, (SensorController)components[1]),
                  new BuilderSystem(myRC, (BuilderController)components[2]));
          system.go();
        }
        //provided BuilderScout (the one that we start off the game with)
        if (components.length == 3 && components[2].type()==ComponentType.SIGHT &&
                components[1].type()==ComponentType.CONSTRUCTOR) {
          RSBuilderScout system = new RSBuilderScout(myRC, new SensorSystem(myRC, (SensorController)components[2]),
                  new BuilderSystem(myRC, (BuilderController)components[1]));
          system.go();
        }
        //FIGHTER_SCOUT
        if (components.length == 4 && components[1].type()==ComponentType.RADAR &&
                components[2].type()==ComponentType.BLASTER && components[3].type()==ComponentType.SHIELD) {
          SensorSystem sensorSys = new SensorSystem(myRC, (SensorController)components[1]);
          RSFighterScout system = new RSFighterScout(myRC, sensorSys, new WeaponSystem(new WeaponController[] {(WeaponController)components[2]}, sensorSys));
          system.go();
        }
        //FIGHTER_SCOUT 2
        if (components.length == 5 && components[1].type()==ComponentType.RADAR &&
                components[2].type()==ComponentType.SMG && components[3].type()==ComponentType.SMG
                && components[4].type()==ComponentType.SHIELD) {
          WeaponController[] weapons = new WeaponController[2];
          weapons[0]=(WeaponController)components[2];
          weapons[1]=(WeaponController)components[3];
          SensorSystem sensorSys = new SensorSystem(myRC, (SensorController)components[1]);
          RSFighterScout system = new RSFighterScout(myRC, sensorSys, new WeaponSystem(weapons, sensorSys));
          system.go();
        }
        //FIGHTER_SCOUT 3
        if (components.length == 4 && components[1].type()==ComponentType.RADAR &&
                components[2].type()==ComponentType.BLASTER && components[3].type()==ComponentType.SMG) {
          WeaponController[] weapons = new WeaponController[2];
          weapons[0]=(WeaponController)components[2];
          weapons[1]=(WeaponController)components[3];
          SensorSystem sensorSys = new SensorSystem(myRC, (SensorController)components[1]);
          RSFighterScout system = new RSFighterScout(myRC, sensorSys, new WeaponSystem(weapons, sensorSys));
          system.go();
        }
        //FIGHTER_SCOUT 4
        if (components.length == 5 && components[1].type()==ComponentType.RADAR &&
                components[2].type()==ComponentType.SMG && components[3].type()==ComponentType.SMG
                && components[4].type()==ComponentType.SMG) {
          WeaponController[] weapons = new WeaponController[3];
          weapons[0]=(WeaponController)components[2];
          weapons[1]=(WeaponController)components[3];
          weapons[2]=(WeaponController)components[4];
          SensorSystem sensorSys = new SensorSystem(myRC, (SensorController)components[1]);
          RSFighterScout system = new RSFighterScout(myRC, sensorSys, new WeaponSystem(weapons, sensorSys));
          system.go();
        }
        //FIGHTER_SCOUT_5
        if (components.length == 5 && components[1].type()==ComponentType.SIGHT &&
                components[2].type()==ComponentType.BLASTER && components[3].type()==ComponentType.BLASTER
                && components[4].type()==ComponentType.SHIELD) {
          WeaponController[] weapons = new WeaponController[2];
          weapons[0]=(WeaponController)components[2];
          weapons[1]=(WeaponController)components[3];
          SensorSystem sensorSys = new SensorSystem(myRC, (SensorController)components[1]);
          RSFighterScout system = new RSFighterScout(myRC, sensorSys, new WeaponSystem(weapons, sensorSys));
          system.go();
        }
        }//FIGHTER_SCOUT_6
        if (components.length == 5 && components[1].type()==ComponentType.SIGHT &&
                components[2].type()==ComponentType.HAMMER && components[3].type()==ComponentType.HAMMER
                && components[4].type()==ComponentType.SHIELD) {
          WeaponController[] weapons = new WeaponController[2];
          weapons[0]=(WeaponController)components[2];
          weapons[1]=(WeaponController)components[3];
          SensorSystem sensorSys = new SensorSystem(myRC, (SensorController)components[1]);
          RSFighterScout system = new RSFighterScout(myRC, sensorSys, new WeaponSystem(weapons, sensorSys));
          system.go();
        }
      if (myRC.getChassis()==Chassis.BUILDING) {
        //RECYCLER
        if (components.length == 3 && components[1].type() == ComponentType.BUILDING_SENSOR &&
                components[2].type()==ComponentType.RECYCLER) {
          RSRecycler system = new RSRecycler(myRC, new SensorSystem(myRC, (SensorController)components[1]),
                  new BuilderSystem(myRC, (BuilderController)components[2]));
          system.go();
        }
        //TURRET (1,2,3, & 4) (don't know why it'd be more than 5, but better safe than sorry!)
        if (components.length >= 5 && components[1].type() == ComponentType.BUILDING_SENSOR &&
                components[2].type()==ComponentType.RADAR && components[3].type()==ComponentType.SHIELD) {
          WeaponController[] weapons = new WeaponController[components.length-4];
          
          //grab the weapon controllers
          for (int i=4; i<components.length;i++) {
            weapons[i-4] = (WeaponController)components[i];
          }
          SensorSystem sensorSys = new SensorSystem(myRC, (SensorController)components[2]);
          RSGuardTower system = new RSGuardTower(myRC, sensorSys,
                  new WeaponSystem(weapons, sensorSys));
          system.go();
        }
      }
      //we didn't find our system, yield for a turn and try again next time
      myRC.yield();
    }
  }

  /**
   * This is the staging area for robot systems taking part of the heavy rush strategy
   */
  public void heavyRushStaging() {
   
    ComponentController[] components = myRC.components();
    //provided BuilderScout (the one that we start off the game with)
    if (components.length == 3 && components[2].type()==ComponentType.SIGHT &&
            components[1].type()==ComponentType.CONSTRUCTOR) {
      RSBaseBuilder system = new RSBaseBuilder(myRC, new SensorSystem(myRC, (SensorController)components[2]),
              new BuilderSystem(myRC, (BuilderController)components[1]));
      system.go();
    }

    //turn off and wait to be activated
    myRC.turnOff();
  }
}