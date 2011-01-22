
package team122;
import battlecode.common.*;
import java.util.ArrayList;

/**
 * The behavioral logic for BuilderScouts.
 * @see BuilderSensorRobotSystem
 * @author bovard
 */
public class RSBuilderScout extends BuilderSensorRobotSystem {
  protected ArrayList<Mine> uncoveredMines;
  protected Mine currentMine;

  public RSBuilderScout(RobotController robotControl, SensorSystem sensorSys, BuilderSystem buildSys) {
    super(robotControl, sensorSys, buildSys);
    uncoveredMines = new ArrayList<Mine>();
    gameEvents = new RSBuilderScoutGE(robotControl, comSys, sensorSys, this);
  }

  /**
   * Main loop for the BuilderScout, should never retern anything
   */
  @Override
  public void go() {
    // hack that ensures that we'll point at our mines before waking up our first bot
    // (so we can find the other two mines right off the bat)
    if (Clock.getRoundNum() < 20)
    {
      yield();
      yield();
      try {
        while(sensorSys.senseObjectAtLocation(robotControl.getLocation().add(robotControl.getDirection()), RobotLevel.ON_GROUND) == null
                || !(robotControl.getDirection() == Direction.NORTH || robotControl.getDirection() == Direction.EAST ||
                robotControl.getDirection() == Direction.SOUTH || robotControl.getDirection() == Direction.WEST)) {
          while(navSys.isActive()) {
            yield();
          }
          actTurn(robotControl.getDirection().rotateRight());
          while(navSys.isActive()) {
            yield();
          }
        }
        //System.out.println("Direction is "+robotControl.getDirection());
        //System.out.println("Sensed: "+sensorControl.senseObjectAtLocation(robotControl.getLocation().add(robotControl.getDirection()), RobotLevel.ON_GROUND));
      } catch (Exception e) {
        System.out.println("caught exception:");
        e.printStackTrace();
      }
    }

    while(true) {

      //if we have a combat game event, run away!
      if(gameEvents.checkGameEventsAbovePriority(GameEventLevel.MISSION.priority)) {
        currentGameEventLevel = GameEventLevel.COMBAT;
        seqFlee();
      }
      //if we received a directive, build it!
      else if(((SensorGameEvents)gameEvents).hasDirective()) {
        currentGameEventLevel = GameEventLevel.MISSION;
        Message buildDirective = comSys.getLastDirective(PlayerConstants.MESSAGE_BUILD_DIRECTIVE);
        BuildOrder toBuild = BuildOrderID.getBuildOrderFromID(comSys.getBuildOrderIDFromBuildDirective(buildDirective));
        seqBuildAtLocation(toBuild, comSys.getMapLocationFromBuildDirective(buildDirective));
        yield();
      }
      //if we still know where some uncovered mines are, go to them and build something there
      else if(!uncoveredMines.isEmpty()) {
        currentGameEventLevel = GameEventLevel.MISSION;
        //if we build a recycler on it
        currentMine = uncoveredMines.get(0);
        if(seqBuildAtLocation(BuildOrder.RECYCLER, currentMine.getLocation())) {
          //remove it from the queue
          while(navSys.isActive()) {
            yield();
          }
          navSys.setTurn(robotControl.getLocation().directionTo(currentMine.getLocation()));
          uncoveredMines.remove(currentMine);
          currentMine = null;

          yield();
        }
      }
      //otherwise look for mines
      else {
        currentGameEventLevel = GameEventLevel.NORMAL;
        seqScout();
      }
    }
  }

  /**
   * checks to see if there are uncovered mines left to find
   * @return if there are any uncoverd mines in teh queue
   */
  public boolean hasUncoveredMines() {
    return !uncoveredMines.isEmpty();
  }

  /**
   * Detects if there is an uncovered mine within sensor range and adds it to the uncoverdMines
   * list if it isn't already there
   * @return If an uncovered mine was found within sensor range
   */
  protected void trackUncoveredMines() {
    if (((SensorGameEvents)gameEvents).canSeeMine()) {
      Mine[] mines = sensorSys.getMines();

      for (int i=0; i < mines.length; i++) {
        //check to see if these is anything built on the mine
        if(sensorSys.senseObjectAtLocation(mines[i].getLocation(), RobotLevel.ON_GROUND)==null) {
          //if this is a newly uncovered mine
          if (!uncoveredMines.contains(mines[i])) {
            uncoveredMines.add(mines[i]);
          }
        }
        //if there is soemthing there, remoe it from the list if it was on the list
        else {
          if(uncoveredMines.contains(mines[i])) {
            uncoveredMines.remove(mines[i]);
          }
        }
      }
    }
  }

  /**
   * Overridden actMove for the BuildScout, needed to add in uncoverdMineTracking
   * @return if the move was successful
   */
  @Override
  protected boolean actMove() {
    trackUncoveredMines();
    return super.actMove();
  }


  /**
   * Couples the yield function with game events, resetting the game events just before
   * calling yield and calculating them again as the first action for the robot
   */
  @Override
  protected void yield() {
    super.yield();
    robotControl.setIndicatorString(0, "ID: " + robotControl.getRobot().getID() + " - Location: "+robotControl.getLocation().toString() + " with "+uncoveredMines.size()+" mines left");
  }


}