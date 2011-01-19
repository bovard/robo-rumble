
package team122;
import battlecode.common.*;
import java.util.Random;

/**
 * This is the System for a basic Recycler. It'll be needing to pump out two types of robots initially
 * BuilderScouts and FighterScouts (see RobotBuildOrder on how to build each, order matters)
 *
 *
 * @author bovard
 */
public class RecyclerRobotSystem extends BuildingRobotSystem {
  protected BuilderController buildControl;
  protected BuilderSystem buildSys;
  private Random rand;
  protected boolean shouldBuild = false;
  protected boolean stayActive = false;


  /**
   * Creates a new Recycler, which can build scout units
   * @param robotControl
   */
  public RecyclerRobotSystem(RobotController robotControl) {
    super(robotControl);

    robotControl.setIndicatorString(0, "Recycler");
    buildControl = (BuilderController)robotControl.components()[2];
    buildSys = new BuilderSystem(robotControl, buildControl);
    rand = new Random();

    //check to see if this Recycler is the most SouthWestern in a group of four mines it should produce
    //if it is set shouldBuild to true so it knows it should start building troops
    try {
      if(sensorControl.senseObjectAtLocation(birthPlace.add(Direction.NORTH), RobotLevel.MINE) != null
              && sensorControl.senseObjectAtLocation(birthPlace.add(Direction.NORTH_EAST), RobotLevel.MINE) != null
              && sensorControl.senseObjectAtLocation(birthPlace.add(Direction.EAST), RobotLevel.MINE) != null) {
        shouldBuild=true;
      }
      //otherwise if the mine is the most southwestern it should stay active
      else if (sensorControl.senseObjectAtLocation(birthPlace.add(Direction.SOUTH), RobotLevel.MINE) == null
              && sensorControl.senseObjectAtLocation(birthPlace.add(Direction.WEST), RobotLevel.MINE) == null) {
        stayActive = true;
      }
    }catch (Exception e) {
      System.out.println("caught exception:");
      e.printStackTrace();
    }
  }

  @Override
  public void go() {
    
    //Note: we'll still get income from the mines.
    while(shouldBuild) {
      if (shouldBuild && Clock.getRoundNum() > 150 && robotControl.getTeamResources() > RobotBuildOrder.RECYCLER_COST + PlayerConstants.MINIMUM_FLUX)
        selBuildScouts();
    }
    while(stayActive) {
      //wait until we want to start building gaurd towers then do it up!
      while(Clock.getRoundNum() < PlayerConstants.START_BUILDING_GUARD_TOWERS) {
        yield();
      }
      //wait for funds to build an antenna and our buildcontroller is free
      while(robotControl.getTeamResources() < ComponentType.ANTENNA.cost + PlayerConstants.MINIMUM_FLUX
              || buildControl.isActive()) {
        yield();
      }
      //once we've built an antenna, make a new ComRecyclerRobotSystem and start it
      if (buildSys.actBuildComponent(ComponentType.ANTENNA, birthPlace, RobotLevel.ON_GROUND)) {
        new ComRecyclerRobotSystem(robotControl).go();
      }
    }
    //If they aren't building they should turn off to save their upkeep
    robotControl.setIndicatorString(1, "Not Producing, turning off!");
    robotControl.turnOff();
  }

  /**
   * Builds FighterScouts and BuilderScouts
   * //TODO: pick a better method of deciding which one to build besides random,
   * ideally we should select between the two based on past build history and game turn
   * @return if one has been successfully made
   */
  protected boolean selBuildScouts() {
    robotControl.setIndicatorString(2, "selBuildScouts");
    double decider = rand.nextDouble();
    // the higher the decider the greater chance of producing Builders
    if (Clock.getRoundNum() < 700) {
      decider += .4;
    }
    else if (Clock.getRoundNum() > 1500){
      decider -= .4;
    }
    if(decider >= .5) {
      //wait until we have enough resources
      while(robotControl.getTeamResources() < MINIMUM_ENERGON + RobotBuildOrder.BUILDER_SCOUT_COST + RobotBuildOrder.RECYCLER_COST) {
        yield();
      }
      //build
      if(seqBuild(RobotBuildOrder.BUILDER_SCOUT)) {
        return true;
      }
      else {
        return false;
      }
    }
    else {
      return buildRandomSoldierScout();
    }
  }


  /**
   * Builds a random soldier scout
   * @return if teh build was sucessfull
   */
  protected boolean buildRandomSoldierScout() {
    Object[] buildOrder = RobotBuildOrder.FIGHTER_SCOUT;
    int buildCost = RobotBuildOrder.FIGHTER_SCOUT_COST;

    switch(rand.nextInt(5)+1) {
      case 1:
        //buildOrder = RobotBuildOrder.FIGHTER_SCOUT;
        //buildCost = RobotBuildOrder.FIGHTER_SCOUT_COST;
        break;
      case 2:
        buildOrder = RobotBuildOrder.FIGHTER_SCOUT_2;
        buildCost = RobotBuildOrder.FIGHTER_SCOUT_2_COST;
        break;
      case 3:
        buildOrder = RobotBuildOrder.FIGHTER_SCOUT_3;
        buildCost = RobotBuildOrder.FIGHTER_SCOUT_3_COST;
        break;
      case 4:
        buildOrder = RobotBuildOrder.FIGHTER_SCOUT_4;
        buildCost = RobotBuildOrder.FIGHTER_SCOUT_4_COST;
        break;
      case 5:
        buildOrder = RobotBuildOrder.FIGHTER_SCOUT_5;
        buildCost = RobotBuildOrder.FIGHTER_SCOUT_5_COST;
        break;
    }
    //wait until we have enough resources
    while(robotControl.getTeamResources() < MINIMUM_ENERGON + buildCost ) {
      yield();
    }
    //build
    if(seqBuild(buildOrder)) {
      return true;
    }
    else {
      return false;
    }
  }

  /**
   * Takes a RobotBuildOrder and builds a robot to match the specification
   * @param buildOrder an array of Object, the first being the chasis the rest being components
   * @return if the build was successful
   */
  protected boolean seqBuild(Object[] buildOrder) {
    actTurn(Direction.WEST);
    //keep rotating until we find a free square and have enough resources
    while(!moveControl.canMove(robotControl.getDirection()) ||
            robotControl.getTeamResources() < MINIMUM_ENERGON + ((Chassis)buildOrder[0]).cost) {
      actTurn(robotControl.getDirection().rotateRight());
    }
    //call to the buildSys to initiate the build sequence
    return buildSys.seqBuild(buildOrder,robotControl.getLocation().add(robotControl.getDirection()));

  }

}
