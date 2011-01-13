package team122;

import battlecode.common.*;
import static battlecode.common.GameConstants.*;

public class RobotPlayer implements Runnable {

  private final RobotController myRC;

  public RobotPlayer(RobotController rc) {
    System.out.println("System Constant: "+GameConstants.SHIELD_MIN_DAMAGE);
    myRC = rc;
  }

  public void run() {
    //this wait may be necessary? all part of the dirty hack
    for (int i=0; i<6; i++)
      myRC.yield();
    ComponentController [] components = myRC.newComponents();
    System.out.println(java.util.Arrays.toString(components));
    System.out.flush();
    if(myRC.getChassis()==Chassis.BUILDING)
      runBuilder((MovementController)components[0],(BuilderController)components[2]);
    else
      runMotor((MovementController)components[0]);
  }

  /**
   * staging will house a robot until it has enough components to be a recognized system
   * after that it will pass the robotControl to the system and call the go() method
   */
  public void staging() {
    ComponentController [] components;
    //one the code enters here it waits until it recognizes one of the builds, then loads
    //the appropriate system
    while(true) {
      components = myRC.components();

      if (myRC.getChassis() == Chassis.LIGHT) {
        //our BUILDER_SCOUT
        if (components.length == 3 && components[1].type()==ComponentType.SIGHT &&
                components[2].type()==ComponentType.CONSTRUCTOR) {
          BuilderScoutRobotSystem system = new BuilderScoutRobotSystem(myRC);
          system.go();
        }
        //provided BuilderScout
        else if (components.length == 3 && components[2].type()==ComponentType.SIGHT &&
                components[1].type()==ComponentType.CONSTRUCTOR) {
          BuilderScoutRobotSystem system = new BuilderScoutRobotSystem(myRC);
          system.go();
        }
        //FIGHTER_SCOUT
        else if (components.length == 4 && components[1].type()==ComponentType.RADAR &&
                components[1].type()==ComponentType.SMG && components[2].type()==ComponentType.SHIELD) {
          FighterScoutRobotSystem system = new FighterScoutRobotSystem(myRC);
          system.go();
        }
      }
      else if (myRC.getChassis()==Chassis.BUILDING) {
        //RECYCLER
        if (components.length == 3 && components[1].type() == ComponentType.BUILDING_SENSOR &&
                components[2].type()==ComponentType.RECYCLER) {
          RecyclerRobotSystem system = new RecyclerRobotSystem(myRC);
          system.go();
        }
      }
      //we didn't find our system, yield for a turn and try again next time
      myRC.yield();
    }
  }

  //this is a dirty hack to make light chassis bots with 3 SMGs and a radar
  public void runBuilder(MovementController motor, BuilderController builder) {

    while (true) {
      try {
        //yield at the top of every loop
        myRC.yield();
        myRC.yield();
        //if motor can't move in the given direction, rotate right
        if(!motor.canMove(myRC.getDirection()))
          motor.setDirection(myRC.getDirection().rotateRight());
        //otherwise build a new light chassis in the direction you're pointing
        else if(myRC.getTeamResources()>= Chassis.LIGHT.cost + ComponentType.RADAR.cost + 3 * ComponentType.SMG.cost + 1) {
          System.out.println("Building a light chassis");
          builder.build(Chassis.LIGHT,myRC.getLocation().add(myRC.getDirection()));
          myRC.yield();
          while(builder.isActive() && myRC.getTeamResources()<ComponentType.RADAR.cost)
            System.out.println("Busy!");
            myRC.yield();
          System.out.println("Building a radar component");
          builder.build(ComponentType.RADAR, myRC.getLocation().add(myRC.getDirection()), RobotLevel.ON_GROUND);
          myRC.yield();
          for (int i = 0 ; i<3; i++) {
            while(builder.isActive() && myRC.getTeamResources()<ComponentType.SMG.cost)
              myRC.yield();
            System.out.println("Building an SMG component");
            builder.build(ComponentType.SMG, myRC.getLocation().add(myRC.getDirection()), RobotLevel.ON_GROUND);
            myRC.yield();
          }
          while(builder.isActive())
            myRC.yield();
          for (int i = 0 ; i<50 ; i++)
            myRC.yield();
        }


      } catch (Exception e) {
        System.out.println("caught exception:");
        e.printStackTrace();
      }
    }
  }

  //This is a dirty hack for testing, looks for a light chassis with 1+ SMGs and a radar
  public void runMotor(MovementController motor) {
    SensorSystem sensorSys = null;
    WeaponSystem weaponSys = null;
    NavigationSystem nav = new NavigationSystem(motor);
    
    //try to see if we can pull out a radar from the components and get a sensorSys running on it
    //also count # weapons
    int weaponCount = 0;
    for (int i=0;i<myRC.components().length;i++) {
      if (myRC.components()[i].type() == ComponentType.RADAR)
        sensorSys = new SensorSystem((SensorController)myRC.components()[i]);
      else if(myRC.components()[i].type() == ComponentType.SMG)
        weaponCount++;
    }

    //if we have SMGs and a radar, make a weaponSys to fire them!
    if (weaponCount > 0 && sensorSys != null) {
      WeaponController[] weapons = new WeaponController[weaponCount];
      weaponCount = 0;
      for (int i=0;i<myRC.components().length;i++) {
        if (myRC.components()[i].type() == ComponentType.SMG) {
          weapons[weaponCount] = (WeaponController)myRC.components()[i];
          weaponCount++;
        }
      }
      weaponSys = new WeaponSystem(weapons, sensorSys);
    }

    //all new bots will move to this new location... choose it well? if it's off the map it doesn't matter
    //chain use North +30 West +15 FTW
    //quadrents use North +52 West +52
    //hooks use South +30 West +1
    MapLocation dest = myRC.getLocation().add(Direction.SOUTH, 30).add(Direction.WEST, 1);
    nav.setDestination(dest);
    myRC.yield();
    int i = 0;
    while (true) {
      try {
        while(!nav.nextMove()) {
          if(weaponSys!=null)
            weaponSys.fire();
          myRC.yield();
        }
      }
      catch (Exception e) {
        System.out.println("caught exception:");
        e.printStackTrace();
      }
    }
  }
}
