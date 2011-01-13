
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
   * @return if either action was sucessfull
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
    if (actScoutUncoveredMine()) {
      if(seqBuildRecycler()) {
        return true;
      }
    }
    return false;
  }

  /**
   * builds a recycler on the newly scouted uncovered mine
   * @return whether or not the action was completeled sucessfully
   */
  protected boolean seqBuildRecycler() {
    // TODO: implement this
    // find a mine that isn't covered already and pull the locaiton
    Mine[] mines = sensorSys.getMines();
    MapLocation mineLoc;
    for (int i=0; i<mines.length;i++) {

    }
    // find a free square adjacent to target mine, return false if can't
    // move adjacent to target mine, return false if can't
    // build a building on target mine, return false if can't
    // build a recycler on the building, return false if can't
    // return true if all these are done
    return false;
  }


  /**
   * Tries to build object obj at location loc
   * @param obj the game object to build
   * @param loc the location to build the object
   * @return if the build was successful
   */
  protected boolean actBuild(GameObject obj, MapLocation loc) {
    //TODO: implement this
    return false;
  }
}
