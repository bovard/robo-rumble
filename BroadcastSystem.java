package team122;
import battlecode.common.*;

/**
 * System used to send messages
 * @author bovard
 */
public class BroadcastSystem {
  protected RobotController robotControl;
  protected BroadcastController broadcastControl;

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
    return toEncrypt;
  }

  /**
   * //TODO: implement this, see CommunicationsSystem.checkMessage()
   * @param message the message to add checksums to
   * @return the checked message
   */
  private Message addCheckSums(Message message) {
    //takes a message and adds checks
    return message;
  }

  /**
   * Encrypts and sends a message
   * @param ints the int array to send
   * @param locs the location array to send
   * @param strings the string array to send
   * @return if the message was sent successfully
   */
  private boolean sendMessage(Message message) {
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
    return sendMessage(message);
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
    return sendMessage(message);
  }

}
