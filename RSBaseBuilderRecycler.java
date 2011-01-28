
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
    seqBuildProductionBuildings();
    seqBuildGuardTowers();
    while(true) {
      if(checkBase()) {
        yield();
      }
      else {
        seqBuildProductionBuildings();
        seqBuildGuardTowers();
      }
    }
  }


  public boolean checkBase() {
    return sensorSys.canSenseObject(armory) && sensorSys.canSenseObject(factory);
    //        && sensorSys.canSenseObject(guardOne) && sensorSys.canSenseObject(guardTwo);
  }


  public boolean seqBuildProductionBuildings() {
    for (int i = 0; i < 20; i++) {
      yield();
    }
    if(armory == null || !sensorSys.canSenseObject(armory)) {
      while(robotControl.getTeamResources() < BuildOrder.ARMORY.cost + 20 + PlayerConstants.MINIMUM_FLUX ||
              sensorSys.senseObjectAtLocation(armoryLoc, RobotLevel.ON_GROUND) != null) {
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
    if(factory == null || !sensorSys.canSenseObject(factory)) {
      while(robotControl.getTeamResources() < BuildOrder.FACTORY.cost + 20 + PlayerConstants.MINIMUM_FLUX) {
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


  private void setLocations() {
    Direction currentDir = robotControl.getDirection();
    armoryLoc = birthPlace.add(currentDir.rotateRight().rotateRight());
    armoryGuardLoc = birthPlace.add(currentDir.rotateRight().rotateRight().rotateRight());
    factoryLoc = birthPlace.add(currentDir.rotateLeft().rotateLeft());
    factoryGuardLoc = birthPlace.add(currentDir.rotateLeft().rotateLeft().rotateLeft());
    comTowerLoc = birthPlace.add(currentDir.opposite());
  }

  protected boolean seqBuildGuardTowers() {
    if(armoryGuard == null || !sensorSys.canSenseObject(armoryGuard)) {
      while(robotControl.getTeamResources() < BuildOrder.GUARD_TOWER_3.cost + PlayerConstants.MINIMUM_FLUX) {
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
      if(factory != null) {
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
    if(factoryGuard == null || !sensorSys.canSenseObject(factoryGuard)) {
      while(robotControl.getTeamResources() < BuildOrder.GUARD_TOWER_2.cost + PlayerConstants.MINIMUM_FLUX) {
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
      if(factory != null) {
        bcSys.setSendBuildDirective(BuildOrder.GUARD_TOWER_2.id, factoryGuardLoc);
        yield();
      }
    }
    return true;
  }



}
