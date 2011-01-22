package team122;

/**
 * Keeps track of the BuildOrder IDs and allows finding a build order through its id
 * @author bovard
 */
public class BuildOrderID {
  public static final int FIGHTER_SCOUT_1 = 1;
  public static final int FIGHTER_SCOUT_2 = 2;
  public static final int FIGHTER_SCOUT_3 = 3;
  public static final int FIGHTER_SCOUT_4 = 4;
  public static final int FIGHTER_SCOUT_5 = 5;
  public static final int FIGHTER_SCOUT_6 = 6;
  public static final int BUILDER_SCOUT_1 = 10;
  public static final int BUILDER_SCOUT_2 = 11;
  public static final int BUILDER_SCOUT_3 = 12;
  public static final int RECYCLER = 20;
  public static final int FACTORY = 30;
  public static final int ARMORY = 40;
  public static final int GUARD_TOWER_1 = 50;

  public static BuildOrder getBuildOrderFromID(int id) {
    switch (id) {
      case BuildOrderID.FIGHTER_SCOUT_1:
        return BuildOrder.FIGHTER_SCOUT_1;
      case BuildOrderID.FIGHTER_SCOUT_2:
        return BuildOrder.FIGHTER_SCOUT_2;
      case BuildOrderID.FIGHTER_SCOUT_3:
        return BuildOrder.FIGHTER_SCOUT_3;
      case BuildOrderID.FIGHTER_SCOUT_4:
        return BuildOrder.FIGHTER_SCOUT_4;
      case BuildOrderID.FIGHTER_SCOUT_5:
        return BuildOrder.FIGHTER_SCOUT_5;
      case BuildOrderID.FIGHTER_SCOUT_6:
        return BuildOrder.FIGHTER_SCOUT_6;
      case BuildOrderID.BUILDER_SCOUT_1:
        return BuildOrder.BUILDER_SCOUT_1;
      case BuildOrderID.BUILDER_SCOUT_2:
        return BuildOrder.BUILDER_SCOUT_2;
      case BuildOrderID.BUILDER_SCOUT_3:
        return BuildOrder.BUILDER_SCOUT_3;
      case BuildOrderID.RECYCLER:
        return BuildOrder.RECYCLER;
      case BuildOrderID.FACTORY:
        return BuildOrder.FACTORY;
      case BuildOrderID.ARMORY:
        return BuildOrder.ARMORY;
      case BuildOrderID.GUARD_TOWER_1:
        return BuildOrder.GUARD_TOWER_1;
    }
    System.out.println("WARNING: Fell through BuildOrder.getBuildOrderFromID with id = "+id);
    return null;
  }
}
