/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package team122;

/**
 * A list of all constants used 
 * @author bovard
 */
public final class PlayerConstants {

  //ranges for sensors
  //orthogonal ranges
  public static final int SIGHT_ORTH_RANGE = 3;
  public static final int RADAR_ORTH_RANGE = 6;
  public static final int TELESCOPE_ORTH_RANGE = 12;
  public static final int SATELLITE_ORTH_RANGE = 10;
  public static final int BUILDING_SENSOR_ORTH_RANGE = 1;
  //diagonal ranges
  public static final int SIGHT_DIAG_RANGE = 2;
  public static final int RADAR_DIAG_RANGE = 4;
  public static final int TELESCOPE_DIAG_RANGE = 8;
  public static final int SATELLITE_DIAG_RANGE = 7;
  public static final int BUILDING_SENSOR_DIAG_RANGE = 1;

  // the number of slices it takes for a sensor to see 360 degrees
  public static final int SIGHT_TURNS = 4;
  public static final int RADAR_TURNS = 2;
  public static final int TELESCOPE_TURNS = 8;
  public static final int SATELLITE_TURNS = 1;
  public static final int BUILDING_SENSOR_TURNS = 1;

  //the types of messages, used to send a receive messenges
  public static final int MESSAGE_INFO = 0;
  public static final int MESSAGE_BUILD_DIRECTIVE = 1;
  public static final int MESSAGE_FIGHT_DIRECTIVE = 2;
  

}
