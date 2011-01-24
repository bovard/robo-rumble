package team122;

/**
 * Keeps track of the BuildOrder IDs and allows finding a build order through its id
 * @author bovard
 */
public class BuildOrderID {
  public static final int FLYING_SCOUT_1 = 1;
  public static final int FIGHTER_SCOUT_1 = 11;
  public static final int FIGHTER_SCOUT_2 = 12;
  public static final int FIGHTER_SCOUT_3 = 13;
  public static final int FIGHTER_SCOUT_4 = 14;
  public static final int FIGHTER_SCOUT_5 = 15;
  public static final int FIGHTER_SCOUT_6 = 16;
  public static final int BUILDER_SCOUT_1 = 20;
  public static final int BUILDER_SCOUT_2 = 21;
  public static final int BUILDER_SCOUT_3 = 22;
  public static final int FLYING_BUILDER_SCOUT_1 = 30;
  public static final int RECYCLER = 40;
  public static final int COMCYCLER = 41;
  public static final int COMCYCLER_2 = 42;
  public static final int FACTORY = 50;
  public static final int ARMORY = 60;
  public static final int GUARD_TOWER_1 = 70;

  public static BuildOrder getBuildOrderFromID(int id) {
    switch (id) {
      case BuildOrderID.FLYING_SCOUT_1:
        return BuildOrder.FLYING_SCOUT_1;
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
      case BuildOrderID.FLYING_BUILDER_SCOUT_1:
        return BuildOrder.FLYING_BUILDER_SCOUT_1;
      case BuildOrderID.RECYCLER:
        return BuildOrder.RECYCLER;
      case BuildOrderID.COMCYCLER:
        return BuildOrder.COMCYCLER;
      case BuildOrderID.COMCYCLER_2:
        return BuildOrder.COMCYCLER_2;
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
