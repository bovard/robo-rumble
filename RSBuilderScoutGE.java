
package team122;
import battlecode.common.*;

/**
 * The GameEvents class for the BuiderScout
 * @author bovard
 */
public class RSBuilderScoutGE extends BuilderSensorGameEvents {

  protected RSBuilderScout robotSys;
  //new mission game event
  protected boolean seeUncoveredMine;

  public RSBuilderScoutGE(RobotController robotControl, CommunicationsSystem comSys, SensorSystem sensorSys,
          RSBuilderScout robotSys) {
    super(robotControl, comSys, sensorSys);
    this.robotSys = robotSys;
  }
  
  @Override
  public void resetGameEvents() {
    seeUncoveredMine = false;
    super.resetGameEvents();
  }

  @Override
  public void calcGameEvents() {
    calcCanSeeUncoveredMine();
    super.calcGameEvents();
  }

  /**
   * Adds the seeUncoveredMine game event in as a MISSION game event. So if the current priority
   * is normal or none it will include the seeUncoveredMine now
   * @param priority the priority level to check against
   * @return if a game event has occurred
   */
  @Override
  public boolean checkGameEvents(int priority) {
    if(priority <= GameEventLevel.NORMAL.priority) {
      return seeUncoveredMine || super.checkGameEvents(priority);
    }
    return super.checkGameEvents(priority);
  }


  /**
   * Detects if there is an uncovered mine detected
   */
  protected void calcCanSeeUncoveredMine() {
    seeUncoveredMine = robotSys.hasUncoveredMines();
  }

  public boolean canSeeUncoveredMine() {
    return seeUncoveredMine;
  }
}
