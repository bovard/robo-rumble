
package team122;
import battlecode.common.*;

/**
 * Navigation with a sensor
 * //TODO: add some navigation methods that take advantage of a sensor here
 * @author bovard
 */
public class SensorNavigationSystem extends NavigationSystem {
  protected SensorSystem sensorSys;

  public SensorNavigationSystem(RobotController robotControl, MovementController moveControl, SensorSystem sensorSys) {
    super(robotControl, moveControl);
    this.sensorSys = sensorSys;

  }

  /**
   * setNextMove is called to choose and execute the next for a bot.
   * @return if the moveController was 'set' (given a command)
   */
  @Override
  public boolean setNextMove() {
    if(has_destination && !moveControl.isActive()) {
      switch(mode) {
        case NavigationMode.A_STAR:
          return a_star();
        case NavigationMode.BUG:
          return bug();
        case NavigationMode.FLOCK:
          return flock();
      }
      System.out.println("Warning: fell through NavigationSystem.setNextMove (bad NavMode)");
    }
    //if the moveController is active or we don't have a destination return false
    System.out.println("WARNING: Bad call to NavSys.setNextMove");
    return false;
  }



  /**
   * The robot tries to flock with other robots around it, mimicing their movement
   * Note: not implemented
   * @return if the moveControl was set
   */
  protected boolean flock() {
    //TODO: Implement this
    //prevents a loop
    System.out.println("WARNING: Tried to use the unimplemented method flock in NavSys.java");
    has_destination = false;
    return false;
  }

  /**
   * The robot tries to follow the leader bot
   * @param bot the robot that this bot should follow
   * @return if the moveControl was set
   */
  protected boolean followLeader(Robot bot) {
    //TODO: Implement this
    //prevents a loop
    System.out.println("WARNING: Tried to use the unimplemented method followLeader in NavSys.java");
    has_destination = false;
    return false;
  }


  /**
   * The A* algorithm, I don't know how well this will work until later in the game
   * as newly created robots can get very little information about the map until they've moved
   * around for a while. It might be worth switching too after the robot has been around
   * for a few hundred turns however. It would also be great for getting to location within sensor
   * range
   * Note: not implemented
   * @return if the moveController was set
   */
  protected boolean a_star() {
    //TODO: Implement this
    //prevents a loop
    System.out.println("WARNING: Tried to use the unimplemented method a_star in NavSys.java");
    has_destination = false;
    return false;
  }
  



}
