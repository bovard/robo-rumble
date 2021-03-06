

package team122;
import battlecode.common.*;

/**
 * Interfaces with the Sensor system to gather and store data about the outside world.
 * Using this as a wrapper over a SensorController I think is worth it because it will
 * stop redundant calls to sense objects, and could potentially be used to combine inputs
 * from multiple sensors (if we want to put multiple sensors on one bot aka telescope)
 *
 * Note: multiple sensors aren't supported atm
 * TODO: Support multiple sensors?
 * @author bovard
 */
public class SensorSystem {

  protected SensorController sensorControl;
  protected RobotController robotControl;

  //class variables
  private int lastMineScan;
  private Mine[] mines;
  private int lastBotScan;
  private Robot[] bots;
  private Robot[] aBots;
  private Robot[] bBots;
  private Robot[] neutralBots;
  private Team myTeam;



  /**
   * Creates a new SensorSystem, needs a sensorcontroller to function, in charge of
   * sensorControl based functions
   * @param sensorControl
   */
  public SensorSystem(RobotController robotControl, SensorController sensor) {
    this.sensorControl = sensor;
    this.robotControl = robotControl;
    this.myTeam = robotControl.getTeam();
    lastMineScan = -1;
    lastBotScan = -1;
  }

  /**
   * returns the sensorController
   * @return the SensorController
   */
  public SensorController getSensor() {
    return sensorControl;
  }

  public MapLocation senseLocationOfObject(GameObject go) {
    try {
      return sensorControl.senseLocationOf(go);
    } catch (Exception e) {
      System.out.println("caught exception:");
      e.printStackTrace();
    }
    System.out.println("WARNING: could not sense location of "+go.toString());
    return null;
  }

  public RobotInfo sensorRobotInfo(Robot bot) {
    try {
      return sensorControl.senseRobotInfo(bot);
    } catch (Exception e) {
      System.out.println("caught exception:");
      e.printStackTrace();
    }
    System.out.println("WARNING: tried to sense RobotInfo but couldn't of Robot "+bot.toString());
    return null;
  }

  /**
   * checks to see if we can sense a map location
   * @param loc the MapLocation to sense
   * @return if it can be sensed
   */
  public boolean canSenseLocation(MapLocation loc) {
    return sensorControl.canSenseSquare(loc);
  }

  /**
   * checks to see if we can sense a game object
   * @param obj the object to sense
   * @return if it can be sensed
   */
  public boolean canSenseObject(GameObject obj) {
    return sensorControl.canSenseObject(obj);
  }

  public GameObject senseObjectAtLocation(MapLocation loc, RobotLevel level) {
    try {
      return sensorControl.senseObjectAtLocation(loc, level);
    } catch (Exception e) {
      System.out.println("caught exception:");
      e.printStackTrace();
    }
    System.out.println("WARNING: Tried to senseobjectatloction and couldn't! SensorSystem.senseObjectAtLocation");
    return null;
  }

  /**
   * Finds the clostest unoccupied (at the calling robot's level) square adjacent to the
   * target location (doesn't check to see if you are at the target location)
   * @param target the location to find the adjacent square to
   * @return the free MapLocation, null if none was found
   */
  public MapLocation findClosestUnoccupiedAdjacentSquareToLocation(MapLocation target) {
    Direction dir = robotControl.getLocation().directionTo(target).opposite();
    try {
      //tries the closest square (check that it can be sensed, that it is land, and that it's free)
      Direction toTry = dir;
      if (sensorControl.canSenseSquare(target.add(toTry))
              && ((robotControl.getChassis().isAirborne() && robotControl.senseTerrainTile(target.add(toTry)) != TerrainTile.OFF_MAP) || robotControl.senseTerrainTile(target.add(toTry)) == TerrainTile.LAND)
              && sensorControl.senseObjectAtLocation(target.add(toTry), robotControl.getRobot().getRobotLevel()) == null) {
        return target.add(toTry);
      }
      //try the alternating left and right rotations from the clostest direction
      for(int i=1; i<=3; i++) {
        toTry = dir;
        for (int j=1; j<=i; j++) {
          toTry = toTry.rotateLeft();
        }
        if (sensorControl.canSenseSquare(target.add(toTry))
              && ((robotControl.getChassis().isAirborne() && robotControl.senseTerrainTile(target.add(toTry)) != TerrainTile.OFF_MAP) || robotControl.senseTerrainTile(target.add(toTry)) == TerrainTile.LAND)
              && sensorControl.senseObjectAtLocation(target.add(toTry), robotControl.getRobot().getRobotLevel()) == null) {
          return target.add(toTry);
        }
        toTry = dir;
        for (int j=1; j<=i; j++) {
          toTry = toTry.rotateRight();
        }
        if (sensorControl.canSenseSquare(target.add(toTry))
              && ((robotControl.getChassis().isAirborne() && robotControl.senseTerrainTile(target.add(toTry)) != TerrainTile.OFF_MAP) || robotControl.senseTerrainTile(target.add(toTry)) == TerrainTile.LAND)
              && sensorControl.senseObjectAtLocation(target.add(toTry), robotControl.getRobot().getRobotLevel()) == null) {
          return target.add(toTry);
        }
      }
      //try the furthest square
      toTry = dir.opposite();
      if (sensorControl.canSenseSquare(target.add(toTry))
              && ((robotControl.getChassis().isAirborne() && robotControl.senseTerrainTile(target.add(toTry)) != TerrainTile.OFF_MAP) || robotControl.senseTerrainTile(target.add(toTry)) == TerrainTile.LAND)
              && sensorControl.senseObjectAtLocation(target.add(toTry), robotControl.getRobot().getRobotLevel()) == null) {
        return target.add(toTry);
      }
    } catch (Exception e) {
      System.out.println("caught exception:");
      e.printStackTrace();
    }
    //if we didn't find a square, return null;
    return null;
  }

  /**
   * returns the nearest enemy robot
   * Note: this may be pretty bytecode expensive, for each enemy robot in sight you need about
   * 40 bytecodes
   * @return the nearest enemy robot
   */
  public Robot getNearestOpponent() {
    if (lastBotScan < Clock.getRoundNum()) {
      getBots();
    }
    int minDist = Integer.MAX_VALUE;
    Robot bot = null;
    MapLocation ourLoc = robotControl.getLocation();
    for (int i=0; i < getBots(myTeam.opponent()).length;i++) {
      try {
        int dist = sensorControl.senseLocationOf(getBots(myTeam.opponent())[i]).distanceSquaredTo(ourLoc);
        if(dist < minDist) {
          minDist = dist;
          bot = getBots(myTeam.opponent())[i];
        }
      }
      catch (Exception e) {
        System.out.println("caught exception:");
        e.printStackTrace();
      }
    }
    return bot;
  }

  /**
   * returns the MapLocation of the nearest enemy
   * bytecost cost: 45ish
   * @return
   */
  public MapLocation getNearestOpponentLocation() {
    Robot enemy = getNearestOpponent();
    if (enemy != null) {
      if (sensorControl.canSenseObject(enemy)) {
        try {
          return sensorControl.senseLocationOf(enemy);
        } catch (Exception e) {
          System.out.println("caught exception:");
          e.printStackTrace();
        }
      }
    }
    System.out.println("WARNING: Tried to find the nearest bot but no bots were found! (or unkown error)");
    return null;
  }

  /**
   * Checks to see if the Mine info is current, if not does a scan for mines
   *
   * Note: since the mines aren't going anywhere, it might be good to not allow a sensorControl
   * system to 'forget' a mine. It would just test new ones against already known mines
   * and only add in ones that it's never seen. I'm not sure how benficial this would be
   *
   * @return a list of mines that are sensed
   */
  public Mine[] getMines() {
    if(lastMineScan < Clock.getRoundNum()) {
      lastMineScan = Clock.getRoundNum();
      
      mines = sensorControl.senseNearbyGameObjects(Mine.class);
    }
    return mines;
  }

  /**
   * Re-Scans an area for Robots
   */
  public void reScanForBots() {
    lastBotScan = Clock.getRoundNum();
    bots = sensorControl.senseNearbyGameObjects(Robot.class);

    int aCount = 0;
    int bCount = 0;
    for (int i=0;i<bots.length;i++) {
      if(bots[i].getTeam()==Team.A)
        aCount++;
      else if (bots[i].getTeam()==Team.B)
        bCount++;
    }

    //prepping to divide the bots into three arrays by team
    aBots = new Robot[aCount];
    bBots = new Robot[bCount];
    neutralBots = new Robot[bots.length-aCount-bCount];


    aCount=0;
    bCount=0;
    int neutralCount=0;
    for (int i=0;i<bots.length;i++) {
      //if the bot comes from team A
      if(bots[i].getTeam()==Team.A) {
        aBots[aCount]=bots[i];
        aCount++;
      }
      //if the bot comes from team B
      else if (bots[i].getTeam()==Team.B) {
        bBots[bCount]=bots[i];
        bCount++;
      }
      //if the bots comes from team neutral
      else {
        neutralBots[neutralCount]=bots[i];
        neutralCount++;
      }
    }
  }

  /**
   * Checks to see if the Robot sensorControl info is current, if not does a scan for bots
   * After getting bot info it splits the bots into two different array par team
   *
   * @return an array of sensed bots
   */
  public Robot[] getBots() {
    if (lastBotScan < Clock.getRoundNum()) {
      reScanForBots();
    }

    return bots;
  }

  /**
   * returns all sensed bots from Team team, refreshing sensors if needed, supports finding
   * neutral bots (debris)
   *
   * @param team - team of the robots to send back
   * @return an array or Robot objects that are on team team
   */
  public Robot[] getBots(Team team) {
    if (lastBotScan < Clock.getRoundNum()) {
      reScanForBots();
    }
    if (team == Team.A)
      return aBots;
    else if (team == Team.B)
      return bBots;
    //must be neutral
    else
      return neutralBots;
  }

  /**
   * Returns the range squared of the sensor squared (as in PlaycerConstants)
   * @return the range squared of the sensor
   */
  public int getRangeSquared() {
    if (sensorControl.type() == ComponentType.SIGHT)
      return PlayerConstants.SIGHT_ORTH_RANGE_SQUARED;
    else if (sensorControl.type() == ComponentType.RADAR)
      return PlayerConstants.RADAR_ORTH_RANGE_SQUARED;
    else if (sensorControl.type() == ComponentType.TELESCOPE)
      return PlayerConstants.TELESCOPE_ORTH_RANGE_SQUARED;
    else if (sensorControl.type() == ComponentType.SATELLITE)
      return PlayerConstants.SATELLITE_ORTH_RANGE_SQUARED;
    else if (sensorControl.type() == ComponentType.BUILDING_SENSOR)
      return PlayerConstants.BUILDING_SENSOR_ORTH_RANGE_SQUARED;
    System.out.println("WARNING: Fell through sensorSystem.getRange() (bad logic)");
    return 0;
  }


  /**
   * Returns the max number of squares a bot can see in the given direction
   * @param dir the direction
   * @return the max distance seen
   */
  public int getRange(Direction dir) {
    //if facing orthogonally
    if (dir == Direction.EAST || dir == Direction.NORTH || dir == Direction.WEST ||
            dir == Direction.SOUTH) {
      if (sensorControl.type() == ComponentType.SIGHT)
        return PlayerConstants.SIGHT_ORTH_RANGE;
      else if (sensorControl.type() == ComponentType.RADAR)
        return PlayerConstants.RADAR_ORTH_RANGE;
      else if (sensorControl.type() == ComponentType.TELESCOPE)
        return PlayerConstants.TELESCOPE_ORTH_RANGE;
      else if (sensorControl.type() == ComponentType.SATELLITE)
        return PlayerConstants.SATELLITE_ORTH_RANGE;
      else if (sensorControl.type() == ComponentType.BUILDING_SENSOR)
        return PlayerConstants.BUILDING_SENSOR_ORTH_RANGE;
    }
    //facing diagonally
    else {
      if (sensorControl.type() == ComponentType.SIGHT)
        return PlayerConstants.SIGHT_DIAG_RANGE;
      else if (sensorControl.type() == ComponentType.RADAR)
        return PlayerConstants.RADAR_DIAG_RANGE;
      else if (sensorControl.type() == ComponentType.TELESCOPE)
        return PlayerConstants.TELESCOPE_DIAG_RANGE;
      else if (sensorControl.type() == ComponentType.SATELLITE)
        return PlayerConstants.SATELLITE_DIAG_RANGE;
      else if (sensorControl.type() == ComponentType.BUILDING_SENSOR)
        return PlayerConstants.BUILDING_SENSOR_DIAG_RANGE;
    }
    System.out.println("WARNING: Fell through sensorSystem.getRange() (bad logic)");
    return 0;
  }

  /**
   * returns the amount of turning a robot would need to do to look at the world around it
   * @return the number of portions of the screen the robot needs to look at to see it all
   */
  public int getBreadth() {
    if (sensorControl.type() == ComponentType.SIGHT)
      return PlayerConstants.SIGHT_TURNS;
    else if (sensorControl.type() == ComponentType.RADAR)
      return PlayerConstants.RADAR_TURNS;
    else if (sensorControl.type() == ComponentType.TELESCOPE)
      return PlayerConstants.TELESCOPE_TURNS;
    else if (sensorControl.type() == ComponentType.SATELLITE)
      return PlayerConstants.SATELLITE_TURNS;
    else if (sensorControl.type() == ComponentType.BUILDING_SENSOR)
      return PlayerConstants.BUILDING_SENSOR_TURNS;
    else
      System.out.println("Error: called getBreadth() without a sensor");
    return 0;
  }

}
