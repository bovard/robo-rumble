
package team122;

/**
 * The interface for all GameEvent classes, all need to following four functions
 * @author bovard
 */
public interface GameEventsInterface {
  public void resetGameEvents();
  public boolean calcGameEvents();
  public boolean checkGameEvents();
  public boolean checkCriticalGameEvents();

}
