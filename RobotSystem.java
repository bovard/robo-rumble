/*
 * RobotSystem inheritance chart
 *
 * RobotSytem
 * ->BuildingRobotSystem
 * --->RecyclerRobotSystem
 * ->MobileRobotSystem
 * --->ScoutRobotSystem
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

  protected final int MINIMUM = 5;



  public RobotSystem(RobotController robotControl) {
    this.robotControl = robotControl;

    //as of 1.07 the movementcontroller is always the first item in the components list
    moveControl = (MovementController)robotControl.components()[0];
    birthPlace = robotControl.getLocation();

  }

  /**
   * This function will be called after the system is created, it should house the loop
   * for any robot
   */
  public void go() {
    while(true) {
      //start of main loop
      System.out.println("fell back to RobotSystem main go loop! (something's wrong)");
      yield();
    }
  }


    /**
   * called to turn a robot
   * @param dir direction to turn in
   * @return if the turn was executed successfully
   */
  protected boolean actTurn(Direction dir) {
    try {
      moveControl.setDirection(dir);
      yield();
      return true;
    } catch (Exception e) {
      System.out.println("caught exception:");
      e.printStackTrace();
      return false;
    }
  }

  /**
   * This will be coupled with the GameEvents class eventually
   */
  protected void yield() {
    robotControl.yield();
    robotControl.setIndicatorString(0, robotControl.getLocation().toString());
  }

}