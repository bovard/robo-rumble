

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

  /**
   * Creates a new SensorSystem, needs a sensorcontroller to function, in charge of
   * sensor based functions
   * @param sensor
   */
  public SensorSystem(SensorController sensor) {
    this.sensor = sensor;
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
      lastMineScan = Clock.getRoundNum();
      bots = sensor.senseNearbyGameObjects(Robot.class);
      this.bots = bots;

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
   * neutral bots
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
    //must be Santa... i mean neutral
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
    //TODO: put these in a Constants file somewhere, hardcoding == terrible
    if (dir == Direction.EAST || dir == Direction.NORTH || dir == Direction.WEST ||
            dir == Direction.SOUTH) {
      if (sensor.type() == ComponentType.SIGHT)
        return 3;
      else if (sensor.type() == ComponentType.RADAR)
        return 6;
      else if (sensor.type() == ComponentType.TELESCOPE)
        return 12;
      else if (sensor.type() == ComponentType.SATELLITE)
        return 10;
      else if (sensor.type() == ComponentType.BUILDING_SENSOR)
        return 1;
    }
    //facing diagonally
    else {
      if (sensor.type() == ComponentType.SIGHT)
        return 2;
      else if (sensor.type() == ComponentType.RADAR)
        return 4;
      else if (sensor.type() == ComponentType.TELESCOPE)
        return 8;
      else if (sensor.type() == ComponentType.SATELLITE)
        return 7;
      else if (sensor.type() == ComponentType.BUILDING_SENSOR)
        return 1;
    }
    return 0;
  }

}
