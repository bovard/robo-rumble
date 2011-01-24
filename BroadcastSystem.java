package team122;
import battlecode.common.*;
import java.util.Random;

/**
 * System used to send messages
 * the int[] of messages are reserved as follows:
 * 0: reserved for checksum
 * 1: round sent
 * 2: message type
 * 3: TTL - a TTL greater than zero will be rebroadcast if able
 * last: reserved for checksum
 * @author bovard
 */
public class BroadcastSystem {
  protected RobotController robotControl;
  protected BroadcastController broadcastControl;
  protected Random rand = new Random();

  public BroadcastSystem(RobotController robotControl, BroadcastController broadcastControl) {
    this.robotControl = robotControl;
    this.broadcastControl = broadcastControl;
    
  }

  /**
   * //TODO: Implement this, see it's corresponding decrypt method in CommunicationsSystem
   * @param toEncrypt
   * @return
   */
  private Message encrypt(Message toEncrypt) {
    //takes the structure and encrypts it
    if(PlayerConstants.ENCRYPTION) {

    }
    return toEncrypt;
  }

  /**
   * adds a checksum to the first and last positions of the int array in any message
   * @param message the message to add checksums to
   * @return the checked message
   */
  private Message addCheckSums(Message message) {
    //takes a message and adds checks

    //add a random assurance bit in position 0
    switch(rand.nextInt(3)) {
      case 0:
        message.ints[0] = PlayerConstants.ASSURANCE_BIT_0;
        break;
      case 1:
        message.ints[1] = PlayerConstants.ASSURANCE_BIT_1;
        break;
      case 2:
        message.ints[1] = PlayerConstants.ASSURANCE_BIT_2;
        break;
    }

    //calculate the positional sum
    int sum = 0;
    for (int i=0; i<message.ints.length-1; i++) {
      sum += message.ints[i] * (i+1);
      //note: adding a multiplication of the place of the number prevents swapping array elements
    }

    //set the last int place equal to the positional sum
    message.ints[message.ints.length-1] = sum * PlayerConstants.ASSURANCE_FACTOR;
    return message;
  }

  /**
   * Encrypts and sends a message
   * @param ints the int array to send
   * @param locs the location array to send
   * @param strings the string array to send
   * @return if the message was sent successfully
   */
  private boolean setSendMessage(Message message) {
    if(broadcastControl.isActive()) {
      return false;
    }
    else {
      try {
        broadcastControl.broadcast(message);
        return true;
      } catch (Exception e) {
        System.out.println("caught exception:");
        e.printStackTrace();
        return false;
      }
    }
  }

  /**
   * takes a message and rebroadcasts it, mutating it first (used to trick/confuse the enemy)
   * //TODO: implement this
   * @param message the message to mutate
   * @return if it was sent successfully
   */
  public boolean mutateAndRebroadcast(Message message) {
    //change some stuff in the message
    return setSendMessage(message);
  }

  /**
   * takes one of our messages and rebroadcasts it, making adjustments to it as necessary
   * //TODO: Implement this
   * @param message The message to send
   * @return if it was rebroadcast sucessfully
   */
  public boolean rebraodcast(Message message) {
    //note: this assumes that we've already checked to makes sure it's our message
    //and that message is not encrypted
    //deincrement ttl, etc...
    return setSendMessage(message);
  }

  /**
   * sends a build directive, receiving units should build a structure specified by 
   * buildOrderID at MapLocation location
   * Note: zeroeth and last positions are reserved for the addCheckSums function, the first position
   * is always the round number, the second is the message type, third is the time to live (TTL)
   * @param buildOrderID teh BuildOrderID from RobotBuildOrder of the thing to make
   * @param location the location to build
   * @return if the message was sent successfully
   */
  public boolean setSendBuildDirective(int buildOrderID, MapLocation location) {
    Message toSend = new Message();
    int[] intArray = {0, Clock.getRoundNum(), PlayerConstants.MESSAGE_BUILD_DIRECTIVE, 0, 
                          buildOrderID, location.x, location.y, 0};
    toSend.ints = intArray;
    return setSendMessage(encrypt(addCheckSums(toSend)));
  }

  /**
   * sends a fight directive, receiving units should move to engage enemies at location
   * Note: zeroeth and last positions are reserved for the addCheckSums function, the first position
   * is always the round number, the second is the message type, third is the time to live (TTL)
   * @param location the location to move to attack
   * @return if the message was sent successfully
   */
  public boolean setSendFightDirective(MapLocation location) {
    Message toSend = new Message();
    int[] intArray = {0, Clock.getRoundNum(), PlayerConstants.MESSAGE_FIGHT_DIRECTIVE, 0,
                          location.x, location.y, 0};
    toSend.ints = intArray;
    return setSendMessage(encrypt(addCheckSums(toSend)));
  }

}
