
package team122;
import battlecode.common.*;
import java.util.Random;

/**
 * The NavigationSystem takes care of selecting a move for the robot
 * The NavigationSystem needs to be given a MovementController to issue commands to, it
 * will check to see if the MovementController is idle before issuing any commands
 *
 * Note: this class shouldn't be doing any checking to see if it actually can set the
 * movementcontroller, but it still does. it will print a warning if it can't
 *
 * Note: this class should only be used when trying to navigate somewhere. Fleeing, combat
 * movement
 *
 * @see SensorNavigationSystem
 * @see JumpNavigationSystem
 * @author bovard
 */
public class NavigationSystem {

  
  protected MovementController moveControl;
  protected RobotController robotControl;
  
  //class variables
  protected Random rand = new Random();
  protected int mode;
  protected MapLocation destination;
  protected boolean has_destination = false;

  //used in bug movement algorithm
  private boolean tracking = false;
  private Direction lastTargetDirection;
  private boolean trackingRight;

  
  /**
   * Creates a NavigationSystem that attempts pathfinding for mobile robots
   * Note: currently this class only requires that a robot can move, not that it has
   * any sensors
   * @param control the movementController
   */
  public NavigationSystem(RobotController robotControl, MovementController control) {
    this.robotControl = robotControl;
    this.moveControl = control;
    
    mode = NavigationMode.BUG;
    //added to make things a bit more random!
    rand.setSeed(Clock.getRoundNum());
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
   * note: this doesn't do much be the overridden version (units with blink) will
   * @return if all movement means are active
   */
  public boolean isActive() {
    return moveControl.isActive();
  }

  /**
   * checks to see if the robot can move in the specified direction
   * @param direction direction to move in
   * @return if the bot can move in that direction
   */
  public boolean canMove(Direction direction) {
    return moveControl.canMove(direction);
  }

  /**
   * sets the moveController to turn
   * Note: the checks here are just as a failsafe, we'll remove them once we're sure that
   * we are using this correctly
   * @param toTurn the direction to turn in 
   * @return if the moveController was set to turn
   */
  public boolean setTurn(Direction toTurn) {
    if(!moveControl.isActive()) {
      if(toTurn != robotControl.getDirection()) {
        try {
          moveControl.setDirection(toTurn);
          return true;
        } catch (Exception e) {
          System.out.println("caught exception:");
          e.printStackTrace();
        }
      }
      System.out.println("WARNING: Bad call to NavSys.setTurn (tried to turn in the direction you were already facing)");
      return false;
    }
    System.out.println("WARNING: Bad call to NavSys.setTurn (moveController was active)");
    return false;
  }

  /**
   * Sets to moveControl to move forward
   * Note: the checks here are just as a failsafe, we'll remove them once we're sure that
   * we are using this correctly
   * @return if the move control was set to move forward
   */
  public boolean setMoveForward() {
    if(!moveControl.isActive()) {
      if(moveControl.canMove(robotControl.getDirection())) {
        try {
          moveControl.moveForward();
          return true;
        } catch (Exception e) {
          System.out.println("caught exception:");
          e.printStackTrace();
        }
      }
      System.out.println("WARNING: Bad call to NavSys.setMoveForward (can't move that way!)");
      return false;
    }
    System.out.println("WARNING: Bad call to NavSys.setMoveForward (moveController active)");
    return false;
  }

  /**
   * Sets to moveControl to move backward
   * Note: the checks here are just as a failsafe, we'll remove them once we're sure that
   * we are using this correctly
   * @return if the move control was set to move backward
   */
  public boolean setMoveBackward() {
    if(!moveControl.isActive()) {
      if(moveControl.canMove(robotControl.getDirection().opposite())) {
        try {
          moveControl.moveBackward();
          return true;
        } catch (Exception e) {
          System.out.println("caught exception:");
          e.printStackTrace();
        }
      }
      System.out.println("WARNING: Bad call to NavSys.setMoveBackward (can't move there!)");
      return false;
    }
    System.out.println("WARNING: Bad call to NavSys.setMoveBackward (movecontrol active)");
    return false;
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
    return robotControl.getLocation().equals(destination);
  }

  /**
   * setNextMove is called to choose and execute the next for a bot.
   * @return if the moveController was 'set' (given a command)
   */
  public boolean setNextMove() {
    if(has_destination && !moveControl.isActive()) {
      switch(mode) {
        case NavigationMode.BUG:
          return bug();
      }
      System.out.println("Warning: fell through NavigationSystem.setNextMove (bad NavMode)");
    }
    //if the moveController is active or we don't have a destination return false
    System.out.println("WARNING: Bad call to NavSys.setNextMove");
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
        System.out.println("WARNING: Bad call to NavSys.setNextMove ->bug (no dest or dest reached)");
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

          //if we can rotate slightly left and/or right
          if (moveControl.canMove(robotControl.getDirection().rotateRight()) 
                  || moveControl.canMove(robotControl.getDirection().rotateLeft()) && rand.nextInt(10) < 8)
          {
            //if canMove right && (random or can't move Left)
            if (moveControl.canMove(robotControl.getDirection().rotateRight())
                    && (rand.nextBoolean() || !moveControl.canMove(robotControl.getDirection().rotateLeft()))) {
              trackingRight = true;
            }
            else {
              trackingRight = false;
            }
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
    }
    System.out.println("WARNING: Bad call to NavSys.setNextMove -> bug (unknown)");
    return false;
  }

}