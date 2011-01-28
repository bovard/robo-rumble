/*
 * RobotSystem inheritance chart (base classes)
 *
 * RobotSytem
 * ->SensorRobotSystem
 * --->BuilderSensorRobotSystem
 * ----->WeaponBuilderSensorRobotSystem
 *
 * Base Systems: Communication, Navigation, Weapon, Builder
 *
 * Note: all other systems are implemented in specific RobotSystems
 *
 */

package team122;
import battlecode.common.*;
import java.util.Random;

/**
 * The base class for our robot AI, this should have a base for all robots, be they
 * flying, ground, or building
 * @author bovard
 */
public class RobotSystem {

  protected MapLocation birthPlace;
  protected Random rand = new Random();
  
  /**
   * the robot will be inerrupted by any game events higher than it's current GameEventLevel
   * @see GameEventLevel
   * @see GameEvents
   */
  protected GameEventLevel currentGameEventLevel;
  protected RobotController robotControl;
  protected GameEvents gameEvents;
  protected CommunicationsSystem comSys;
  protected NavigationSystem navSys;

  /**
   * Creates a new RobotSystem base class, assumes that there is a movement controller
   * @param robotControl the RobotController
   */
  public RobotSystem(RobotController robotControl) {
    this.robotControl = robotControl;

    //as of 1.07 the movementcontroller is ALWAYS the first item in the components list
    MovementController moveControl = (MovementController)robotControl.components()[0];
    navSys = new NavigationSystem(robotControl, moveControl);
    birthPlace = robotControl.getLocation();
    comSys = new CommunicationsSystem(robotControl);
    gameEvents = new GameEvents(robotControl, comSys);

    //check out PlayerConstants to see what each of these filters
    comSys.setFilter(new int[] {1, 0, 0});

    rand.setSeed(Clock.getRoundNum());
  }


  /**
   * This function will be called after the system is created, it should house the loop
   * for any robot, this is the base class, we shouldn't fall back to this as it has no
   * functionality, if we return from this the robot dies
   */
  public void go() {
    while(true) {
      //start of main loop
      System.out.println("fell back to RobotSystem main go loop! (something's wrong)");
      yield();
    }
  }

  /**
   * Couples the yield function with game events, resetting the game events just before
   * calling yield and calculating them again as the first action for the robot
   */
  protected void yield() {
    gameEvents.resetGameEvents();
    robotControl.yield();
    gameEvents.calcGameEvents();
    robotControl.setIndicatorString(0, "ID: " + robotControl.getRobot().getID() + " - Location: "+robotControl.getLocation().toString());
  }


  /**
   * Called to move multiple times to a destination, assumes the destination is already set
   * Note: will fall out if GameEvent higher than gameEventLevel happens
   * @return if the destination was reached safely
   */
  protected boolean seqMove() {
    boolean hasGameEvents = gameEvents.checkGameEventsAbove(currentGameEventLevel);
    boolean done = navSys.isAtDestination();
    while(!hasGameEvents && !done) {
      while(navSys.isActive() && !hasGameEvents) {
        yield();
        hasGameEvents = gameEvents.checkGameEventsAbove(currentGameEventLevel);
      }
      if (!hasGameEvents) {
        if(actMove()) {
          hasGameEvents = gameEvents.checkGameEventsAbove(currentGameEventLevel);
          done = navSys.isAtDestination();
        }
        else {
          return false;
        }
      }
    }
    return done;
  }


  /**
   * called to turn a robot, all robots can turn (even buildings)
   * Note: will only return true if the robot can turn and dir != currentDirection
   * @param dir direction to turn in
   * @return if the turn was executed successfully
   */
  protected boolean actTurn(Direction dir) {
    if(navSys.setTurn(dir)) {
      yield();
      return true;
    }
    System.out.println("WARNING: tried to turn but couldn't");
    return false;
  }



  /**
   * Called to move once (and yield) Assumes the robot already has a destination
   * @return if robot is in its current destination
   */
  protected boolean actMove() {
    if(navSys.getDestination()==null) {
      return false;
    }
    else if(navSys.setNextMove()) {
      yield();
      robotControl.setIndicatorString(0, "ID: " + robotControl.getRobot().getID() + " - Location: "+robotControl.getLocation().toString());
      return true;
    }
    System.out.println("WARNING: tried to move but couldn't");
    return false;
  }

  /**
   * checks to see if the robot can move forward and moves forward if it can
   * @return if the move was performed successfully
   */
  protected boolean actMoveForward() {
    if(navSys.setMoveForward()) {
      yield();
      return true;
    }
    System.out.println("WARNING: tried to move forward but couldn't");
    return false;
  }

  /**
   * checks to see if the robot can move backward and move backward if it can
   * @return if the move was performed successfully
   */
  protected boolean actMoveBackward() {
    if(navSys.setMoveBackward()) {
      yield();
      return true;
    }
    System.out.println("WARNING: tried to move backward but couldn't");
    return false;
  }

  /**
   * Yields until the navSys isn't active, dropping out if we encounter a game event
   * @return if the navSys is not free
   */
  protected boolean eventWaitForNavSys () {
    while(navSys.isActive() && !gameEvents.checkGameEventsAbove(currentGameEventLevel)) {
      yield();
    }
    return !gameEvents.checkGameEventsAbove(currentGameEventLevel);
  }

  /**
   * Forces the robot to move somewhere.
   * @param moveForward true if should force a move forward, false if should move backwards
   * @return if the move was succesful
   */
  protected boolean setForceMove(boolean moveForward) {
    //System.out.println("forcing a move forward? "+moveForward);
    while(navSys.isActive()) {
      yield();
    }

    Direction ourDir = robotControl.getDirection();
    if(!moveForward) {
      ourDir = robotControl.getDirection().opposite();
    }
    
    // if you can tun 45 right or left, randomize
    if(navSys.canMove(ourDir.rotateLeft()) && navSys.canMove(ourDir.rotateRight())) {
      //System.out.println("canMove 45 left OR right!");
      if(rand.nextBoolean()) {
        navSys.setTurn(ourDir.rotateLeft());
        while(navSys.isActive()) {
          yield();
        }
      }
      else {
        navSys.setTurn(ourDir.rotateRight());
        while(navSys.isActive()) {
          yield();
        }
      }
    }
    //if you can move 45 left
    else if(navSys.canMove(ourDir.rotateLeft())) {
      //System.out.println("canMove 45 left");
      navSys.setTurn(ourDir.rotateLeft());
      while(navSys.isActive()) {
        yield();
      }
    }
    //if you can move 45 right
    else if(navSys.canMove(ourDir.rotateRight())) {
      //System.out.println("canMove 45 right!");
      navSys.setTurn(ourDir.rotateRight());
      while(navSys.isActive()) {
        yield();
      }
    }
    // if you can tun 90 right or left, randomize
    else if(navSys.canMove(ourDir.rotateLeft().rotateLeft())
            && navSys.canMove(ourDir.rotateRight().rotateRight())) {
      //System.out.println("canMove 90 left OR right!");
      if(rand.nextBoolean()) {
        navSys.setTurn(ourDir.rotateLeft().rotateLeft());
        while(navSys.isActive()) {
          yield();
        }
      }
      else {
        navSys.setTurn(ourDir.rotateRight().rotateRight());
        while(navSys.isActive()) {
          yield();
        }
      }
    }
    //if you can move 90 left
    else if(navSys.canMove(ourDir.rotateLeft().rotateLeft())) {
      //System.out.println("canMove 90 left!");
      navSys.setTurn(ourDir.rotateLeft().rotateLeft());
      while(navSys.isActive()) {
        yield();
      }
    }
    //if you can move 90 right
    else if(navSys.canMove(ourDir.rotateRight().rotateRight())) {
      //System.out.println("canMove 90 right!");
      navSys.setTurn(ourDir.rotateRight().rotateRight());
      while(navSys.isActive()) {
        yield();
      }
    }
    // if you can tun 135 right or left, randomize
    else if(navSys.canMove(ourDir.rotateLeft().rotateLeft().rotateLeft())
            && navSys.canMove(ourDir.rotateRight().rotateRight().rotateRight())) {
      //System.out.println("canMove 135 left OR right!");
      if(rand.nextBoolean()) {
        navSys.setTurn(ourDir.rotateLeft().rotateLeft().rotateLeft());
        while(navSys.isActive()) {
          yield();
        }
      }
      else {
        navSys.setTurn(ourDir.rotateRight().rotateRight().rotateRight());
        while(navSys.isActive()) {
          yield();
        }
      }
    }
    //if you can move 135 left
    else if(navSys.canMove(ourDir.rotateLeft().rotateLeft().rotateLeft())) {
      //System.out.println("canMove 135 left");
      navSys.setTurn(ourDir.rotateLeft().rotateLeft());
      while(navSys.isActive()) {
        yield();
      }
    }
    //if you can move 135 right
    else if(navSys.canMove(ourDir.rotateRight().rotateRight().rotateRight())) {
      //System.out.println("canMove 135 right!");
      navSys.setTurn(ourDir.rotateRight().rotateRight().rotateRight());
      while(navSys.isActive()) {
        yield();
      }
    }
    //else we can't move in the direction we want and have to move in the opposite direction if we can
    else {
      if(moveForward) {
        if(navSys.canMove(ourDir.opposite())) {
          navSys.setMoveBackward();
        }
      }
      else {
        if (navSys.canMove(ourDir.opposite())) {
          navSys.setMoveForward();
        }
      }
    }
    if(!navSys.isActive())
    {
      if(moveForward) {
        navSys.setMoveForward();
      }
      else {
        navSys.setMoveBackward();
      }
    }
    return true;
  }

}