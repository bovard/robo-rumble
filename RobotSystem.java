/*
 * RobotSystem inheritance chart
 *
 * RobotSytem
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
  protected RobotController robotControl;
  protected MovementController moveControl;


  public RobotSystem() {

  }

  public RobotSystem(RobotController robotControl) {
    this.robotControl = robotControl;

    //as of 1.06 the movementcontroller is always the first item in the components list
    moveControl = (MovementController)robotControl.components()[0];

  }

  /**
   * This function will be called after the system is created, it should house the loop
   * for any robot
   */
  public void go() {
    while(true) {
      //start of main loop
    }
  }

  /**
   * This will be coupled with the GameEvents class eventually
   */
  protected void yield() {
    robotControl.yield();
  }

}