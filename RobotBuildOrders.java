package team122;
import battlecode.common.*;

/**
 * Descibes the compositions and build orders for robots
 * @author bovard
 */
public class RobotBuildOrders {
  //TODO: redo the costs to just a fixed number... I don't think the costs will ever change
  //and it is computationally expensive to calculate all of these
  public static final Object[] FIGHTER_SCOUT = {Chassis.LIGHT, ComponentType.RADAR, ComponentType.BLASTER, ComponentType.SHIELD};
  public static final int FIGHTER_SCOUT_COST = Chassis.LIGHT.cost + ComponentType.RADAR.cost + ComponentType.BLASTER.cost + ComponentType.SHIELD.cost;
  public static final Object[] FIGHTER_SCOUT_2 = {Chassis.LIGHT, ComponentType.RADAR, ComponentType.BLASTER};
  public static final int FIGHTER_SCOUT_2_COST = Chassis.LIGHT.cost + ComponentType.RADAR.cost + ComponentType.BLASTER.cost;
  public static final Object[] FIGHTER_SCOUT_3 = {Chassis.LIGHT, ComponentType.RADAR, ComponentType.BLASTER, ComponentType.SMG};
  public static final int FIGHTER_SCOUT_3_COST = Chassis.LIGHT.cost + ComponentType.RADAR.cost + ComponentType.BLASTER.cost + ComponentType.SMG.cost;
  public static final Object[] FIGHTER_SCOUT_4 = {Chassis.LIGHT, ComponentType.RADAR, ComponentType.SMG, ComponentType.SMG, ComponentType.SMG};
  public static final int FIGHTER_SCOUT_4_COST = Chassis.LIGHT.cost + ComponentType.RADAR.cost + ComponentType.SMG.cost + ComponentType.SMG.cost + ComponentType.SMG.cost;

  public static final Object[] BUILDER_SCOUT = {Chassis.LIGHT, ComponentType.SIGHT, ComponentType.CONSTRUCTOR};
  public static final int BUILDER_SCOUT_COST = Chassis.LIGHT.cost + ComponentType.SIGHT.cost + ComponentType.CONSTRUCTOR.cost;
  public static final Object[] BUILDER_SCOUT_2 = {Chassis.LIGHT, ComponentType.SIGHT, ComponentType.CONSTRUCTOR, ComponentType.SHIELD};
  public static final int BUILDER_SCOUT_2_COST = Chassis.LIGHT.cost + ComponentType.SIGHT.cost + ComponentType.CONSTRUCTOR.cost + ComponentType.SHIELD.cost;

}
