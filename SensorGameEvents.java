
package team122;
import battlecode.common.*;

/**
 * SensorGameEvents, the events in GameEvents plus those that can be found with a sensor
 * @author bovard
 */
public class SensorGameEvents extends GameEvents {
  SensorSystem sensorSys;

  //game events
  //normal game events
  boolean seeMine, seeDebris;
  //mission gameEvents

  //critical game events
  boolean seeEnemy;

  /**
   * Creates a SensorGameEvents class to manage GameEvents based from Sensor
   * @param robotControl the robotcontroller
   * @param sensorControl the sensorcontroller
   */
  public SensorGameEvents(RobotController robotControl, CommunicationsSystem comSys, SensorSystem sensorSys) {
    super(robotControl, comSys);
    this.sensorSys = sensorSys;
  }

  @Override
  public void resetGameEvents() {
    super.resetGameEvents();
    seeMine = false;
    seeDebris = false;
    seeEnemy = false;
  }

  /**
   * calculates if any game events have occurred this turn
   * @return if any game events have occured
   */
  @Override
  public boolean calcGameEvents() {
    calcSeeMine();
    calcSeeDebris();
    calcSeeEnemy();
    return super.calcGameEvents() || seeMine || seeDebris || seeEnemy;
  }

  /**
   * calculates only soldier-relevant game events (ie. excludes scans for mine info)
   * @return if any soldier-relevant game events have happened
   */
  public boolean calcSoldierGameEvents() {
    calcSeeDebris();
    calcSeeEnemy();
    return super.calcGameEvents() || seeDebris || seeEnemy;
  }

  /**
   * checks to see if any game events have occured this turn
   * @return if any game events have occured
   */
  @Override
  public boolean checkGameEvents(int priority) {
    boolean event = false;
    switch(priority) {
      case GameEventLevelPriority.COMBAT:
        //highest priority level, can't have one higher
        return super.checkGameEvents(priority) || false;
      case GameEventLevelPriority.MISSION:
        //check the CRICITAL game events
        return super.checkGameEvents(priority) || seeEnemy;
      case GameEventLevelPriority.NORMAL:
        //check the COMBAT and MISSION game events
        return super.checkGameEvents(priority) || seeEnemy;
      case GameEventLevelPriority.NONE:
        //check all game events
        return super.checkGameEvents(priority) || seeEnemy || seeMine || seeDebris;
    }
    System.out.print("WARNING: fell through checkGameEvents (bad priority level)");
    return false;
  }


  /**
   * calculates if a mine can be seen for the current turn
   */
  protected void calcSeeMine() {
    if(sensorSys.getMines().length > 0) {
      seeMine = true;
    }
    else {
      seeMine = false;
    }
  }
  
  /**
   * calculates to see if the bot can sense an enemy
   */
  protected void calcSeeEnemy() {
    if(sensorSys.getBots(robotControl.getTeam().opponent()).length > 0) {
      seeEnemy = true;
    }
    else {
      seeEnemy = false;
    }
  }
  
  /**
   * calculates to see if the bot can sense debris
   */
  protected void calcSeeDebris() {
    if(sensorSys.getBots(Team.NEUTRAL).length > 0) {
      seeDebris = true;
    }
    else {
      seeDebris = false;
    }
  }

  /**
   * checks to see if the robot can see a mine this turn
   * @return if the robot can see a mine
   */
  public boolean canSeeMine() {
    return seeMine;
  }

  /**
   * checks to see if the robot can sense an enemy this turn
   * @return if the robot can see an enemy
   */
  public boolean canSeeEnemy() {
    return seeEnemy;
  }

  /**
   * checks to see if the robot can sense debris this turn
   * @return if the robot can see debris
   */
  public boolean canSeeDebris() {
    return seeDebris;
  }


}
