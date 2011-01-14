
package team122;
import battlecode.common.*;

/**
 *
 * @author bovard
 */
public class BuilderScoutRobotSystem extends ScoutRobotSystem {
  protected BuilderController buildControl;
  protected BuilderSystem buildSys;
  protected MapLocation uncoveredMineLoc;

  public BuilderScoutRobotSystem(RobotController robotControl) {

    super(robotControl);
    robotControl.setIndicatorString(0,"BuilderScoutConstructor");

    //on scouts, build systems should be in component[2], added another if clause to catch
    //our starting bot, who has it in components[1]
    if(robotControl.components()[1].type() == ComponentType.CONSTRUCTOR)
      buildControl = (BuilderController)robotControl.components()[1];
    else
      buildControl = (BuilderController)robotControl.components()[2];
    buildSys = new BuilderSystem(robotControl, buildControl);

  }

  public void go() {
    robotControl.setIndicatorString(0,"BuilderScout");
    while(true) {
      selScout();
    }
  }

  /**
   * the bot attempts to perform the ScoutBuild sequence, if it can't it tries to flee
   * @return if either action was sucessfull
   */
  protected boolean selScout() {
    robotControl.setIndicatorString(1, "selScout");
    if(seqScoutBuild())
      return true;
    return actFlee();
  }

  /**
   * sends the robot scouting to find an uncovered mine which it will build a recycler on
   * @return if this was performed successfully
   */
  protected boolean seqScoutBuild() {
    robotControl.setIndicatorString(1, "selScoutBuild");
    //move around until you find an uncoverd mine
    if (seqScoutUncoveredMine()) {
      //try to build a recycler on that mine
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
    robotControl.setIndicatorString(1, "selBuildRecycler");
    // find a free square adjacent to target mine and move there
    if (seqApproachLocation(uncoveredMineLoc, robotControl.getRobot().getRobotLevel())) {
      //wait till we have enough money
      while(robotControl.getTeamResources() < MINIMUM + RobotBuildOrder.RECYCLER_COST) {
        yield();
      }
      //build the recycler
      if (buildSys.seqBuild(RobotBuildOrder.RECYCLER, uncoveredMineLoc)) {
        return true;
      }
    }
    return false;
  }
  

    /**
   * sends the bot to find an uncovered mine, when one is sensed, returns true and also
   * set unCoveredMineLoc to the location of the uncovered mine.
   * returns false if scouting is stopped for any other reason
   * @return if an uncovered mine was found
   */
  protected boolean seqScoutUncoveredMine() {
    robotControl.setIndicatorString(1, "seqScoutUncoveredMine");
    navSys.setDestination(chooseNextDestination());
    boolean done = false;

    //while we haven't found an uncovered mine and we aren't at our destination
    while(!done && !actMove()) {

      Mine[] mines = sensorSys.getMines();
      int i = 0;
      while (i < mines.length && !done) {
        try {
          if(sensorControl.senseObjectAtLocation(mines[i].getLocation(), RobotLevel.ON_GROUND)==null) {
            uncoveredMineLoc = mines[i].getLocation();
            done = true;
          }
            
        } catch (Exception e) {
          System.out.println("caught exception:");
          e.printStackTrace();
        }
      }
      return done;
    }
    return false;
  }


}
