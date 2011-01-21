/*
 * RobotSystem inheritance chart
 *
 * RobotSytem
 * ->BuildingRobotSystem
 * --->RecyclerRobotSystem
 * ----->ComRecyclerRobotSystem
 * --->AttackTurretRobotSystem
 * ->MobileRobotSystem
 * --->SensorRobotSystem
 * ----->BuilderScoutRobotSystem
 * ----->FighterScoutRobotSystem
 */

package team122;
import battlecode.common.*;

/**
 * The base class for our robot AI, this should have a base for all robots, be they
 * flying, ground, or building
 * @author bovard
 */
public class RobotSystem {

  protected MapLocation birthPlace;
  
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

    //as of 1.07 the movementcontroller is always the first item in the components list
    MovementController moveControl = (MovementController)robotControl.components()[0];
    navSys = new NavigationSystem(robotControl, moveControl);
    birthPlace = robotControl.getLocation();
    comSys = new CommunicationsSystem(robotControl);
    gameEvents = new GameEvents(robotControl, comSys);
   
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
   * Called to move multiple times to a destination
   * @param dest place to move to
   * @return if the destination was reached safely
   */
  protected boolean seqMove(MapLocation dest) {
    navSys.setDestination(dest);

    boolean hasGameEvents = gameEvents.checkGameEvents(currentGameEventLevel.priority);
    boolean done = navSys.isAtDestination();
    while(!hasGameEvents && !done) {
      //waits until it can move, then does so
      while(navSys.isActive() && !hasGameEvents) {
        yield();
        hasGameEvents = gameEvents.checkGameEvents(currentGameEventLevel.priority);
      }
      //moves and checks to see if we're at the destination
      actMove();
      done = navSys.isAtDestination();
      hasGameEvents = gameEvents.checkGameEvents(currentGameEventLevel.priority);
    }
    return done;
  }

  /**
   * Called to move multiple times to a destination, assumes the destination is already set
   * Note: will fall out if GameEvent higher than gameEventLevel happens
   * @param dest place to move to
   * @return if the destination was reached safely
   */
  protected boolean seqMove() {
    boolean hasGameEvents = gameEvents.checkGameEvents(currentGameEventLevel.priority);
    boolean done = navSys.isAtDestination();
    while(!hasGameEvents && !done) {
      while(navSys.isActive() && !hasGameEvents) {
        yield();
        hasGameEvents = gameEvents.checkGameEvents(currentGameEventLevel.priority);
      }
      actMove();
      hasGameEvents = gameEvents.checkGameEvents(currentGameEventLevel.priority);
      done = navSys.isAtDestination();
    }
    return done;
  }

  /**
   * Called to move once (and yield) Assumes the robot already has a destination
   * @return if robot is in its current destination
   */
  protected boolean actMove() {
    if(navSys.setNextMove()) {
      yield();
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

}