
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


  /**
   * Creates a new Recycler, which can build scout units
   * @param robotControl
   */
  public RecyclerRobotSystem(RobotController robotControl) {
    super(robotControl);

    robotControl.setIndicatorString(0, "Recycler");
    buildControl = (BuilderController)robotControl.components()[2];
    buildSys = new BuilderSystem(robotControl, buildControl, moveControl);
    rand = new Random();

    //check to see if this Recycler is the most NorthWestern at the start of the game
    //if it is set shouldBuild to true so it knows it should start building troops
    try {
      if(Clock.getRoundNum() < 10
              && ( sensorControl.senseObjectAtLocation(birthPlace.add(Direction.NORTH), RobotLevel.MINE) == null
              || sensorControl.senseObjectAtLocation(birthPlace.add(Direction.WEST), RobotLevel.MINE) == null)) {
        shouldBuild=true;
      }
    }catch (Exception e) {
      System.out.println("caught exception:");
      e.printStackTrace();
    }
  }

  public void go() {
    //If they aren't building they should turn off to save their upkeep
    //Note: we'll still get income from the mines.
    if (!shouldBuild) {
      robotControl.turnOff();
    }
    while(true) {
      if (shouldBuild && Clock.getRoundNum() > 150 && robotControl.getTeamResources() > RobotBuildOrder.BUILDER_SCOUT_COST*3)
        selBuildScouts();
      for (int i=0; i<5; i++)
        yield();
    }
  }

  /**
   * Builds FighterScouts and BuilderScouts
   * //TODO: pick a better method of deciding which one to build besides random,
   * ideally we should select between the two based on past build history and game turn
   * @return if one has been successfully made
   */
  protected boolean selBuildScouts() {
    robotControl.setIndicatorString(2, "selBuildScouts");
    if(rand.nextBoolean()) {
      //wait until we have enough resources
      while(robotControl.getTeamResources() < MINIMUM + RobotBuildOrder.BUILDER_SCOUT_COST ) {
        yield();
      }
      //build
      if(seqBuild(RobotBuildOrder.BUILDER_SCOUT))
        return true;
    }
    else {
      //wait until we have enough resources
      while(robotControl.getTeamResources() < MINIMUM + RobotBuildOrder.FIGHTER_SCOUT_COST ) {
        yield();
      }
      //build
      if(seqBuild(RobotBuildOrder.FIGHTER_SCOUT))
        return true;
    }
    return false;
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
            robotControl.getTeamResources() < MINIMUM + ((Chassis)buildOrder[0]).cost) {
      actTurn(robotControl.getDirection().rotateRight());
    }
    //call to the buildSys to initiate the build sequence
    return buildSys.seqBuild(buildOrder,robotControl.getLocation().add(robotControl.getDirection()));

  }

}
