

package team122;
import battlecode.common.*;

/**
 * this controls our scout fighters, they have the build lightmotor, radar, blaster, shield
 * //TODO: flesh this class out
 * @author bovard
 */
public class FighterScoutRobotSystem extends SensorRobotSystem {
  protected WeaponSystem weaponSys;
  protected WeaponController weaponControl;


  public FighterScoutRobotSystem(RobotController robotControl) {
    super(robotControl);
    robotControl.setIndicatorString(0,"FighterScoutConstructor");
    weaponControl = (WeaponController)robotControl.components()[2];
    WeaponController[] weapons = new WeaponController[1];
    weapons[0] = weaponControl;
    weaponSys = new WeaponSystem(weapons, sensorSys);

  }

  public void go() {
    //TODO: Implement this!
    robotControl.setIndicatorString(0, "FighterScout");
    while(true) {
      selFightNScout();
      yield();
    }
  }

  /**
   * the basic selector for the fighter class. decides when to patrol and when to engage
   * @return if one action was performed sucessfully
   */
  public boolean selFightNScout() {
    //TODO: when a fighter sees an enemy or is shot he should engage with the enemy
    navSys.setDestination(chooseNextDestination());
    return seqMove();
  }

   /**
   * Called to move once (and yield) Assumes the robot already has a destination
   * Overridded to provide for weapons capabilities
   * @return if robot is in its current destination
   */
  @Override
  protected boolean actMove() {
    if(navSys.getDestination()==null)
      return false;
    boolean done = navSys.nextMove();
    weaponSys.fire();
    yield();
    //check for map boundary conditions
    updateMapExtrema();
    //update your position for the GUI
    robotControl.setIndicatorString(0, robotControl.getLocation().toString());
    done = !checkDestinationValidity() || done;
    return done;
  }

}
