/*
 * RobotSystem inheritance chart
 *
 * RobotSytem
 * ->BuildingRobotSystem
 * --->RecyclerRobotSystem
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
  protected RobotController robotControl;
  protected MovementController moveControl;
  protected GameEvents gameEvents;
  protected CommunicationsSystem comSys;

  protected final int MINIMUM_ENERGON = 5;


  /**
   * Creates a new RobotSystem base class, assumes that there is a movement controller
   * @param robotControl the RobotController
   */
  public RobotSystem(RobotController robotControl) {
    this.robotControl = robotControl;

    //as of 1.07 the movementcontroller is always the first item in the components list
    moveControl = (MovementController)robotControl.components()[0];
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
   * @param dir direction to turn in
   * @return if the turn was executed successfully
   */
  protected boolean actTurn(Direction dir) {
    try {
      if(!moveControl.isActive()) {
        moveControl.setDirection(dir);
      }
      yield();
      return true;
    } catch (Exception e) {
      System.out.println("caught exception:");
      e.printStackTrace();
      return false;
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
    robotControl.setIndicatorString(0, "Location: "+robotControl.getLocation().toString());
  }

}