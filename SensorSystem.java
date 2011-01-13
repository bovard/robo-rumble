/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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

  public SensorSystem(SensorController sensor) {
    this.sensor = sensor;
    lastMineScan = -1;
    lastBotScan = -1;
  }

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
   * @return a list of bots that are sensed
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

      aBots = new Robot[aCount];
      bBots = new Robot[bCount];

      //System.out.println("We found "+aCount+" team A bots and "+bCount+" team B bots");
      
      aCount=0;
      bCount=0;
      for (int i=0;i<bots.length;i++) {
        if(bots[i].getTeam()==Team.A) {
          aBots[aCount]=bots[i];
          aCount++;
        }
        else if (bots[i].getTeam()==Team.B) {
          bBots[bCount]=bots[i];
          bCount++;
        }
      }

    }

    return bots;
  }

  /**
   * returns all sensed bots from Team team, refreshing sensors if needed
   * Note: Don't call this with neutral
   * //TODO: support calling from neutral?
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
    else
      System.out.println("NOT A GOOD IDEA!! (called getBots with 'neutral' as the team");
    return bots;
  }

}
