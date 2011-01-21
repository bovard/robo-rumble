
package team122;

/**
 * Makes different levels for Game Events. A robot at one GameLevel will only respond
 * to GameEvents with a higher (numerically) level
 *
 * @author bovard
 */
public enum GameEventLevel {

  //the most important GameEventLevel should have the highest priority value
  CRITICAL (GameEventLevelPriority.CRITICAL),
  MISSION (GameEventLevelPriority.MISSION),
  NORMAL (GameEventLevelPriority.NORMAL),
  NONE (GameEventLevelPriority.NONE);

  public final int priority;

  GameEventLevel(int priority) {
    this.priority = priority;
  }
}
