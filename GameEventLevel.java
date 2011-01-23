
package team122;

/**
 * Makes different levels for Game Events. A robot at one GameLevel will only respond
 * to GameEvents with a higher (numerically) level
 *
 * @author bovard
 */
public enum GameEventLevel {

  //the most important GameEventLevel should have the highest priority value
  //check the priority levels in GameEventLevelPriority.java
  /*
   * The COMBAT GameEventLevel is the highest and is used when the robot is in combat,
   * it can't be interrupted by anything
   */
  COMBAT (GameEventLevelPriority.COMBAT),
  /*
   * The DIRECTIVE GameEventLevel is used for when a robot receives a directive, it will take
   * precendence over mission oriented activities
   */
  DIRECTIVE (GameEventLevelPriority.DIRECTIVE),
  /*
   * the MISSION GameEventLevel is used for when the robot is doing thing directly related to
   * its mission (aka building a recycler on an uncovered mine for a BuilderScout)
   */
  MISSION (GameEventLevelPriority.MISSION),
  /*
   * The NORMAL GameEventLevel is used for when the robot is doing a normal task
   */
  NORMAL (GameEventLevelPriority.NORMAL),
  /*
   * The LOW gameEventLevel is used for when the robot is idle, usually scouting
   */
  LOW (GameEventLevelPriority.LOW),
  /*
   * The NONE GameEventLevel should be used for tasks that have no priority. Not even sure if we should have this level...
   */
  NONE (GameEventLevelPriority.NONE);

  public final int priority;

  GameEventLevel(int priority) {
    this.priority = priority;
  }
}
