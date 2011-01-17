package team122;
import battlecode.common.*;

/**
 * Descibes the compositions and build orders for robots
 * @author bovard
 */
public class RobotBuildOrder {

  //as the contest constants have been changing a lot recently I think we should let the cost
  //calculations stay as opposed to hard coding them.
  //FIGHTER SCOUTS
  //1
  public static final Object[] FIGHTER_SCOUT = {Chassis.LIGHT, ComponentType.RADAR, ComponentType.BLASTER, ComponentType.SHIELD};
  public static final int FIGHTER_SCOUT_COST = Chassis.LIGHT.cost + ComponentType.RADAR.cost + ComponentType.BLASTER.cost + ComponentType.SHIELD.cost;
  //2
  public static final Object[] FIGHTER_SCOUT_2 = {Chassis.LIGHT, ComponentType.RADAR, ComponentType.SMG, ComponentType.SMG, ComponentType.SHIELD};
  public static final int FIGHTER_SCOUT_2_COST = Chassis.LIGHT.cost + ComponentType.RADAR.cost + ComponentType.SMG.cost + ComponentType.SMG.cost + ComponentType.SHIELD.cost;
  //3
  public static final Object[] FIGHTER_SCOUT_3 = {Chassis.LIGHT, ComponentType.RADAR, ComponentType.BLASTER, ComponentType.SMG};
  public static final int FIGHTER_SCOUT_3_COST = Chassis.LIGHT.cost + ComponentType.RADAR.cost + ComponentType.BLASTER.cost + ComponentType.SMG.cost;
  //4
  public static final Object[] FIGHTER_SCOUT_4 = {Chassis.LIGHT, ComponentType.RADAR, ComponentType.SMG, ComponentType.SMG, ComponentType.SMG};
  public static final int FIGHTER_SCOUT_4_COST = Chassis.LIGHT.cost + ComponentType.RADAR.cost + ComponentType.SMG.cost + ComponentType.SMG.cost + ComponentType.SMG.cost;
  //5
  public static final Object[] FIGHTER_SCOUT_5 = {Chassis.LIGHT, ComponentType.SIGHT, ComponentType.BLASTER, ComponentType.BLASTER, ComponentType.SHIELD};
  public static final int FIGHTER_SCOUT_5_COST = Chassis.LIGHT.cost + ComponentType.SIGHT.cost + ComponentType.BLASTER.cost + ComponentType.BLASTER.cost + ComponentType.SHIELD.cost;

  //BUILDER SCOUTS
  public static final Object[] BUILDER_SCOUT = {Chassis.LIGHT, ComponentType.SIGHT, ComponentType.CONSTRUCTOR};
  public static final int BUILDER_SCOUT_COST = Chassis.LIGHT.cost + ComponentType.SIGHT.cost + ComponentType.CONSTRUCTOR.cost;
  public static final Object[] BUILDER_SCOUT_2 = {Chassis.LIGHT, ComponentType.SIGHT, ComponentType.CONSTRUCTOR, ComponentType.SHIELD};
  public static final int BUILDER_SCOUT_2_COST = Chassis.LIGHT.cost + ComponentType.SIGHT.cost + ComponentType.CONSTRUCTOR.cost + ComponentType.SHIELD.cost;

  public static final Object[] RECYCLER = {Chassis.BUILDING, ComponentType.RECYCLER};
  public static final int RECYCLER_COST = Chassis.BUILDING.cost + ComponentType.RECYCLER.cost;
}
