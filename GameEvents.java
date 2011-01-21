package team122;
import battlecode.common.*;

/**
 * Calculates certain events that can happen in a robot's life. Meeting their first love,
 * going for a walk in the park, getting shot at by enemey robots, etc...
 *
 * Game events should interface to the robot through this class or a child. Events that should
 * interrupt a robot's routine events are called critical events, whereas the rest are just
 * events. Critical events will stop a robot in mid step and make it reconsider it's path, whereas
 * regular events will only be processed (or checked) once in a while.
 * 
 *
 * @author bovard
 */
public class GameEvents {

  protected RobotController robotControl;
  protected CommunicationsSystem comSys;

  //list of GameEvents, we'll add more as we get more complex behavoir
  //Note: check out GameEventLevels to see the levels
  //Normal
  protected boolean lowHealth, hasMessages, negativeFluxRegen;
  //Misison
  protected boolean hasDirective;
  //critcal GameEvents
  protected boolean lostHealth;

  //class variables
  private double formerHP;
  private int numTurnsNeg;
  private double formerFlux;

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
    negativeFluxRegen = false;
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
    return lostHealth || lowHealth || hasDirective || hasMessages || negativeFluxRegen;
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
   * calculates if there is a negativeflux regen and if this is above the threshold for
   * number of turns negative
   */
  protected void calcNegativeFluxRegen() {
    if (robotControl.getTeamResources() < formerFlux) {
      numTurnsNeg++;
      if (numTurnsNeg < PlayerConstants.NUM_TURNS_NEGATIVE) {
        negativeFluxRegen = true;
      }
      else {
        negativeFluxRegen = false;
      }
    }
    else {
      numTurnsNeg = 0;
    }
    formerFlux = robotControl.getTeamResources();
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
   * checks to see if the teams flux levels have a negative regen
   * @return if there is negative flux regen
   */
  public boolean negativeFluxRegen() {
    return negativeFluxRegen;
  }

  /**
   * checks to see if any GameEvents have occurred of priority > priority
   * @param priority the priority of game events to check
   * @return true if a gameevent has occurred with priority > the imputted priority
   */
  public boolean checkGameEvents(int priority) {
    switch(priority) {
      case GameEventLevelPriority.CRITICAL:
        //highest priority level, can't have one higher
        return false;
      case GameEventLevelPriority.MISSION:
        //check the CRICITAL game events
        return lostHealth;
      case GameEventLevelPriority.NORMAL:
        //check the CRITICAL and MISSION game events
        return lostHealth || hasDirective;
      case GameEventLevelPriority.NONE:
        //check all game events
        return lostHealth || lowHealth || hasDirective || negativeFluxRegen;
    }
    System.out.print("WARNING: fell through checkGameEvents (bad priority level)");
    return false;
  }

}
