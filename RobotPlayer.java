package team122;

import battlecode.common.*;

public class RobotPlayer implements Runnable {

  private final RobotController myRC;

  public RobotPlayer(RobotController rc) {
    myRC = rc;
  }

  public void run() {
    System.out.println(java.util.Arrays.toString(myRC.components()));
    System.out.flush();
    staging();
  }

  /**
   * staging will house a robot until it has enough components to be a recognized system
   * after that it will pass the robotControl to the system and call the go() method
   *
   * Note: requires version 1.07 or later to work correctly
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
        //provided BuilderScout (the one that we start off the game with)
        else if (components.length == 3 && components[2].type()==ComponentType.SIGHT &&
                components[1].type()==ComponentType.CONSTRUCTOR) {
          BuilderScoutRobotSystem system = new BuilderScoutRobotSystem(myRC);
          system.go();
        }
        //FIGHTER_SCOUT
        else if (components.length == 4 && components[1].type()==ComponentType.RADAR &&
                components[1].type()==ComponentType.BLASTER && components[2].type()==ComponentType.SHIELD) {
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
}