

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

  public SensorController sensor;
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
   * sensor based functions
   * @param sensor
   */
  public SensorSystem(SensorController sensor, Team myTeam) {
    this.sensor = sensor;
    this.myTeam = myTeam;
    lastMineScan = -1;
    lastBotScan = -1;
  }

  /**
   * returns the sensorController
   * @return the SensorController
   */
  public SensorController getSensor() {
    return sensor;
  }

  /**
   * returns the nearest enemy robot
   * //TODO: actually make this return the nearest enemy robot instead of the first in the queue
   * @return the nearest enemy robot
   */
  public Robot getNearestOpponent() {
    if (lastBotScan < Clock.getRoundNum()) {
      getBots();
    }
    if (getBots(myTeam.opponent()).length > 0) {
      return getBots(myTeam.opponent())[0];
    }
    return null;
  }

  /**
   * Checks to see if the Mine info is current, if not does a scan for mines
   *
   * Note: since the mines aren't going anywhere, it might be good to not allow a sensor
   * system to 'forget' a mine. It would just test new ones against already known mines
   * and only add in ones that it's never seen. I'm not sure how benficial this would be
   *
   * @return a list of mines that are sensed
   */
  public Mine[] getMines() {
    if(lastMineScan < Clock.getRoundNum()) {
      lastMineScan = Clock.getRoundNum();
      
      mines = sensor.senseNearbyGameObjects(Mine.class);
    }
    return mines;
  }

  /**
   * Checks to see if the Robot sensor info is current, if not does a scan for bots
   * After getting bot info it splits the bots into two different array par team
   *
   * @return an array of sensed bots
   */
  public Robot[] getBots() {
    if (lastBotScan < Clock.getRoundNum()) {
      lastBotScan = Clock.getRoundNum();
      bots = sensor.senseNearbyGameObjects(Robot.class);

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
      getBots();
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
   * Returns the max number of squares a bot can see in the given direction
   * @param dir the direction
   * @return the max distance seen
   */
  public int getRange(Direction dir) {
    //if facing orthogonally
    if (dir == Direction.EAST || dir == Direction.NORTH || dir == Direction.WEST ||
            dir == Direction.SOUTH) {
      if (sensor.type() == ComponentType.SIGHT)
        return PlayerConstants.SIGHT_ORTH_RANGE;
      else if (sensor.type() == ComponentType.RADAR)
        return PlayerConstants.RADAR_ORTH_RANGE;
      else if (sensor.type() == ComponentType.TELESCOPE)
        return PlayerConstants.TELESCOPE_ORTH_RANGE;
      else if (sensor.type() == ComponentType.SATELLITE)
        return PlayerConstants.SATELLITE_ORTH_RANGE;
      else if (sensor.type() == ComponentType.BUILDING_SENSOR)
        return PlayerConstants.BUILDING_SENSOR_ORTH_RANGE;
    }
    //facing diagonally
    else {
      if (sensor.type() == ComponentType.SIGHT)
        return PlayerConstants.SIGHT_DIAG_RANGE;
      else if (sensor.type() == ComponentType.RADAR)
        return PlayerConstants.RADAR_DIAG_RANGE;
      else if (sensor.type() == ComponentType.TELESCOPE)
        return PlayerConstants.TELESCOPE_DIAG_RANGE;
      else if (sensor.type() == ComponentType.SATELLITE)
        return PlayerConstants.SATELLITE_DIAG_RANGE;
      else if (sensor.type() == ComponentType.BUILDING_SENSOR)
        return PlayerConstants.BUILDING_SENSOR_DIAG_RANGE;
    }
    return 0;
  }

  /**
   * returns the amount of turning a robot would need to do to look at the world around it
   * @return the number of portions of the screen the robot needs to look at to see it all
   */
  public int getBreadth() {
    if (sensor.type() == ComponentType.SIGHT)
      return PlayerConstants.SIGHT_TURNS;
    else if (sensor.type() == ComponentType.RADAR)
      return PlayerConstants.RADAR_TURNS;
    else if (sensor.type() == ComponentType.TELESCOPE)
      return PlayerConstants.TELESCOPE_TURNS;
    else if (sensor.type() == ComponentType.SATELLITE)
      return PlayerConstants.SATELLITE_TURNS;
    else if (sensor.type() == ComponentType.BUILDING_SENSOR)
      return PlayerConstants.BUILDING_SENSOR_TURNS;
    else
      System.out.println("Error: called getBreadth() without a sensor");
    return 0;
  }

}
