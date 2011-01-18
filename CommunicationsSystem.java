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

  public boolean checkMail() {
    //if we haven't already checked our mail (messages) this turn, check them
    if (lastMailCheck < Clock.getRoundNum()) {
      lastMailCheck = Clock.getRoundNum();
      Message[] received = robotControl.getAllMessages();
      Message toCheck;
      for (int i=0; i<received.length; i++) {
        //grab a message and decrypt it
        toCheck = decrypt(received[i]);
        //if the message checks out
        if (checkMessage(toCheck)) {
          //see what type of message it is and act accordingly
          switch (toCheck.ints[1]) {
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
    return null;
  }

  /**
   * decrypts an incoming message
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
   * @param message the message to check
   * @return if the message is a valid message (aka hasn't been tampared with)
   */
  private boolean checkMessage(Message message) {
    //check to make sure the message was sent last round and the TTL isn't too high
    if (message.ints[1] == Clock.getRoundNum() - 1 &&
            message.ints[3] <= PlayerConstants.MAX_MESSAGE_LIFE) {
      //check to make sure the assurance bit isn't messed with
      if(message.ints[0] == PlayerConstants.ASSURANCE_BIT_0
              || message.ints[0] == PlayerConstants.ASSURANCE_BIT_1
              || message.ints[0] == PlayerConstants.ASSURANCE_BIT_2) {
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
    return false;
  }


}
