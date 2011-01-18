package team122;
import battlecode.common.*;

/**
 * Calculates certain events that can happen in a robot's life. Meeting their first love,
 * going for a walk in the park, getting shot at by enemey robots, etc...
 *
 * Game events should interface to the robot through this class or a child. Events that should
 * interrupt a robot's routine events are called critical events, whereas the rest are just
 * events. Critical events will stop a robot in mid step and make it reconsider it's path, whereas
 * regular events will only be processed once in a while.
 * 
 *
 * @author bovard
 */
public class GameEvents {

  protected RobotController robotControl;
  protected CommunicationsSystem comSys;

  //list of GameEvents, we'll add more as we get more complex behavoir
  boolean lowHealth, hasMessages;

  //critcal GameEvents
  boolean lostHealth, hasDirective;

  //class variables
  double formerHP;

  /**
   * Basic constructor for GameEvents, just requires a robotcontroller
   * @param robotControl the robotcontroller to monitor
   */
  public GameEvents(RobotController robotControl, CommunicationsSystem comSys) {
    this.robotControl = robotControl;
    this.comSys = comSys;

    //initialize events
    resetGameEvents();
  }

  /**
   * Sets all events to false and resets constants, this is called right before a robot yields
   */
  public void resetGameEvents() {
    lostHealth = false;
    lowHealth = false;
    hasDirective = false;
    formerHP = robotControl.getHitpoints();
  }

  /**
   * calculates the game events, returning true if any game events return true; this is the first
   * thing called after a robot yields
   * @return if any game events happened
   */
  public boolean calcGameEvents() {
    calcLostHealth();
    calcLowHealth();
    calcHasDirective();
    return lostHealth || lowHealth || hasDirective || hasMessages;
  }

  protected void calcHasDirective() {
    hasMessages = comSys.checkMail();
    hasDirective = comSys.hasDirective();
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
   * checks to see if the robot has a message directive
   * @return if the robot has a message directive
   */
  public boolean hasDirective() {
    return hasDirective;
  }

  /**
   * checks to see if the robot has messages
   * @return if the robot has messages
   */
  public boolean hasMessages() {
    return hasMessages;
  }

  /**
   * checks to see if any GameEvents have occurred
   * @return true if a gameevent has occurred
   */
  public boolean checkGameEvents() {
    return lostHealth || lowHealth || hasDirective;
  }




  /**
   * checks to see if any critical game events have occurred (game events that should
   * interrupt any behavoir except combat oriented ones).
   */
  public boolean checkCriticalGameEvents() {
    return lostHealth || hasDirective;
  }
}
