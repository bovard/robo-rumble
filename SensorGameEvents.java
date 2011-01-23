
package team122;
import battlecode.common.*;

/**
 * SensorGameEvents, the events in GameEvents plus those that can be found with a sensor
 * @author bovard
 */
public class SensorGameEvents extends GameEvents {
  SensorSystem sensorSys;

  //game events
  //idle game events
  protected boolean seeMine, seeDebris;
  //combat game events
  protected boolean seeEnemy;

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
   */
  @Override
  public void calcGameEvents() {
    calcSeeMine();
    calcSeeDebris();
    calcSeeEnemy();
  }

  /**
   * checks to see if any game events have occured this turn
   * @return if any game events have occured
   */
  @Override
  public boolean checkGameEventsAbovePriority(int priority) {
    switch(priority) {
      case GameEventLevelPriority.COMBAT:
        //highest priority level, can't have one higher
        return super.checkGameEventsAbovePriority(priority);
      case GameEventLevelPriority.DIRECTIVE:
        //check the COMBAT game events
        return super.checkGameEventsAbovePriority(priority) || seeEnemy;
      case GameEventLevelPriority.MISSION:
        //check the COMBAT and DIRECTIVE game events
        return super.checkGameEventsAbovePriority(priority) || seeEnemy;
      case GameEventLevelPriority.NORMAL:
        //check the COMBAT, DIRECTIVE and MISSION game events
        return super.checkGameEventsAbovePriority(priority) || seeEnemy;
      case GameEventLevelPriority.LOW:
        //check the COMBAT, DIRECTIVE, MISSION and NORMAL game events
        return super.checkGameEventsAbovePriority(priority) || seeEnemy;
      case GameEventLevelPriority.NONE:
        //check all game events
        return super.checkGameEventsAbovePriority(priority) || seeEnemy || seeMine || seeDebris;
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
