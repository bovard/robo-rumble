
package team122;
import battlecode.common.*;

/**
 * Builds up the home base for global domination!
 * @author bovard
 */
public class RSBaseBuilder extends BuilderSensorRobotSystem {
  public RSBaseBuilder(RobotController robotControl, SensorSystem sensorSys, BuilderSystem buildSys) {
    super(robotControl, sensorSys, buildSys);
  }

  @Override
  public void go() {

    buildBaseScript();

    //go look for new mines
    new RSBuilderScout(robotControl, sensorSys, buildSys).go();

  }


  public void buildBaseScript() {
    turnToFaceRecycler();
    yield();
    Direction currentDir = robotControl.getDirection();
    boolean mineAtFrontRight = sensorSys.senseObjectAtLocation(birthPlace.add(currentDir.rotateRight()), RobotLevel.MINE) != null;

    //if the mine is to our right
    if(mineAtFrontRight) {
      System.out.println("detected mine right");
      seqBuild(BuildOrder.ARMORY, birthPlace.add(currentDir.rotateRight().rotateRight()));
    }
    //otherwise the mine is to our left
    else {
      System.out.println("detected mine left");
      while(robotControl.getTeamResources() < BuildOrder.ARMORY.cost + PlayerConstants.MINIMUM_FLUX) {
        yield();
      }
      seqBuild(BuildOrder.ARMORY, birthPlace.add(currentDir.rotateLeft().rotateLeft()));
    }

    Direction toBuild = currentDir;
    while(!navSys.canMove(toBuild)) {
      toBuild = toBuild.rotateRight();
    }
    while(robotControl.getTeamResources() < BuildOrder.FACTORY.cost + PlayerConstants.MINIMUM_FLUX) {
      yield();
    }

    seqBuild(BuildOrder.FACTORY, birthPlace.add(toBuild));

    //turn them on
    try {
      robotControl.turnOn(birthPlace.add(currentDir), RobotLevel.ON_GROUND);
      robotControl.turnOn(birthPlace.add(toBuild), RobotLevel.ON_GROUND);
      if(mineAtFrontRight) {
        robotControl.turnOn(birthPlace.add(currentDir.rotateRight().rotateRight()), RobotLevel.ON_GROUND);
      }
      else {
        robotControl.turnOn(birthPlace.add(currentDir.rotateLeft().rotateLeft()), RobotLevel.ON_GROUND);
      }

    } catch (Exception e) {
      System.out.println("caught exception:");
      e.printStackTrace();
    }

  }

  /**
   * Turns the robot to face N/S/E/W and a mine w/ a recycler on it
   */
  public void turnToFaceRecycler() {
    yield();
    try {
      while(sensorSys.senseObjectAtLocation(robotControl.getLocation().add(robotControl.getDirection()), RobotLevel.ON_GROUND) == null
              || !(robotControl.getDirection() == Direction.NORTH || robotControl.getDirection() == Direction.EAST
              || robotControl.getDirection() == Direction.SOUTH || robotControl.getDirection() == Direction.WEST)) {
        while(navSys.isActive()) {
          yield();
        }
        actTurn(robotControl.getDirection().rotateRight());
        while(navSys.isActive()) {
          yield();
        }
      }
      //System.out.println("Direction is "+robotControl.getDirection());
      //System.out.println("Sensed: "+sensorControl.senseObjectAtLocation(robotControl.getLocation().add(robotControl.getDirection()), RobotLevel.ON_GROUND));
    } catch (Exception e) {
      System.out.println("caught exception:");
      e.printStackTrace();
    }
  }
}
