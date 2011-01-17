package team122;
import battlecode.common.*;

/**
 * Calculates certain events that can happen in a robot's life. Meeting their first love,
 * going for a walk in the park, getting shot at by enemey robots, etc...
 * @author bovard
 */
public class GameEvents {

  private RobotController robotControl;

  //list of GameEvents, we'll add more as we get more complex behavoir
  boolean lowHealth;

  //critcal GameEvents
  boolean lostHealth;

  //class variables
  double formerHP;

  /**
   * Basic constructor for GameEvents, just requires a robotcontroller
   * @param robotControl the robotcontroller to monitor
   */
  public GameEvents(RobotController robotControl) {
    this.robotControl = robotControl;

    //initialize events
    resetGameEvents();
  }

  /**
   * Sets all events to false and resets constants
   */
  public void resetGameEvents() {
    lostHealth = false;
    lowHealth = false;
    formerHP = robotControl.getHitpoints();
  }

  /**
   * calculates the game events, returning true if any game events return true;
   * @return if any game events happened
   */
  public boolean calcGameEvents() {
    calcLostHealth();
    calcLowHealth();

    return lostHealth || lowHealth;
  }

  /**
   * calculates if the robot has lost health since last turn
   * @return if the robot has lost health
   */
  protected void calcLostHealth() {
    if (robotControl.getHitpoints() < formerHP) {
      lostHealth = true;
    }
    else {
      lostHealth = false;
    }
  }

  /**
   * calculates if the robot has low health
   * @return if the robot has low health
   */
  protected void calcLowHealth() {
    if (robotControl.getHitpoints() < .1*robotControl.getMaxHp()) {
      lowHealth = true;
    }
    else {
      lowHealth = false;
    }
  }

  /**
   * Returns the game event lost health, which means the bot would have lost helath from
   * last turn if this returned true
   * @return if the robot has lost health since last turn
   */
  public boolean lostHeath() {
    return lostHealth;
  }

  /**
   * checks to see if the robot is low health
   * @return if the robot has less than 10% of max HP
   */
  public boolean hasLowHealth() {
    return lowHealth;
  }

  /**
   * checks to see if any GameEvents have occurred
   * @return true if a gameevent has occurred
   */
  public boolean checkGameEvents() {
    return lostHealth || lowHealth;
  }

  /**
   * checks to see if any critical game events have occurred (game events that should
   * interrupt any behavoir except combat oriented ones).
   */
  public boolean checkCriticalGameEvents() {
    return lostHealth;
  }
}
