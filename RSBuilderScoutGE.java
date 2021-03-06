
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
    calcLostHealth();
    //Note: calcRecentlyLostHeath() must be called AFTER calcLostHealth()
    calcRecentlyLostHealth();
    calcSeeMine();
    calcSeeEnemy();
    calcFluxRegen();
  }

  /**
   * Adds the seeNewUncoveredMine game event in as a MISSION game event. So if the current priority
   * is normal or none it will include the seeNewUncoveredMine now
   * @param priority the priority level to check against
   * @return if a game event has occurred
   */
  @Override
  public boolean checkGameEventsAbove(GameEventLevel gameEventLevel) {
    switch(gameEventLevel.priority) {
      case GameEventLevelPriority.CRITICAL:
        //highest level
        return super.checkGameEventsAbove(gameEventLevel);
      case GameEventLevelPriority.COMBAT:
        //check CRITICAL game events and above
        return super.checkGameEventsAbove(gameEventLevel);
      case GameEventLevelPriority.DIRECTIVE:
        //check the COMBAT game events and above
        return super.checkGameEventsAbove(gameEventLevel);
      case GameEventLevelPriority.MISSION:
        //check the DIRECTIVE game events and above
        return super.checkGameEventsAbove(gameEventLevel);
      case GameEventLevelPriority.NORMAL:
        //check the MISSION game events and above
        return super.checkGameEventsAbove(gameEventLevel) || seeNewUncoveredMine;
      case GameEventLevelPriority.LOW:
        //check the NORMAL game events and above
        return super.checkGameEventsAbove(gameEventLevel) || seeNewUncoveredMine || seeUncoveredMines;
      case GameEventLevelPriority.NONE:
        //check all game events
        return super.checkGameEventsAbove(gameEventLevel) || seeNewUncoveredMine || seeUncoveredMines;
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
