package team122;
import battlecode.common.*;

/**
 *
 * @author bovard
 */
public class RSRecycler extends BuilderSensorRobotSystem {
  protected boolean shouldBuild = false, stayActive = false;

  public RSRecycler(RobotController robotControl, SensorSystem sensorSys, BuilderSystem buildSys) {
    super(robotControl, sensorSys, buildSys);


    //check to see if this Recycler is the most SouthWestern in a group of four mines it should produce
    //if it is set shouldBuild to true so it knows it should start building troops
    try {
      if(sensorSys.senseObjectAtLocation(birthPlace.add(Direction.NORTH), RobotLevel.MINE) != null
              && sensorSys.senseObjectAtLocation(birthPlace.add(Direction.NORTH_EAST), RobotLevel.MINE) != null
              && sensorSys.senseObjectAtLocation(birthPlace.add(Direction.EAST), RobotLevel.MINE) != null) {
        shouldBuild=true;
        stayActive=true;
      }
      //otherwise if the mine is the most northeastern mine around stay active (become a ComCycler)
      else if (sensorSys.senseObjectAtLocation(birthPlace.add(Direction.NORTH), RobotLevel.MINE) == null
              && sensorSys.senseObjectAtLocation(birthPlace.add(Direction.EAST), RobotLevel.MINE) == null
              && sensorSys.senseObjectAtLocation(birthPlace.add(Direction.NORTH_EAST), RobotLevel.MINE) == null) {
        stayActive = true;
      }
    }catch (Exception e) {
      System.out.println("caught exception:");
      e.printStackTrace();
    }
  }


  @Override
  public void go() {

    //Note: we'll still get income from the mines.
    while(shouldBuild) {
      if (shouldBuild && Clock.getRoundNum() > 150 && robotControl.getTeamResources() > BuildOrder.RECYCLER.cost + PlayerConstants.MINIMUM_FLUX)
        selBuildScouts();
    }

    if(Clock.getRoundNum() > 50)
    {
      //if we aren't pumping out scouts fulltime, build a couple before continuing
      while(robotControl.getTeamResources() < BuildOrder.RECYCLER.cost + 
              BuildOrder.BUILDER_SCOUT_1.cost + PlayerConstants.MINIMUM_FLUX) {
        yield();
      }
      if(Clock.getRoundNum() < PlayerConstants.START_COMCYCLERS) {
        seqBuildRandomFighterScout();
      }
      else {
        seqBuild(BuildOrder.BUILDER_SCOUT_1);
      }
    }


    while(stayActive) {
      //wait until we want to start building gaurd towers then do it up!
      while(Clock.getRoundNum() < PlayerConstants.START_COMCYCLERS) {
        yield();
      }
      //wait for funds to build an antenna and our buildcontroller is free
      while(robotControl.getTeamResources() < BuildOrder.COMCYCLER_2.cost + PlayerConstants.MINIMUM_FLUX
              || buildSys.isActive()) {
        yield();
      }
      //once we've built an antenna, make a new ComRecyclerRobotSystem and start it
      if (seqBuild(BuildOrder.COMCYCLER_2, birthPlace)) {
        ComponentController[] components = robotControl.components();
        //radar
        SensorSystem newSensorSystem = new SensorSystem(robotControl, (SensorController)components[components.length-2]);
        //and blaster
        WeaponSystem newWeaponSystem = new WeaponSystem((WeaponController)components[components.length-1], newSensorSystem);
        //them make a new ComCyler and unleesh it upon the world!
        new RSComCycler(robotControl, newSensorSystem, buildSys, newWeaponSystem).go();
      }
      else {
        System.out.println("WARNING: Tried to build a ComCycler and Failed!");
        robotControl.turnOff();
      }
    }

    //If they aren't building they should turn off to save their upkeep
    robotControl.setIndicatorString(1, "Not Producing, turning off!");
    robotControl.turnOff();
  }


  /**
   * Builds FighterScouts and BuilderScouts
   * //TODO: pick a better method of deciding which one to build besides random,
   * ideally we should select between the two based on past build history and game turn
   * @return if one has been successfully made
   */
  protected boolean selBuildScouts() {
    robotControl.setIndicatorString(2, "selBuildScouts");
    double decider = rand.nextDouble();
    // the higher the decider the greater chance of producing Builders
    if (Clock.getRoundNum() < 700) {
      decider += .4;
    }
    else if (Clock.getRoundNum() > 1500){
      decider -= .3;
    }
    //wait for resources
    while(robotControl.getTeamResources() < PlayerConstants.MINIMUM_FLUX + BuildOrder.BUILDER_SCOUT_1.cost
            + BuildOrder.RECYCLER.cost + (Clock.getRoundNum()-150)/10 + BuildOrder.GUARD_TOWER_1.cost) {
      yield();
    }
    if(decider >= .5) {
      //build
      if(seqBuild(BuildOrder.BUILDER_SCOUT_1)) {
        return true;
      }
      else {
        return false;
      }
    }
    else {
      return seqBuildRandomFighterScout();
    }
  }


  /**
   * Builds a random fighter scout
   * @return if teh build was sucessfull
   */
  protected boolean seqBuildRandomFighterScout() {
    BuildOrder toBuild = BuildOrder.FIGHTER_SCOUT_1;

    switch(rand.nextInt(6)+1) {
      case 1:
        break;
      case 2:
        toBuild = BuildOrder.FIGHTER_SCOUT_2;
        break;
      case 3:
        toBuild = BuildOrder.FIGHTER_SCOUT_3;
        break;
      case 4:
        toBuild = BuildOrder.FIGHTER_SCOUT_4;
        break;
      case 5:
        toBuild = BuildOrder.FIGHTER_SCOUT_5;
        break;
      case 6:
        toBuild = BuildOrder.FIGHTER_SCOUT_6;
        break;
    }
    //wait until we have enough resources
    while(robotControl.getTeamResources() < PlayerConstants.MINIMUM_FLUX + toBuild.cost ) {
      yield();
    }
    //build
    if(seqBuild(toBuild)) {
      return true;
    }
    else {
      return false;
    }
  }

  /**
   * Takes a RobotBuildOrder and builds a robot to match the specification
   * @param buildOrder an array of Object, the first being the chasis the rest being components
   * @return if the build was successful
   */
  protected boolean seqBuild(BuildOrder toBuild) {
    if(!robotControl.getDirection().equals(Direction.WEST))
    {
      while(navSys.isActive()) {
        yield();
      }
      actTurn(Direction.WEST);
    }
    //keep rotating until we find a free square and have enough resources
    while(!navSys.canMove(robotControl.getDirection())
            || robotControl.getTeamResources() < PlayerConstants.MINIMUM_FLUX + toBuild.chassis.cost) {
      if(navSys.isActive()) {
        yield();
      }
      else {
        actTurn(robotControl.getDirection().rotateRight());
      }
    }
    //call to the buildSys to initiate the build sequence
    return seqBuild(toBuild,robotControl.getLocation().add(robotControl.getDirection()));

  }

}
