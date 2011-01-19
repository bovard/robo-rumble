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
  //ECONOMEY CONSTANTS
  public static final int MINIMUM_FLUX = 3;


  //SENSOR CONSTANTS
  
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

  //COMMUNICATION CONSTANTS

  //should we use encryption/decryption?
  public static final boolean ENCRYPTION = false;

  //the types of messages, used to send a receive messenges
  //Note: these have to be in array order as we're using a filter based on these in CommunicationsSystem
  public static final int MESSAGE_INFO = 0;
  public static final int MESSAGE_BUILD_DIRECTIVE = 1;
  public static final int MESSAGE_FIGHT_DIRECTIVE = 2;


  //communications check sum constants
  public static final int ASSURANCE_BIT_0 = 387;
  public static final int ASSURANCE_BIT_1 = 6372;
  public static final int ASSURANCE_BIT_2 = 15;
  public static final int ASSURANCE_FACTOR = 7;
  public static final int MAX_MESSAGE_LIFE = 5;


}
