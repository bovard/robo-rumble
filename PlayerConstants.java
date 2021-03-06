

package team122;

/**
 * A list of all constants used 
 * @author bovard
 */
public final class PlayerConstants {
  //GLOBAL STRATEGY CONSTANTS
  public static final int LIGHT_RUSH = 1;
  public static final int HEAVY_RUSH = 2;

  //HEAVY RUSH STRATEGY CONSTNATS
  public static final int SCOUT_COOLDOWN = 100;
  public static final int BUILDER_SCOUT_COOLDOWN = 150;
  public static final int HEAVY_COOLDOWN = 100;

  //LIGHT RUSH STRATEGY CONSTANTS
  public static final int START_COMCYCLERS = 500;
  public static final int START_BUILDING_GUARD_TOWERS = 1000;

  //ECONOMY CONSTANTS
  public static final int MINIMUM_FLUX = 3;
  public static final double MINIMUM_FLUX_REGEN = 3.0;


  //SENSOR CONSTANTS
  
  //ranges for sensors
  //orthogonal ranges
  public static final int SIGHT_ORTH_RANGE = 3;
  public static final int SIGHT_ORTH_RANGE_SQUARED = 9;
  public static final int RADAR_ORTH_RANGE = 6;
  public static final int RADAR_ORTH_RANGE_SQUARED = 36;
  public static final int TELESCOPE_ORTH_RANGE = 144;
  public static final int TELESCOPE_ORTH_RANGE_SQUARED = 12;
  public static final int SATELLITE_ORTH_RANGE = 10;
  public static final int SATELLITE_ORTH_RANGE_SQUARED = 100;
  public static final int BUILDING_SENSOR_ORTH_RANGE = 1;
  public static final int BUILDING_SENSOR_ORTH_RANGE_SQUARED = 1;
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
  public static final int NUM_OF_MESSAGE_TYPES = 3;
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
