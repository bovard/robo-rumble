
package team122;
import battlecode.common.*;

/**
 *
 * @author bovard
 */
public class BuilderScoutRobotSystem extends ScoutRobotSystem {
  protected BuilderController buildControl;

  public BuilderScoutRobotSystem() {

  }

  public BuilderScoutRobotSystem(RobotController robotControl) {
    super(robotControl);

    //on scouts, build systems should be in component[2], added another if clause to catch
    //our starting bot, who has it in components[1]
    if(robotControl.components()[1].type() == ComponentType.CONSTRUCTOR)
      buildControl = (BuilderController)robotControl.components()[1];
    else
      buildControl = (BuilderController)robotControl.components()[2];

  }

  public void go() {
    while(true) {
      selScout();
    }
  }

  /**
   * the bot attempts to perform the ScoutBuild sequence, if it can't it tries to flee
   * @return
   */
  protected boolean selScout() {
    if(seqScoutBuild())
      return true;
    return actFlee();
  }

  /**
   * sends the robot scouting to find an uncovered mine which it will build a recycler on
   * @return
   */
  protected boolean seqScoutBuild() {
    if (actScoutMine()) {
      if(actBuildRecycler()) {
        return true;
      }
    }
    return false;
  }

  /**
   * builds a recycler on the newly scouted uncovered mine
   * @return whether or not the action was completeled sucessfully
   */
  protected boolean actBuildRecycler() {

    return false
  }
}
