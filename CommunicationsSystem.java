package team122;
import battlecode.common.*;
import java.util.ArrayList;

/**
 * This class handles receiving communications
 * @author bovard
 */
public class CommunicationsSystem {
  private RobotController robotControl;
  private ArrayList<Message> messages = new ArrayList<Message>();
  private ArrayList<Message> directives = new ArrayList<Message>();
  private int[] filter;
  private int lastMailCheck = -1;

  public CommunicationsSystem(RobotController robotControl) {
    this.robotControl = robotControl;
    this.filter = new int[] {0,0,0};
  }

  /**
   * tells the system what kind of messages to look for, as defined as message types in
   * PlayerConstants. only messages of that type should be stored
   * Filters are binary int arrays (so either 1,0) a 1 in the position of PlayerConstants.MESSAGE_SUBTYPE
   * indicates that it should be accepted, a 0 indicates it should be rejected.
   * @param filter an array of messages to listen for
   */
  public void setFilter(int[] filter) {
    this.filter = filter;
  }

  /**
   * Checks to see if the robot has received any mail this turn, then returns true if there
   * is mail in the queue or directive queue
   * @return if there is mail
   */
  public boolean checkMail() {
    //if we haven't already checked our mail (messages) this turn, check them
    if (lastMailCheck < Clock.getRoundNum()) {
      lastMailCheck = Clock.getRoundNum();

      //grab all messages
      Message[] received = robotControl.getAllMessages();
      Message toCheck;

      //cycle through all the messages
      for (int i=0; i<received.length; i++) {
        //grab a message and decrypt it
        toCheck = decrypt(received[i]);
        //if the message checks out
        if (checkMessage(toCheck)) {
          //see what type of message it is and act accordingly
          switch (toCheck.ints[2]) {
            case PlayerConstants.MESSAGE_INFO:
              if(filter[PlayerConstants.MESSAGE_INFO]==1) {
                messages.add(toCheck);
              }
              break;
            case PlayerConstants.MESSAGE_FIGHT_DIRECTIVE:
              if(filter[PlayerConstants.MESSAGE_FIGHT_DIRECTIVE]==1) {
                directives.add(toCheck);
              }
              break;
            case PlayerConstants.MESSAGE_BUILD_DIRECTIVE:
              if(filter[PlayerConstants.MESSAGE_BUILD_DIRECTIVE]==1) {
                directives.add(toCheck);
              }
              break;
          }
        }
      }
    }
    //then return if we have mail
    return !messages.isEmpty() || !directives.isEmpty();
  }

  /**
   * checks to filter and the last message(s) received to see if the robot has a message directive
   * @return
   */
  public boolean hasDirective() {
    return !directives.isEmpty();
  }

  /**
   * 
   * @param directiveType
   * @return
   */
  public Message getLastDirective(int directiveType) {
    return directives.remove(directives.size()-1);
  }

  /**
   * Gets the maplocation out of a build directive message
   * @see BroadcastSystem.sendBuildDirective()
   * @param buildDirective the buildDirective message
   * @return the MapLocation of the directive
   */
  public MapLocation getMapLocationFromBuildDirective(Message buildDirective) {
    return new MapLocation(buildDirective.ints[5],buildDirective.ints[6]);
  }

  /**
   * Gets the buildOrderID out of a build directive message
   * @see BroadcastSystem.sendBuildDirective()
   * @param buildDirective the buildDirective message
   * @return the int buildOrderID
   */
  public int getBuildOrderIDFromBuildDirective(Message buildDirective) {
    return buildDirective.ints[4];
  }

  /**
   * Gets the maplocation out of a fight directive
   * @see BroadcastSystem.sendFightDirective()
   * @param fightDirective the fightDirective message
   * @return the location to move to attack
   */
  public MapLocation getLocationFromFightDirective(Message fightDirective) {
    return new MapLocation(fightDirective.ints[4], fightDirective.ints[5]);
  }

  /**
   * decrypts an incoming message
   * @see BroadcastSystem
   * @param message the message to decrypt
   * @return the decrypted message
   */
  private Message decrypt(Message message) {
    if(PlayerConstants.ENCRYPTION) {
      
    }
    return message;
  }

  /**
   * checks a message for validity
   * @see BroadcastSystem
   * @param message the message to check
   * @return if the message is a valid message (aka hasn't been tampared with)
   */
  private boolean checkMessage(Message message) {
    //make sure the int field isn't null
    if (message.ints != null) {
      //check to make sure the assurance bit isn't messed with
      if(message.ints[0] == PlayerConstants.ASSURANCE_BIT_0
              || message.ints[0] == PlayerConstants.ASSURANCE_BIT_1
              || message.ints[0] == PlayerConstants.ASSURANCE_BIT_2) {

        //calculate the positional sum
        int sum = 0;
        for (int i = 0; i< message.ints.length-1; i++) {
          sum += message.ints[i]*(i+1);
        }
        //check to see if the positional sum is correct
        if (sum*PlayerConstants.ASSURANCE_FACTOR == message.ints[message.ints.length-1]) {
          return true;
        }
      }
    }
    System.out.println("FAILED!");
    return false;
  }


}
