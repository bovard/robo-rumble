
package team122;
import battlecode.common.*;
import java.util.Random;

/**
 * The NavigationSystem takes care of selecting a move for the robot
 * The NavigationSystem needs to be given a MovementController to issue commands to, it
 * will check to see if the MovementController is idle before issuing any commands
 *
 * Note: we may need to give the NavSys access to the sensory gear
 * So all the sensors will only look for a given type of object, which means we're going
 * to have to do multiple sweeps if we want to get everything? or we can just tell it to do
 * a sweep for type Object, then try casting them out to find everything?
 *
 * Note: this class should only be used when trying to navigate somewhere. Fleeing, combat
 * movement, etc... are not supported here (yet?)
 *
 * @author bovard
 */
public class NavigationSystem {

  protected Random rand = new Random();
  protected MovementController moveControl;
  protected RobotController robotControl;
  protected int mode;
  protected MapLocation destination;
  protected boolean has_destination = false;

  //used in bug movement algorithm
  protected boolean tracking = false;
  protected Direction lastTargetDirection;
  protected boolean trackingRight;

  
  /**
   * Creates a NavigationSystem that attempts pathfinding for mobile robots
   * Note: currently this class only requires that a robot can move, not that it has
   * any sensors
   * @param control the movementController
   */
  public NavigationSystem(MovementController control) {
    moveControl = control;
    robotControl = moveControl.getRC();
    mode = NavigationMode.BUG;
  }

  /**
   * Creates a navSystem with a destination already in mind
   * @param control the MovementController
   * @param destination the MapLocation to be the destination
   */
  public NavigationSystem(MovementController control, MapLocation dest) {
    moveControl = control;
    this.destination = dest;
    has_destination = true;
    robotControl.setIndicatorString(2, "Dest: "+dest.toString());
    mode = NavigationMode.BUG;
  }

  /**
   * Stops the robot tracking and gives it a new destination
   * @param new_dest the new MapLocation to try and move to
   */
  public void setDestination(MapLocation new_dest) {
    destination = new_dest;
    has_destination = true;
    tracking = false;
    robotControl.setIndicatorString(2, "Dest: "+destination.toString());
  }

  /**
   * Returns the map location that the robot is currently trying to move toward
   * @return the MapLocation destination
   */
  public MapLocation getDestination() {
    return destination;
  }

  /**
   * Allows a choice of what navigation mode to use (A*, bug, flock, etc...)
   * Note: currently only bug is implemented
   * @param mode The navigation mode taken from NavigationMode.java
   */
  public void setMode(int mode) {
    this.mode = mode;
  }

  /**
   * Checks to see if the robot is currently at it's destination
   * @return if the robot is at its destination MapLocation
   */
  public boolean isAtDestination() {
    return robotControl.getLocation() == destination;
  }

  /**
   * setNextMove is called to choose and execute the next for a bot.
   * @return if the moveController was 'set' (given a command)
   */
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
    }
    //if the moveController is active or we don't have a destination return false
    return false;
  }


  /**
   * bug runs the bug algorithm. Currently the bug chooses a random direction to trace
   * and can remember the original direction it needed to go to get the target
   * TODO: add a break for when we've been tracing too long
   * TODO: be smarter about what direction to choose to trace
   * Note: currently the bug will fall off of convex curves but will hug concave
   * @return if the moveController was set
   */
  protected boolean bug() {
    try {
      Direction currentDirection = moveControl.getRC().getDirection();
      if (moveControl.getRC().getLocation().equals(destination)) {
        //System.out.println("DESTINATION REACHED!!");
        has_destination = false;
        robotControl.setIndicatorString(2, "No Dest");
        return false;
      }
      //if we're currently tracking
      else if (tracking) {
        //System.out.println("Tracking");

        //check to see if we can move in the direction we were last blocked in and we're
        //off the obstacle
        if (moveControl.canMove(lastTargetDirection) && moveControl.canMove(lastTargetDirection.rotateLeft())
                && moveControl.canMove(lastTargetDirection.rotateRight())) {
          //System.out.println("Done Tracking!");
          tracking = false;
          moveControl.setDirection(lastTargetDirection);
          return true;
        }
        else {
          //System.out.println("Continuing to Track... moving");
          if (trackingRight)
            if (moveControl.canMove(currentDirection.rotateLeft())) {
              moveControl.setDirection(currentDirection.rotateLeft());
              return true;
            }
            else if (moveControl.canMove(currentDirection)) {
              moveControl.moveForward();
              return true;
            }
            else {
              moveControl.setDirection(currentDirection.rotateRight());
              return true;
            }
          else if (!trackingRight)
            if (moveControl.canMove(currentDirection.rotateRight())) {
              moveControl.setDirection(currentDirection.rotateRight());
              return true;
            }
            else if (moveControl.canMove(currentDirection)) {
              moveControl.moveForward();
              return true;
            }
            else {
              moveControl.setDirection(currentDirection.rotateLeft());
              return true;
            }
        }
      }


      else if (!tracking) {
        //System.out.println("Not tracking... moving");
        lastTargetDirection = moveControl.getRC().getLocation().directionTo(destination);
        //if you can move toward the target and you're facing that way move foward
        if (moveControl.canMove(lastTargetDirection) && lastTargetDirection == currentDirection) {
          moveControl.moveForward();
          return true;
        }
        //if you can move toward the target but you aren't facing the right way, rotate
        else if (moveControl.canMove(lastTargetDirection)) {
          moveControl.setDirection(lastTargetDirection);
          return true;
        }
        //otherwise if you can't move toward the target you need to start tracking!
        else {
          //System.out.println("Need to start tracking!");
          tracking = true;
          //choose a direction to track in (by making it random we can avoid (some) loops
          //TODO: Change this to favor the direction that would require the least turning
          //to continue in (so when hitting an object at an angle they would continue
          if (moveControl.canMove(robotControl.getDirection().rotateRight())) {
            trackingRight = true;
          }
          else if (moveControl.canMove(robotControl.getDirection().rotateLeft())) {
            trackingRight = false;
          }
          else {
            trackingRight = rand.nextBoolean();
          }
          //TODO: do we need to make this pass-by-value?
          Direction toMove = lastTargetDirection;
          //a count prevents the robot from turning in circles forever
          int count = 8;
          while(!moveControl.canMove(toMove) && count > 0) {
            if (trackingRight)
              toMove = toMove.rotateRight();
            else
              toMove = toMove.rotateLeft();
            count--;
          }
          //System.out.println("Changing to Direction "+toMove.name()+" and count="+count);
          moveControl.setDirection(toMove);
          return true;
        }
      }
    } catch (Exception e) {
      System.out.println("caught exception:");
      e.printStackTrace();
      return false;
    }
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
   * The A* algorithm, I don't know how well this will work until later in the game
   * as newly created robots can get very little information about the map until they've moved
   * around for a while. It might be worth switching too after the robot has been around
   * for a few hundred turns however
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