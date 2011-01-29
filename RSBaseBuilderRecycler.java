
package team122;
import battlecode.common.*;

/**
 *
 * @author bovard
 */
public class RSBaseBuilderRecycler extends BuilderSensorRobotSystem {
  protected BuilderSystem constructor;
  protected BroadcastSystem bcSys;
  protected MapLocation constructionZone;
  protected Robot armory, factory, armoryGuard, factoryGuard, comTower;
  protected MapLocation armoryLoc, factoryLoc, armoryGuardLoc, factoryGuardLoc, comTowerLoc;


  public RSBaseBuilderRecycler(RobotController robotControl, SensorSystem sensorSys, BuilderSystem recycler) {
    super(robotControl, sensorSys, recycler);
    robotControl.setIndicatorString(0, "BaseBuidlerRecycler");

    //finds the little guy who turned this on
    constructionZone = findBaseBuilder();

    //build a constructor component
    while(robotControl.getTeamResources() < ComponentType.CONSTRUCTOR.cost + PlayerConstants.MINIMUM_FLUX
            || buildSys.isActive()) {
      yield();
      robotControl.setIndicatorString(1, "waitingToBuildConstructor");
    }
    buildSys.setBuildComponent(ComponentType.CONSTRUCTOR, birthPlace, RobotLevel.ON_GROUND);
    yield();

    constructor = new BuilderSystem(robotControl, (BuilderController)robotControl.components()[robotControl.components().length-1]);

    //build a antenna
    while(robotControl.getTeamResources() < ComponentType.ANTENNA.cost + PlayerConstants.MINIMUM_FLUX
            || buildSys.isActive()) {
      yield();
      robotControl.setIndicatorString(1, "waitingToBuildAntenna");
    }
    buildSys.setBuildComponent(ComponentType.ANTENNA, birthPlace, RobotLevel.ON_GROUND);
    yield();

    bcSys = new BroadcastSystem(robotControl, (BroadcastController)robotControl.components()[robotControl.components().length-1]);

    //turn towards the construction zone
    navSys.setTurn(birthPlace.directionTo(constructionZone));
    yield();
    setLocations();

  }

  @Override
  public void go() {
    seqBuildBase();
    while(true) {
      if(checkBase()) {
        seqBuildScoutsAndHeavies();
        yield();
      }
      else {
        seqBuildBase();
      }
    }
  }


  /**
   * Checks to makes sure that our base is safe and sounds
   * @return if any base buildings are missing
   */
  public boolean checkBase() {
    return sensorSys.canSenseObject(armory) && sensorSys.canSenseObject(factory)
            && sensorSys.canSenseObject(armoryGuard) && sensorSys.canSenseObject(factoryGuard);
  }

  /**
   * Checks to see if each element of the base is there and builds it if it isn't
   * @return
   */
  public boolean seqBuildBase() {
    while(buildSys.isActive()) {
      yield();
    }

    seqBuildArmory();
    seqBuildArmoryGuard();

    while(robotControl.getTeamResources() < BuildOrder.RECYCLER.cost + BuildOrder.GUARD_TOWER_3.cost + 6*PlayerConstants.MINIMUM_FLUX) {
      yield();
    }

    seqBuildFactory();
    seqBuildFactoryGuard();

    while(robotControl.getTeamResources() < BuildOrder.RECYCLER.cost + BuildOrder.GUARD_TOWER_2.cost + 6*PlayerConstants.MINIMUM_FLUX) {
      yield();
    }

    return true;
  }

  /**
   * Checks to see if either of the production buildings are not build or are missing and builds them
   * @return true
   */
  public boolean seqBuildArmory() {
    if(armory == null || !sensorSys.canSenseObject(armory)) {
      while(robotControl.getTeamResources() < BuildOrder.ARMORY.cost + 20 + PlayerConstants.MINIMUM_FLUX ||
              sensorSys.senseObjectAtLocation(armoryLoc, RobotLevel.ON_GROUND) != null
              || constructor.isActive()) {
        yield();
        robotControl.setIndicatorString(1, "waitingToBuildArmory");
      }
      constructor.setBuildChassis(Chassis.BUILDING, armoryLoc);
      yield();
      while(constructor.isActive()) {
        yield();
      }
      constructor.setBuildComponent(ComponentType.ARMORY, armoryLoc, RobotLevel.ON_GROUND);
      while(constructor.isActive()) {
        yield();
      }
      armory = (Robot)sensorSys.senseObjectAtLocation(armoryLoc, RobotLevel.ON_GROUND);
      if(armory != null) {
        try {
          robotControl.turnOn(armoryLoc, RobotLevel.ON_GROUND);
        } catch (Exception e) {
          System.out.println("caught exception:");
          e.printStackTrace();
        }
      }
    }
    return true;
  }

  public boolean seqBuildFactory() {
    if(factory == null || !sensorSys.canSenseObject(factory)) {
      while(robotControl.getTeamResources() < BuildOrder.FACTORY.cost + 20 + PlayerConstants.MINIMUM_FLUX
              || sensorSys.senseObjectAtLocation(factoryLoc, RobotLevel.ON_GROUND) != null
              || constructor.isActive()) {
        yield();
        robotControl.setIndicatorString(1, "waitingToBuildFactory");
      }
      constructor.setBuildChassis(Chassis.BUILDING, factoryLoc);
      yield();
      while(constructor.isActive()) {
        yield();
      }
      constructor.setBuildComponent(ComponentType.FACTORY, factoryLoc, RobotLevel.ON_GROUND);
      while(constructor.isActive()) {
        yield();
      }
      factory = (Robot)sensorSys.senseObjectAtLocation(factoryLoc, RobotLevel.ON_GROUND);
      if(factory != null) {
        try {
          robotControl.turnOn(factoryLoc, RobotLevel.ON_GROUND);
        } catch (Exception e) {
          System.out.println("caught exception:");
          e.printStackTrace();
        }
      }
    }
    return true;
  }


  /**
   * set the locations to build each base component
   */
  private void setLocations() {
    Direction currentDir = robotControl.getDirection();
    armoryLoc = birthPlace.add(currentDir.rotateRight().rotateRight());
    armoryGuardLoc = birthPlace.add(currentDir.rotateRight().rotateRight().rotateRight());
    factoryLoc = birthPlace.add(currentDir.rotateLeft().rotateLeft());
    factoryGuardLoc = birthPlace.add(currentDir.rotateLeft().rotateLeft().rotateLeft());
    comTowerLoc = birthPlace.add(currentDir.opposite());
  }

  /**
   * checks to see that both guard towers are built and/or still there, builds them if they aren't
   * @return true
   */
  protected boolean seqBuildArmoryGuard() {
    //build the armory guard tower if there isn't one there
    if(armoryGuard == null || !sensorSys.canSenseObject(armoryGuard)) {
      while(robotControl.getTeamResources() < BuildOrder.GUARD_TOWER_3.cost + PlayerConstants.MINIMUM_FLUX
              || sensorSys.senseObjectAtLocation(armoryGuardLoc, RobotLevel.ON_GROUND) != null
              || constructor.isActive()) {
        yield();
        robotControl.setIndicatorString(1, "waitingToBuildArmoryGuardTower");
      }
      constructor.setBuildChassis(Chassis.BUILDING, armoryGuardLoc);
      yield();
      while(constructor.isActive()) {
        yield();
      }
      seqBuild(BuildOrder.GUARD_TOWER_3, armoryGuardLoc);
      while(constructor.isActive()) {
        yield();
      }
      armoryGuard = (Robot)sensorSys.senseObjectAtLocation(armoryGuardLoc, RobotLevel.ON_GROUND);
      if(armoryGuard != null) {
        try {
          robotControl.turnOn(armoryGuardLoc, RobotLevel.ON_GROUND);
          bcSys.setSendBuildDirective(BuildOrder.GUARD_TOWER_3.id, armoryGuardLoc);
          yield();
        } catch (Exception e) {
          System.out.println("caught exception:");
          e.printStackTrace();
        }
      }
    }
    return true;
  }

  public boolean seqBuildFactoryGuard() {
    //build the factory guard tower if there isn't one there
    if(factoryGuard == null || !sensorSys.canSenseObject(factoryGuard)) {
      while(robotControl.getTeamResources() < BuildOrder.GUARD_TOWER_2.cost + PlayerConstants.MINIMUM_FLUX
              || sensorSys.senseObjectAtLocation(factoryGuardLoc, RobotLevel.ON_GROUND) != null
              || constructor.isActive()) {
        yield();
        robotControl.setIndicatorString(1, "waitingToBuildFactoryGuardTower");
      }
      constructor.setBuildChassis(Chassis.BUILDING, factoryGuardLoc);
      yield();
      while(constructor.isActive()) {
        yield();
      }
      seqBuild(BuildOrder.GUARD_TOWER_2, factoryGuardLoc);
      while(constructor.isActive()) {
        yield();
      }
      factoryGuard = (Robot)sensorSys.senseObjectAtLocation(factoryGuardLoc, RobotLevel.ON_GROUND);
      if(factoryGuard != null) {
        bcSys.setSendBuildDirective(BuildOrder.GUARD_TOWER_2.id, factoryGuardLoc);
        yield();
      }
    }

    
    


    return true;
  }

  private int lastScout = 0;
  private int lastHeavy = 0;
  private int scoutCooldown = 150;
  private int heavyCooldown = 75;

  protected boolean seqBuildScoutsAndHeavies() {
    seqBuildScout();
    if(rand.nextBoolean()) {
      seqBuildHeavy(BuildOrder.HEAVY_WARRIOR_1);
    }
    else{
      seqBuildHeavy(BuildOrder.HEAVY_WARRIOR_2);
    }


    return true;
  }


  protected boolean seqBuildScout() {
    if (lastScout + scoutCooldown < Clock.getRoundNum()
            && gameEvents.isFluxRegenAbove(Chassis.FLYING.upkeep)
            && robotControl.getTeamResources() > BuildOrder.FLYING_BUILDER_SCOUT_1.cost +
            BuildOrder.RECYCLER.cost + PlayerConstants.MINIMUM_FLUX) {
      robotControl.setIndicatorString(1, "Building a Scout!");
      if(sensorSys.senseObjectAtLocation(constructionZone, RobotLevel.IN_AIR)==null) {
        bcSys.setSendBuildDirective(BuildOrder.FLYING_BUILDER_SCOUT_1.id, constructionZone);
        lastScout = Clock.getRoundNum();
        while(sensorSys.senseObjectAtLocation(constructionZone, RobotLevel.IN_AIR)==null
                || robotControl.getTeamResources() < BuildOrder.FLYING_BUILDER_SCOUT_1.cost +
            BuildOrder.RECYCLER.cost + 5*PlayerConstants.MINIMUM_FLUX) {
          yield();
        }
        seqBuild(BuildOrder.FLYING_BUILDER_SCOUT_1, constructionZone);
      }
    }
    return false;
  }

  protected boolean seqBuildHeavy(BuildOrder order) {
    if (lastHeavy + heavyCooldown < Clock.getRoundNum()
            && gameEvents.isFluxRegenAbove(PlayerConstants.MINIMUM_FLUX + Chassis.HEAVY.upkeep)
            && robotControl.getTeamResources() > order.cost +
            BuildOrder.RECYCLER.cost + PlayerConstants.MINIMUM_FLUX) {
      robotControl.setIndicatorString(1, "Building a Heavy!");
      if(sensorSys.senseObjectAtLocation(constructionZone, RobotLevel.ON_GROUND)==null) {
        bcSys.setSendBuildDirective(order.id, constructionZone);
        lastHeavy = Clock.getRoundNum();
        while(sensorSys.senseObjectAtLocation(constructionZone, RobotLevel.ON_GROUND)==null) {
          yield();
        }
        for(int i=0; i<10; i++) {
          yield();
        }
        while(robotControl.getTeamResources() < order.cost +
            BuildOrder.RECYCLER.cost + 5*PlayerConstants.MINIMUM_FLUX) {
          yield();
        }
        seqBuild(order, constructionZone);
      }
    }
    return true;
  }


}
