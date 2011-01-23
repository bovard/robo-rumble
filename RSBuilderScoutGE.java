
package team122;
import battlecode.common.*;

/**
 * The GameEvents class for the BuiderScout
 * @author bovard
 */
public class RSBuilderScoutGE extends BuilderSensorGameEvents {

  protected RSBuilderScout robotSys;
  //new mission game event
  protected boolean seeNewUncoveredMine;
  //new normal game event
  protected boolean seeUncoveredMines;
  //class variables
  private int lastNumMines;
  private int lastNumMines2TurnsAgo;

  public RSBuilderScoutGE(RobotController robotControl, CommunicationsSystem comSys, SensorSystem sensorSys,
          RSBuilderScout robotSys) {
    super(robotControl, comSys, sensorSys);
    this.robotSys = robotSys;

  }
  
  @Override
  public void resetGameEvents() {
    if(robotSys != null) {
      lastNumMines2TurnsAgo = lastNumMines;
      lastNumMines = robotSys.getNumberOfUncoveredMines();
    }
    else {
      lastNumMines = 0;
      lastNumMines2TurnsAgo = 0;
    }
    seeNewUncoveredMine = false;
    seeUncoveredMines = false;
    super.resetGameEvents();
  }

  @Override
  public void calcGameEvents() {
    calcCanSeeNewUncoveredMine();
    calcCanSeeUncoveredMine();
    super.calcGameEvents();
  }

  /**
   * Adds the seeNewUncoveredMine game event in as a MISSION game event. So if the current priority
   * is normal or none it will include the seeNewUncoveredMine now
   * @param priority the priority level to check against
   * @return if a game event has occurred
   */
  @Override
  public boolean checkGameEventsAbovePriority(int priority) {
    switch(priority) {
      case GameEventLevelPriority.COMBAT:
        //highest priority level, can't have one higher
        return super.checkGameEventsAbovePriority(priority);
      case GameEventLevelPriority.DIRECTIVE:
        //check the COMBAT game events
        return super.checkGameEventsAbovePriority(priority);
      case GameEventLevelPriority.MISSION:
        //check the COMBAT and DIRECTIVE game events
        return super.checkGameEventsAbovePriority(priority);
      case GameEventLevelPriority.NORMAL:
        //check the COMBAT, DIRECTIVE and MISSION game events
        return super.checkGameEventsAbovePriority(priority) || seeNewUncoveredMine;
      case GameEventLevelPriority.LOW:
        //check the COMBAT, DIRECTIVE, MISSION and NORMAL game events
        return super.checkGameEventsAbovePriority(priority) || seeNewUncoveredMine || seeUncoveredMines;
      case GameEventLevelPriority.NONE:
        //check all game events
        return super.checkGameEventsAbovePriority(priority) || seeNewUncoveredMine || seeUncoveredMines;
    }
    System.out.println("WARNING: Fell through RSBuilderScoutGE.checkGameEventsAbovePriority (bad priority)");
    return false;
  }


  /**
   * detects if the robot knows the location of an uncovered mine
   */
  protected void calcCanSeeUncoveredMine() {
    if(robotSys.getNumberOfUncoveredMines() > 0) {
      seeUncoveredMines = true;
    }
    else {
      seeUncoveredMines = false;
    }
  }

  /**
   * Detects if the robot has just found an uncovered mine
   */
  protected void calcCanSeeNewUncoveredMine() {
    if (lastNumMines2TurnsAgo < robotSys.getNumberOfUncoveredMines() ) {
      seeNewUncoveredMine = true;
      lastNumMines = robotSys.getNumberOfUncoveredMines();
      lastNumMines2TurnsAgo = robotSys.getNumberOfUncoveredMines();
      System.out.println("it's true!");
    }
    else {
      seeNewUncoveredMine = false;
    }
  }

  /**
   * checks to see if the robot saw a new mine this turn
   * @return if the robot saw a new mine this turn
   */
  public boolean canSeeNewUncoveredMine() {
    return seeNewUncoveredMine;
  }

  /**
   * returns if the robot has uncovered mines
   * @return if the robot has uncovered mines
   */
  public boolean canSeeUncoveredMine() {
    return seeUncoveredMines;
  }
}
