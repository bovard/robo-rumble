
package team122;
import battlecode.common.*;

/**
 * A list of all our robot builds. Make sure you update the getBuildOrderFromID method as 
 * well as the new ID in BuildOrderID.java when adding to this. Java Enums are teh fail
 * and you have to do some work to treat them like ints
 * @author bovard
 */
public enum BuildOrder {



  FIGHTER_SCOUT_1 (
    Chassis.LIGHT,
    ComponentType.RECYCLER,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.RADAR, ComponentType.BLASTER, ComponentType.SHIELD},
    new ComponentType[] {},
    new ComponentType[] {},
    Chassis.LIGHT.cost + ComponentType.RADAR.cost + ComponentType.BLASTER.cost + ComponentType.SHIELD.cost,
    BuildOrderID.FIGHTER_SCOUT_1
  ),
  FIGHTER_SCOUT_2 (
    Chassis.LIGHT,
    ComponentType.RECYCLER,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.RADAR, ComponentType.SMG, ComponentType.SMG, ComponentType.SHIELD},
    new ComponentType[] {},
    new ComponentType[] {},
    Chassis.LIGHT.cost + ComponentType.RADAR.cost + ComponentType.SMG.cost + ComponentType.SMG.cost + ComponentType.SHIELD.cost,
    BuildOrderID.FIGHTER_SCOUT_2
  ),
  FIGHTER_SCOUT_3 (
    Chassis.LIGHT,
    ComponentType.RECYCLER,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.RADAR, ComponentType.BLASTER, ComponentType.SMG},
    new ComponentType[] {},
    new ComponentType[] {},
    Chassis.LIGHT.cost + ComponentType.RADAR.cost + ComponentType.BLASTER.cost + ComponentType.SMG.cost,
    BuildOrderID.FIGHTER_SCOUT_3
  ),
  FIGHTER_SCOUT_4 (
    Chassis.LIGHT,
    ComponentType.RECYCLER,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.RADAR, ComponentType.SMG, ComponentType.SMG, ComponentType.SMG},
    new ComponentType[] {},
    new ComponentType[] {},
    Chassis.LIGHT.cost + ComponentType.RADAR.cost + ComponentType.SMG.cost + ComponentType.SMG.cost + ComponentType.SMG.cost,
    BuildOrderID.FIGHTER_SCOUT_4
  ),
  FIGHTER_SCOUT_5 (
    Chassis.LIGHT,
    ComponentType.RECYCLER,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.SIGHT, ComponentType.BLASTER, ComponentType.BLASTER, ComponentType.SHIELD},
    new ComponentType[] {},
    new ComponentType[] {},
    Chassis.LIGHT.cost + ComponentType.SIGHT.cost + ComponentType.BLASTER.cost + ComponentType.BLASTER.cost + ComponentType.SHIELD.cost,
    BuildOrderID.FIGHTER_SCOUT_5
  ),
  BUILDER_SCOUT_1 (
    Chassis.LIGHT,
    ComponentType.RECYCLER,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.SIGHT, ComponentType.CONSTRUCTOR},
    new ComponentType[] {},
    new ComponentType[] {},
    Chassis.LIGHT.cost + ComponentType.SIGHT.cost + ComponentType.CONSTRUCTOR.cost,
    BuildOrderID.BUILDER_SCOUT_1
  ),
  BUILDER_SCOUT_2 (
    Chassis.LIGHT,
    ComponentType.RECYCLER,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.SIGHT, ComponentType.CONSTRUCTOR, ComponentType.SHIELD},
    new ComponentType[] {},
    new ComponentType[] {},
    Chassis.LIGHT.cost + ComponentType.SIGHT.cost + ComponentType.CONSTRUCTOR.cost + ComponentType.SHIELD.cost,
    BuildOrderID.BUILDER_SCOUT_2
  ),
  BUILDER_SCOUT_3 (
    Chassis.LIGHT,
    ComponentType.RECYCLER,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.SIGHT, ComponentType.CONSTRUCTOR, ComponentType.PLATING},
    new ComponentType[] {},
    new ComponentType[] {},
    Chassis.LIGHT.cost + ComponentType.SIGHT.cost + ComponentType.CONSTRUCTOR.cost + ComponentType.PLATING.cost,
    BuildOrderID.BUILDER_SCOUT_3
  ),
  RECYCLER (
    Chassis.BUILDING,
    ComponentType.CONSTRUCTOR,
    new ComponentType[] {ComponentType.RECYCLER},
    new ComponentType[] {},
    new ComponentType[] {},
    new ComponentType[] {},
    Chassis.BUILDING.cost + ComponentType.RECYCLER.cost,
    BuildOrderID.RECYCLER
  ),
  FACTORY (
    Chassis.BUILDING,
    ComponentType.CONSTRUCTOR,
    new ComponentType[] {ComponentType.FACTORY},
    new ComponentType[] {},
    new ComponentType[] {},
    new ComponentType[] {},
    Chassis.BUILDING.cost + ComponentType.FACTORY.cost,
    BuildOrderID.FACTORY
  ),
  ARMORY (
    Chassis.BUILDING,
    ComponentType.CONSTRUCTOR,
    new ComponentType[] {ComponentType.ARMORY},
    new ComponentType[] {},
    new ComponentType[] {},
    new ComponentType[] {},
    Chassis.BUILDING.cost + ComponentType.ARMORY.cost,
    BuildOrderID.ARMORY
  ),
  GUARD_TOWER_1 (
    Chassis.BUILDING,
    ComponentType.CONSTRUCTOR,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.RADAR, ComponentType.SHIELD, ComponentType.SMG, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.PLATING, ComponentType.PLATING, ComponentType.PLATING},
    new ComponentType[] {},
    new ComponentType[] {},
    Chassis.BUILDING.cost + ComponentType.RADAR.cost + ComponentType.SHIELD.cost + ComponentType.SMG.cost + ComponentType.SHIELD.cost + ComponentType.SHIELD.cost + ComponentType.SHIELD.cost + ComponentType.SHIELD.cost + ComponentType.SHIELD.cost + ComponentType.SHIELD.cost + ComponentType.SHIELD.cost + ComponentType.SHIELD.cost + ComponentType.SHIELD.cost + ComponentType.SHIELD.cost + ComponentType.SHIELD.cost + ComponentType.SHIELD.cost + ComponentType.PLATING.cost + ComponentType.PLATING.cost + ComponentType.PLATING.cost,
    BuildOrderID.GUARD_TOWER_1
  );

  


  public final Chassis chassis;
  public final ComponentType chassisBuilder;
  public final ComponentType[] constructorComponents;
  public final ComponentType[] recyclerComponents;
  public final ComponentType[] factoryComponents;
  public final ComponentType[] armoryComponents;
  public final int cost;
  public final int id;
  
  /**
   * The components of recognized builds
   * @param chassis The Chassis of the robot
   * @param chassisBuilder The ComponentType that can build the chassis
   * @param constructorComponents The robot's components buildable by a constructor
   * @param recyclerComponents The robot's components buildable by a recycler
   * @param factoryComponents The robot's components buildable by a factory
   * @param armoryComponents The robot's components buildable by an armory
   * @param cost The total cost of the robot
   * @param id The id number of the robot (as per BuildOrderID.java)
   */
  BuildOrder(Chassis chassis,ComponentType chassisBuilder, ComponentType[] constructorComponents,
          ComponentType[] recyclerComponents, ComponentType[] factoryComponents,
          ComponentType[] armoryComponents, int cost, int id) {
    
    this.chassis = chassis;
    this.chassisBuilder = chassisBuilder;
    this.constructorComponents = constructorComponents;
    this.recyclerComponents = recyclerComponents;
    this.factoryComponents = factoryComponents;
    this.armoryComponents = armoryComponents;
    this.cost = cost;
    this.id = id;
  }

  

  /**
   * returns the list of components to build for the given buld component
   * @param builder the ComponentType builder (constructor, recycler, factor, armory)
   * @return the list of components to build, can be an empty array
   */
  public ComponentType[] getComponents(ComponentType builder) {
    if(builder == ComponentType.CONSTRUCTOR) {
      return constructorComponents;
    }
    else if (builder == ComponentType.RECYCLER) {
      return recyclerComponents;
    }
    else if (builder == ComponentType.FACTORY) {
      return factoryComponents;
    }
    else if (builder == ComponentType.ARMORY) {
      return armoryComponents;
    }
    System.out.println("WARNING: Invalid call to BuildOrder.getComponents() with "+builder.name());
    return null;
  }



}