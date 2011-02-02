
package team122;
import battlecode.common.*;

/**
 * A list of all our robot builds. Make sure you update the getBuildOrderFromID method as 
 * well as the new ID in BuildOrderID.java when adding to this. We needed to add the extra
 * constants so we can treat these BuildOrders like int (for transmitting in messages)
 * @author bovard
 */
public enum BuildOrder {


  FLYING_SCOUT_1 (
    //Chassis
    Chassis.FLYING,
    //Chassis Builder
    ComponentType.ARMORY,
    //Components built by the constructor
    new ComponentType[] {},
    //Components built by the recycler
    new ComponentType[] {ComponentType.RADAR},
    //Components built by the factory
    new ComponentType[] { ComponentType.DISH},
    //Components built by the armory
    new ComponentType[] {},
    //cost
    Chassis.FLYING.cost + ComponentType.RADAR.cost + ComponentType.DISH.cost,
    //id
    BuildOrderID.FLYING_SCOUT_1
  ),
  FLYING_FIGHTER_SCOUT_1 (
    Chassis.FLYING,
    ComponentType.ARMORY,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.SIGHT, ComponentType.HAMMER, ComponentType.SHIELD, ComponentType.PLATING},
    new ComponentType[] {},
    new ComponentType[] {},
    Chassis.FLYING.cost + ComponentType.SIGHT.cost + ComponentType.HAMMER.cost + ComponentType.SHIELD.cost + ComponentType.PLATING.cost,
    BuildOrderID.FLYING_FIGHTER_SCOUT_1
  ),
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
  FIGHTER_SCOUT_6 ( //aka "HAMMER TIME!"
    Chassis.LIGHT,
    ComponentType.RECYCLER,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.SIGHT, ComponentType.HAMMER, ComponentType.HAMMER, ComponentType.SHIELD},
    new ComponentType[] {},
    new ComponentType[] {},
    Chassis.LIGHT.cost + ComponentType.SIGHT.cost + ComponentType.HAMMER.cost + ComponentType.HAMMER.cost + ComponentType.SHIELD.cost,
    BuildOrderID.FIGHTER_SCOUT_6
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
  FLYING_BUILDER_SCOUT_1 (
    Chassis.FLYING,
    ComponentType.ARMORY,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.SIGHT, ComponentType.CONSTRUCTOR},
    new ComponentType[] {},
    new ComponentType[] {},
    Chassis.FLYING.cost + ComponentType.SIGHT.cost + ComponentType.CONSTRUCTOR.cost,
    BuildOrderID.FLYING_BUILDER_SCOUT_1
  ),
  /**
   * HEAVY_WARRIOR_1 is immune to light arms fire (smg, blaster, hammer)
   * (blaster, hammer, smg)
   */
  HEAVY_WARRIOR_1 (
    Chassis.HEAVY,
    ComponentType.FACTORY,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.RADAR, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD},
    new ComponentType[] {ComponentType.REGEN, ComponentType.RAILGUN},
    new ComponentType[] {},
    ComponentType.RADAR.cost + 6*ComponentType.SHIELD.cost + ComponentType.REGEN.cost + ComponentType.RAILGUN.cost,
    BuildOrderID.HEAVY_WARRIOR_1
  ),
  /**
   * HEAVY_WARRIOR_2 is resistant to heavy weapon fire (blasters, beam, railguns) and nullifies SMGs
   * Also the 2 smgs should try to negate enemy plasmas
   */
  HEAVY_WARRIOR_2 (
    Chassis.HEAVY,
    ComponentType.FACTORY,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.RADAR, ComponentType.SHIELD, ComponentType.SMG, ComponentType.SMG},
    new ComponentType[] {ComponentType.REGEN, ComponentType.HARDENED, ComponentType.RAILGUN},
    new ComponentType[] {},
    ComponentType.RADAR.cost + ComponentType.SHIELD.cost + 2 * ComponentType.SMG.cost + ComponentType.REGEN.cost + ComponentType.HARDENED.cost + ComponentType.RAILGUN.cost,
    BuildOrderID.HEAVY_WARRIOR_2
  ),
  /**
   * HEAVY_WARRIOR_3 tries to replace the need for regen by using plasmas
   */
  HEAVY_WARRIOR_3 (
    Chassis.HEAVY,
    ComponentType.FACTORY,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.RADAR},
    new ComponentType[] {ComponentType.RAILGUN},
    new ComponentType[] {ComponentType.PLASMA, ComponentType.PLASMA, ComponentType.PLASMA, ComponentType.PLASMA, ComponentType.PLASMA},
    ComponentType.RADAR.cost + ComponentType.SHIELD.cost + ComponentType.RAILGUN.cost + 5 * ComponentType.PLASMA.cost,
    BuildOrderID.HEAVY_WARRIOR_3
  ),
  /**
   * HEAVY_WARRIOR_4 should be used against enemies who leave their shields at home
   */
  HEAVY_WARRIOR_4 (
    Chassis.HEAVY,
    ComponentType.FACTORY,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.RADAR, ComponentType.SHIELD, ComponentType.SMG, ComponentType.SMG, ComponentType.SMG, ComponentType.SMG, ComponentType.SMG, ComponentType.SMG, ComponentType.SMG},
    new ComponentType[] {ComponentType.HARDENED, ComponentType.REGEN},
    new ComponentType[] {},
    ComponentType.RADAR.cost + ComponentType.SHIELD.cost + 7 * ComponentType.SMG.cost + ComponentType.HARDENED.cost + ComponentType.REGEN.cost,
    BuildOrderID.HEAVY_WARRIOR_4
  ),
  /**
   * HEAVY_WARRIOR_5 is made for overcoming plasma shields at close range
   */
  HEAVY_WARRIOR_5 (
    Chassis.HEAVY,
    ComponentType.FACTORY,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.RADAR, ComponentType.HAMMER, ComponentType.HAMMER, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD},
    new ComponentType[] {ComponentType.HARDENED, ComponentType.REGEN},
    new ComponentType[] {},
    ComponentType.RADAR.cost + 4 * ComponentType.SHIELD.cost + 2 * ComponentType.HAMMER.cost + ComponentType.REGEN.cost + ComponentType.HARDENED.cost,
    BuildOrderID.HEAVY_WARRIOR_5
  ),
  /**
   * HEAVY_WARRIOR_6 is made for overcoming plasma shields at a far range
   */
  HEAVY_WARRIOR_6 (
    Chassis.HEAVY,
    ComponentType.FACTORY,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.RADAR, ComponentType.SMG},
    new ComponentType[] {},
    new ComponentType[] {ComponentType.BEAM, ComponentType.BEAM, ComponentType.PLASMA, ComponentType.PLASMA, ComponentType.PLASMA},
    ComponentType.RADAR.cost + 2 * ComponentType.BEAM.cost + 3 * ComponentType.PLASMA.cost + ComponentType.SMG.cost,
    BuildOrderID.HEAVY_WARRIOR_6
  ),
  /**
   * TURTLE_1 is almost invicible, with an okay punch
   */
  TURTLE_1 (
    Chassis.HEAVY,
    ComponentType.FACTORY,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.SIGHT, ComponentType.HAMMER, ComponentType.HAMMER, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD},
    new ComponentType[] {ComponentType.REGEN},
    new ComponentType[] {},
    ComponentType.SIGHT.cost + 9 * ComponentType.SHIELD.cost + 2 * ComponentType.HAMMER.cost + ComponentType.REGEN.cost,
    BuildOrderID.TURTLE_1
  ),
  /**
   * TURTLE_1 is almost invicible, with a small punch
   */
  TURTLE_2 (
    Chassis.HEAVY,
    ComponentType.FACTORY,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.SIGHT, ComponentType.HAMMER, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD},
    new ComponentType[] {ComponentType.REGEN},
    new ComponentType[] {},
    ComponentType.SIGHT.cost + 11 * ComponentType.SHIELD.cost + ComponentType.HAMMER.cost + ComponentType.REGEN.cost,
    BuildOrderID.TURTLE_2
  ),
  /**
   * TURTLE_3 is pretty much invicible, used to broadcast target locations
   */
  TURTLE_3 (
    Chassis.HEAVY,
    ComponentType.FACTORY,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD},
    new ComponentType[] {ComponentType.DISH, ComponentType.REGEN},
    new ComponentType[] {},
    12 * ComponentType.SHIELD.cost + ComponentType.DISH.cost + ComponentType.REGEN.cost,
    BuildOrderID.TURTLE_3
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
  COMCYCLER (
    Chassis.BUILDING,
    ComponentType.CONSTRUCTOR,
    new ComponentType[] {ComponentType.RECYCLER},
    new ComponentType[] {ComponentType.SHIELD, ComponentType.SIGHT, ComponentType.BLASTER, ComponentType.ANTENNA},
    new ComponentType[] {},
    new ComponentType[] {},
    Chassis.BUILDING.cost + ComponentType.CONSTRUCTOR.cost + ComponentType.SIGHT.cost + ComponentType.BLASTER.cost + ComponentType.SHIELD.cost + ComponentType.ANTENNA.cost,
    BuildOrderID.COMCYCLER
  ),
  COMCYCLER_2 (
    Chassis.BUILDING,
    ComponentType.CONSTRUCTOR,
    new ComponentType[] {ComponentType.RECYCLER},
    new ComponentType[] {ComponentType.RADAR, ComponentType.BLASTER},
    new ComponentType[] {},
    new ComponentType[] {},
    Chassis.BUILDING.cost + ComponentType.CONSTRUCTOR.cost + ComponentType.RADAR.cost + ComponentType.BLASTER.cost,
    BuildOrderID.COMCYCLER_2
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
    new ComponentType[] {ComponentType.RADAR, ComponentType.SHIELD, ComponentType.SMG, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.SHIELD, ComponentType.BLASTER, ComponentType.BLASTER, ComponentType.SMG},
    new ComponentType[] {},
    new ComponentType[] {},
    Chassis.BUILDING.cost + ComponentType.RADAR.cost + ComponentType.SHIELD.cost + ComponentType.SMG.cost + ComponentType.SHIELD.cost + ComponentType.SHIELD.cost + ComponentType.SHIELD.cost + ComponentType.SHIELD.cost + ComponentType.SHIELD.cost + ComponentType.BLASTER.cost + ComponentType.BLASTER.cost + ComponentType.SMG.cost,
    BuildOrderID.GUARD_TOWER_1
  ),
  GUARD_TOWER_2 (
    Chassis.BUILDING,
    ComponentType.CONSTRUCTOR,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.RADAR, ComponentType.SMG, ComponentType.SMG, ComponentType.SMG, ComponentType.SMG, ComponentType.SHIELD },
    new ComponentType[] {ComponentType.HARDENED, ComponentType.REGEN, ComponentType.RAILGUN},
    new ComponentType[] {},
    Chassis.BUILDING.cost + ComponentType.RADAR.cost + ComponentType.SHIELD.cost + 4*ComponentType.SMG.cost + ComponentType.HARDENED.cost + ComponentType.REGEN.cost + ComponentType.RAILGUN.cost,
    BuildOrderID.GUARD_TOWER_2
  ),
  GUARD_TOWER_3 (
    Chassis.BUILDING,
    ComponentType.CONSTRUCTOR,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.RADAR, ComponentType.SMG, },
    new ComponentType[] {},
    new ComponentType[] {ComponentType.PLASMA, ComponentType.PLASMA, ComponentType.PLASMA, ComponentType.PLASMA, ComponentType.BEAM, ComponentType.BEAM},
    Chassis.BUILDING.cost + ComponentType.RADAR.cost + ComponentType.SMG.cost + 4*ComponentType.PLASMA.cost + 2*ComponentType.BEAM.cost,
    BuildOrderID.GUARD_TOWER_3
  ),
  CONTROL_TOWER_1 (
    Chassis.BUILDING,
    ComponentType.CONSTRUCTOR,
    new ComponentType[] {},
    new ComponentType[] {ComponentType.SHIELD},
    new ComponentType[] {ComponentType.HARDENED, ComponentType.REGEN},
    new ComponentType[] {ComponentType.SATELLITE, ComponentType.NETWORK},
    Chassis.BUILDING.cost + ComponentType.RADAR.cost + ComponentType.SMG.cost + 4*ComponentType.PLASMA.cost + 2*ComponentType.BEAM.cost,
    BuildOrderID.CONTROL_TOWER_1
  );

  


  public final Chassis chassis;
  public final ComponentType chassisBuilder;
  public final ComponentType[] constructorComponents;
  public final ComponentType[] recyclerComponents;
  public final ComponentType[] factoryComponents;
  public final ComponentType[] armoryComponents;
  public final int cost;
  public final int id;
  public final int[] key;
  
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
    this.key = ComponentTypeKey.getComponentTypeKey(this);
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