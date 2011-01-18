package team122;
import battlecode.common.*;

/**
 * Descibes the compositions and build orders for robots
 * @author bovard
 */
public class RobotBuildOrder {

  //Note: for the ids, the 1000s represent the chassis 1=list, 2=medium, 3=heavy, 4=building
  //                   the 100s reprsent the model (FIGHTER SCOUT, RECYCLER)
  //as the contest constants have been changing a lot recently I think we should let the cost
  //calculations stay as opposed to hard coding them.
  //FIGHTER SCOUTS
  //1
  public static final Object[] FIGHTER_SCOUT = {Chassis.LIGHT, ComponentType.RADAR, ComponentType.BLASTER, ComponentType.SHIELD};
  public static final int FIGHTER_SCOUT_COST = Chassis.LIGHT.cost + ComponentType.RADAR.cost + ComponentType.BLASTER.cost + ComponentType.SHIELD.cost;
  public static final int FIGHTER_SCOUT_ID = 1001;
  //2
  public static final Object[] FIGHTER_SCOUT_2 = {Chassis.LIGHT, ComponentType.RADAR, ComponentType.SMG, ComponentType.SMG, ComponentType.SHIELD};
  public static final int FIGHTER_SCOUT_2_COST = Chassis.LIGHT.cost + ComponentType.RADAR.cost + ComponentType.SMG.cost + ComponentType.SMG.cost + ComponentType.SHIELD.cost;
  public static final int FIGHTER_SCOUT_2_ID = 1002;
  //3
  public static final Object[] FIGHTER_SCOUT_3 = {Chassis.LIGHT, ComponentType.RADAR, ComponentType.BLASTER, ComponentType.SMG};
  public static final int FIGHTER_SCOUT_3_COST = Chassis.LIGHT.cost + ComponentType.RADAR.cost + ComponentType.BLASTER.cost + ComponentType.SMG.cost;
  public static final int FIGHTER_SCOUT_3_ID = 1003;
  //4
  public static final Object[] FIGHTER_SCOUT_4 = {Chassis.LIGHT, ComponentType.RADAR, ComponentType.SMG, ComponentType.SMG, ComponentType.SMG};
  public static final int FIGHTER_SCOUT_4_COST = Chassis.LIGHT.cost + ComponentType.RADAR.cost + ComponentType.SMG.cost + ComponentType.SMG.cost + ComponentType.SMG.cost;
  public static final int FIGHTER_SCOUT_4_ID = 1004;
  //5
  public static final Object[] FIGHTER_SCOUT_5 = {Chassis.LIGHT, ComponentType.SIGHT, ComponentType.BLASTER, ComponentType.BLASTER, ComponentType.SHIELD};
  public static final int FIGHTER_SCOUT_5_COST = Chassis.LIGHT.cost + ComponentType.SIGHT.cost + ComponentType.BLASTER.cost + ComponentType.BLASTER.cost + ComponentType.SHIELD.cost;
  public static final int FIGHTER_SCOUT_5_ID = 1005;

  //BUILDER SCOUTS
  public static final Object[] BUILDER_SCOUT = {Chassis.LIGHT, ComponentType.SIGHT, ComponentType.CONSTRUCTOR};
  public static final int BUILDER_SCOUT_COST = Chassis.LIGHT.cost + ComponentType.SIGHT.cost + ComponentType.CONSTRUCTOR.cost;
  public static final int BUILDER_SCOUT_ID = 1101;

  public static final Object[] BUILDER_SCOUT_2 = {Chassis.LIGHT, ComponentType.SIGHT, ComponentType.CONSTRUCTOR, ComponentType.SHIELD};
  public static final int BUILDER_SCOUT_2_COST = Chassis.LIGHT.cost + ComponentType.SIGHT.cost + ComponentType.CONSTRUCTOR.cost + ComponentType.SHIELD.cost;
  public static final int BUILDER_SCOUT_2_ID = 1102;


  //BUILDINGS
  //TURRET DESIGNS: CONSTRUCTOR COMPONENTS / RECYCLER COMPONENTS / FACTORY COMPONENTS
  //NOTE: Turret 1 is stand-alone, Turret 2&3 should be placed together
  //TURRET_1: BUILDING, / RADAR, SHIELD, SMG, SMG / RAILGUN, REGEN, HARDENED
  //TURRET_2: BUILDING, / RADAR, SMG, SHIELD, / RAILGUN, RAILGUN, HARDENED
  //TURRET_3: BUILDING, / RADAR, / MEDIC, MEDIC, MEDIC, HARDENED
  //TURRET BASE
  public static final Object[] BUILDING = {Chassis.BUILDING};
  public static final int BUILDING_COST = Chassis.BUILDING.cost;
  public static final int BUILDING_ID = 4000;

  //TURRET RECYCLER PARTS
  //TURRET_1_RECYCLER_PART
  public static final ComponentType[] TURRET_1_RECYCLER_PART = {ComponentType.RADAR, ComponentType.SHIELD, ComponentType.SMG, ComponentType.SMG};
  public static final int TURRET_1_RECYCLER_PART_COST = ComponentType.RADAR.cost + ComponentType.SHIELD.cost + ComponentType.SMG.cost + ComponentType.SMG.cost;
  public static final int TURRET_1_RECYCLER_PART_ID = 4011;
  //TURRET_2_RECYCLER_PART
  public static final ComponentType[] TURRET_2_RECYCLER_PART = {ComponentType.RADAR, ComponentType.SMG, ComponentType.SHIELD};
  public static final int TURRET_2_RECYCLER_PART_COST = ComponentType.RADAR.cost + ComponentType.SMG.cost + ComponentType.SHIELD.cost;
  public static final int TURRET_2_RECYCLER_PART_ID = 4021;
  //TURRET_3_RECYCLER_PART
  public static final ComponentType[] TURRET_3_RECYCLER_PART = {ComponentType.RADAR};
  public static final int TURRET_3_RECYCLER_PART_COST = ComponentType.RADAR.cost;
  public static final int TURRET_3_RECYCLER_PART_ID = 4031;

  //TURRET FACTOR PARTS
  //TURRET_1_FACTORY_PART
  public static final ComponentType[] TURRET_1_FACTORY_PART = {ComponentType.RAILGUN, ComponentType.REGEN, ComponentType.HARDENED};
  public static final int TURRET_1_FACTORY_PART_COST = ComponentType.RAILGUN.cost + ComponentType.REGEN.cost + ComponentType.HARDENED.cost;
  public static final int TURRET_1_FACTORY_PART_ID = 4012;
  //TURRET_2_FACTORY_PART
  public static final ComponentType[] TURRET_2_FACTORY_PART = {ComponentType.RAILGUN, ComponentType.RAILGUN, ComponentType.HARDENED};
  public static final int TURRET_2_FACTORY_PART_COST = ComponentType.RAILGUN.cost + ComponentType.RAILGUN.cost + ComponentType.HARDENED.cost;
  public static final int TURRET_2_FACTORY_PART_ID = 4022;
  //TURRET_3_FACTORY_PART
  public static final ComponentType[] TURRET_3_FACTORY_PART = {ComponentType.MEDIC, ComponentType.MEDIC, ComponentType.MEDIC, ComponentType.HARDENED};
  public static final int TURRET_3_FACTORY_PART_COST = ComponentType.MEDIC.cost + ComponentType.MEDIC.cost + ComponentType.MEDIC.cost + ComponentType.HARDENED.cost;
  public static final int TURRET_3_FACTORY_PART_ID = 4032;

  //TURRET COSTS
  public static final int TURRET_1_COST = BUILDING_COST + TURRET_1_RECYCLER_PART_COST + TURRET_1_FACTORY_PART_COST;
  public static final int TURRET_1_ID = 4010;
  public static final int TURRET_2_COST = BUILDING_COST + TURRET_2_RECYCLER_PART_COST + TURRET_2_FACTORY_PART_COST;
  public static final int TURRET_2_ID = 4020;
  public static final int TURRET_3_COST = BUILDING_COST + TURRET_3_RECYCLER_PART_COST + TURRET_3_FACTORY_PART_COST;
  public static final int TURRET_3_ID = 4030;


  //RECYCLER
  public static final Object[] RECYCLER = {Chassis.BUILDING, ComponentType.RECYCLER};
  public static final int RECYCLER_COST = Chassis.BUILDING.cost + ComponentType.RECYCLER.cost;
  public static final int RECYCLER_ID = 4101;

  //FACTORY
  public static final Object[] FACTORY = {Chassis.BUILDING, ComponentType.FACTORY};
  public static final int FACTORY_COST = Chassis.BUILDING.cost + ComponentType.FACTORY.cost;
  public static final int FACTORY_ID = 4102;

  //ARMORY
  public static final Object[] ARMORY = {Chassis.BUILDING, ComponentType.ARMORY};
  public static final int ARMORY_COST = Chassis.BUILDING.cost + ComponentType.ARMORY.cost;
  public static final int ARMORY_ID = 4103;


  /**
   * Given a RobotBuildOrder ID, it returns the cost
   * @param buildOrderID the ID to lookeup
   * @return the cost of building the item with that id
   */
  public static final int getCost(int buildOrderID) {
    switch(buildOrderID) {
      case FIGHTER_SCOUT_ID:
        return FIGHTER_SCOUT_COST;
      case FIGHTER_SCOUT_2_ID:
        return FIGHTER_SCOUT_2_COST;
      case FIGHTER_SCOUT_3_ID:
        return FIGHTER_SCOUT_3_COST;
      case FIGHTER_SCOUT_4_ID:
        return FIGHTER_SCOUT_4_COST;
      case FIGHTER_SCOUT_5_ID:
        return FIGHTER_SCOUT_5_COST;
      case BUILDER_SCOUT_ID:
        return BUILDER_SCOUT_COST;
      case BUILDER_SCOUT_2_ID:
        return BUILDER_SCOUT_2_COST;
      case BUILDING_ID:
        return BUILDING_COST;
      case TURRET_1_RECYCLER_PART_ID:
        return TURRET_1_RECYCLER_PART_COST;
      case TURRET_2_RECYCLER_PART_ID:
        return TURRET_2_RECYCLER_PART_COST;
      case TURRET_3_RECYCLER_PART_ID:
        return TURRET_3_RECYCLER_PART_COST;
      case TURRET_1_FACTORY_PART_ID:
        return TURRET_1_FACTORY_PART_COST;
      case TURRET_2_FACTORY_PART_ID:
        return TURRET_2_FACTORY_PART_COST;
      case TURRET_3_FACTORY_PART_ID:
        return TURRET_3_FACTORY_PART_COST;
      case TURRET_1_ID:
        return TURRET_1_COST;
      case TURRET_2_ID:
        return TURRET_2_COST;
      case TURRET_3_ID:
        return TURRET_3_COST;
      case RECYCLER_ID:
        return RECYCLER_COST;
      case FACTORY_ID:
        return FACTORY_COST;
      case ARMORY_ID:
        return ARMORY_COST;
    }
    System.out.println("WARNING: Bad call to RobotBuildOrder.getCost");
    return 0;
  }

  /**
   * given a buildOrder ID, it looks up the Object[] buildOrder
   * @param buildOrderID the buildOrder to build
   * @return the Object[] build order
   */
  public static final Object[] getBuildOrder(int buildOrderID) {
    switch(buildOrderID) {
      case FIGHTER_SCOUT_ID:
        return FIGHTER_SCOUT;
      case FIGHTER_SCOUT_2_ID:
        return FIGHTER_SCOUT_2;
      case FIGHTER_SCOUT_3_ID:
        return FIGHTER_SCOUT_3;
      case FIGHTER_SCOUT_4_ID:
        return FIGHTER_SCOUT_4;
      case FIGHTER_SCOUT_5_ID:
        return FIGHTER_SCOUT_5;
      case BUILDER_SCOUT_ID:
        return BUILDER_SCOUT;
      case BUILDER_SCOUT_2_ID:
        return BUILDER_SCOUT_2;
      case BUILDING_ID:
        return BUILDING;
      case TURRET_1_RECYCLER_PART_ID:
        return TURRET_1_RECYCLER_PART;
      case TURRET_2_RECYCLER_PART_ID:
        return TURRET_2_RECYCLER_PART;
      case TURRET_3_RECYCLER_PART_ID:
        return TURRET_3_RECYCLER_PART;
      case TURRET_1_FACTORY_PART_ID:
        return TURRET_1_FACTORY_PART;
      case TURRET_2_FACTORY_PART_ID:
        return TURRET_2_FACTORY_PART;
      case TURRET_3_FACTORY_PART_ID:
        return TURRET_3_FACTORY_PART;
      case RECYCLER_ID:
        return RECYCLER;
      case FACTORY_ID:
        return FACTORY;
      case ARMORY_ID:
        return ARMORY;
    }
    System.out.println("WARNING: Bad call to RobotBuildOrder.getBuildOrder");
    return null;
  }

  /**
   * used for finding if a buildorder starts with a chasis or not (from there you can choose
   * the appropriate method in BuilderSystem).
   * @param buildOrderID the buildOrderID to lookup
   * @return if it starts with a chasis
   */
  public static final boolean startsWithChasis(int buildOrderID) {
    switch(buildOrderID) {
      case FIGHTER_SCOUT_ID:
        return true;
      case FIGHTER_SCOUT_2_ID:
        return true;
      case FIGHTER_SCOUT_3_ID:
        return true;
      case FIGHTER_SCOUT_4_ID:
        return true;
      case FIGHTER_SCOUT_5_ID:
        return true;
      case BUILDER_SCOUT_ID:
        return true;
      case BUILDER_SCOUT_2_ID:
        return true;
      case BUILDING_ID:
        return true;
      case TURRET_1_RECYCLER_PART_ID:
        return false;
      case TURRET_2_RECYCLER_PART_ID:
        return false;
      case TURRET_3_RECYCLER_PART_ID:
        return false;
      case TURRET_1_FACTORY_PART_ID:
        return false;
      case TURRET_2_FACTORY_PART_ID:
        return false;
      case TURRET_3_FACTORY_PART_ID:
        return false;
      case RECYCLER_ID:
        return true;
      case FACTORY_ID:
        return true;
      case ARMORY_ID:
        return true;
    }
    System.out.println("WARNING: Bad call to RobotBuildOrder.startsWithChasis");
    return false;
  }
}
