package team122;
import battlecode.common.*;
import java.util.ArrayList;

/**
 * This class handles receiving communications
 * @author bovard
 */
public class CommunicationsSystem {
  private RobotController robotControl;
  private ArrayList<Message> oldMessages;
  private int[] filter;

  public CommunicationsSystem(RobotController robotControl) {
    this.robotControl = robotControl;
  }

  /**
   * tells the system what kind of messages to look for, as defined as message types in
   * PlayerConstants. only messages of that type should be stored
   * @param filter an array of messages to listen for
   */
  public void setFilter(int[] filter) {
    this.filter = filter;
  }

  /**
   * checks to filter and the last message(s) received to see if the robot has a message directive
   * @return
   */
  public boolean hasDirective() {
    return false;
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
    return message;
  }

  /**
   * checks a message for validity
   * @param message the message to check
   * @return if the message is a valid message (aka hasn't been tampared with)
   */
  private boolean checkMessage(Message message) {
    return true;
  }


}
