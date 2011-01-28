
package team122;
import battlecode.common.*;

/**
 * Builds up the home base for global domination! (this should be loaded onto the builder
 * scout that is given to us when the game boots
 * @author bovard
 */
public class RSBaseBuilder extends RSBuilderScout {
  public RSBaseBuilder(RobotController robotControl, SensorSystem sensorSys, BuilderSystem buildSys) {
    super(robotControl, sensorSys, buildSys);
  }

  @Override
  public void go() {

    buildFourMineScript();

    moveToClearing();

    chooseDirection();

    navSys.setMoveBackward();
    while(navSys.isActive()) {
      yield();
    }

    MapLocation toBuild = robotControl.getLocation().add(robotControl.getDirection());
    while(robotControl.getTeamResources() < BuildOrder.RECYCLER.cost + PlayerConstants.MINIMUM_FLUX) {
      yield();
    }
    seqBuild(BuildOrder.RECYCLER, toBuild);

    try {
      robotControl.turnOn(toBuild, RobotLevel.ON_GROUND);
    } catch (Exception e) {
      System.out.println("caught exception:");
      e.printStackTrace();
    }

    //go look for new mines
    super.go();

  }

  public void chooseDirection() {
    Direction dirToEnemy = Direction.NORTH;
    //we found teh south edge, so the enemy is north
    if(maxY < Integer.MAX_VALUE) {
      dirToEnemy = Direction.NORTH;
    }
    //we found the north edge, so the enemy is south
    else if(minY > -1) {
      dirToEnemy = Direction.SOUTH;
    }
    //we foudn teh west edge, so the enemy is east
    else if (minX > -1) {
      dirToEnemy = Direction.EAST;
    }
    //we found the east edge, so the enemy is west
    else if (maxX < Integer.MAX_VALUE) {
      dirToEnemy = Direction.WEST;
    }

    navSys.setTurn(dirToEnemy);
    while(navSys.isActive()) {
      yield();
    }
  }

  public void moveToClearing() {
    boolean done = false;
    while(!done) {
      changeScoutDirection();
      navSys.setDestination(chooseNextDestination());
      while(!done && actMove()) {
        while(navSys.isActive()) {
          yield();
        }
        boolean canMove = true;
        Direction toTest = robotControl.getDirection();
        for (int i = 0; i < 8; i++) {
          toTest = toTest.rotateLeft();
          canMove = canMove && navSys.canMove(toTest);
        }
        done = canMove;
      }
    }

  }


  public void buildFourMineScript() {
    currentGameEventLevel = GameEventLevel.CRITICAL;
    yield();
    changeScoutDirection();

    //turn to face nearest recycler
    turnToFaceRecycler();
    yield();


    //go to the mine on the other side
    MapLocation firstMine = birthPlace.add(robotControl.getDirection(), 2);
    navSys.setDestination(firstMine);
    while(!navSys.isAtDestination()) {
      while(navSys.isActive()) {
        yield();
      }
      actMove();
    }
    while(navSys.isActive()) {
      yield();
    }
    
    //find the mine next to you
    MapLocation otherMine = null;
    
    int numFreeSquares = 0;
    for(int i=0; i< 8; i++) {
      navSys.setTurn(robotControl.getDirection().rotateRight());
      while(navSys.isActive()) {
        yield();
      }
      Direction toSense = robotControl.getDirection();
      if(sensorSys.senseObjectAtLocation(firstMine.add(toSense), RobotLevel.MINE) != null
              && sensorSys.senseObjectAtLocation(firstMine.add(toSense), RobotLevel.ON_GROUND)==null) {
        //if there is a mine there with nothing on it
        otherMine = firstMine.add(toSense);
        System.out.println("found the other mine at "+otherMine);
      }
      if(navSys.canMove(toSense)) {
        numFreeSquares++;
      }
    }

    //if there are two ways out, build the mine, and back out and build the next mine
    if(numFreeSquares > 1) {
      System.out.println("many ways out!");
      //build a mine on the free square
      while(robotControl.getTeamResources() < BuildOrder.RECYCLER.cost + PlayerConstants.MINIMUM_FLUX) {
        yield();
      }
      seqBuild(BuildOrder.RECYCLER, otherMine);

      //yield and backout when you can
      while(navSys.isActive()) {
        yield();
      }
      navSys.setMoveBackward();
      yield();

      //build a mine on the free square
      while(robotControl.getTeamResources() < BuildOrder.RECYCLER.cost + PlayerConstants.MINIMUM_FLUX) {
        yield();
      }
      seqBuild(BuildOrder.RECYCLER, firstMine);

    }
    //this means that we are stuck! we should back-up, then build on the first mine, then build on other mine
    else {
      System.out.println("only one way out!");
      //yield and backout when you can
      while(navSys.isActive()) {
        yield();
      }
      navSys.setMoveBackward();
      yield();

      //build a mine on the free square
      while(robotControl.getTeamResources() < BuildOrder.RECYCLER.cost + PlayerConstants.MINIMUM_FLUX) {
        yield();
      }
      seqBuild(BuildOrder.RECYCLER, firstMine);

      //yield and backout when you can
      while(navSys.isActive()) {
        yield();
      }
      navSys.setMoveBackward();
      yield();

      //build a mine on the free square
      while(robotControl.getTeamResources() < BuildOrder.RECYCLER.cost + PlayerConstants.MINIMUM_FLUX) {
        yield();
      }
      seqBuild(BuildOrder.RECYCLER, otherMine);

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
