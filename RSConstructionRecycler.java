
package team122;
import battlecode.common.*;

/**
 *
 * @author bovard
 */
public class RSConstructionRecycler extends BuilderSensorRobotSystem {

  private MapLocation constructionLocation;
  private int lastScout = 0;
  private int lastScoutBuilder = 0;
  private int lastWarrior = 0;

  public RSConstructionRecycler(RobotController robotControl, SensorSystem sensorSys,
          BuilderSystem buildSys) {
    super(robotControl, sensorSys, buildSys);
    robotControl.setIndicatorString(0, "RSConstructionRecycler");
    constructionLocation = findBaseBuilder();
  }

  @Override
  public void go() {
    if(constructionLocation != null) {
      while(true) {
        //wait for money!
        while(robotControl.getTeamResources() < BuildOrder.RECYCLER.cost + BuildOrder.FLYING_BUILDER_SCOUT_1.cost
                + PlayerConstants.MINIMUM_FLUX || buildSys.isActive()
                || !gameEvents.isFluxRegenAbove(PlayerConstants.MINIMUM_FLUX_REGEN + BuildOrder.HEAVY_WARRIOR_1.chassis.upkeep)) {
          yield();
        }

        //build on a flying scout if one is there
        Robot bot = (Robot)sensorSys.senseObjectAtLocation(constructionLocation, RobotLevel.IN_AIR);
        if (bot!=null && Clock.getRoundNum() > lastScoutBuilder + PlayerConstants.BUILDER_SCOUT_COOLDOWN) {

          RobotInfo info = sensorSys.sensorRobotInfo(bot);
          if (!info.on && info.maxHp == 10) {
            System.out.println("here");
            while(robotControl.getTeamResources() < BuildOrder.RECYCLER.cost + BuildOrder.FLYING_BUILDER_SCOUT_1.cost
                  + 2 * PlayerConstants.MINIMUM_FLUX) {
              yield();
            }
            robotControl.setIndicatorString(1, "seqBuild FBS");
            lastScoutBuilder = Clock.getRoundNum();
            seqBuild(BuildOrder.FLYING_BUILDER_SCOUT_1, constructionLocation);
            try {
              robotControl.turnOn(constructionLocation, RobotLevel.IN_AIR);
            } catch (Exception e) {
              System.out.println("caught exception:");
              e.printStackTrace();
            }
          }
        }
        /**
        //build on a scout if one is there
        Robot bot = (Robot)sensorSys.senseObjectAtLocation(constructionLocation, RobotLevel.IN_AIR);
        if (bot!=null && Clock.getRoundNum() > lastScout + PlayerConstants.SCOUT_COOLDOWN) {

          RobotInfo info = sensorSys.sensorRobotInfo(bot);
          if (info.on && info.maxHp == 10) {
            while(robotControl.getTeamResources() < BuildOrder.RECYCLER.cost + BuildOrder.FLYING_SCOUT_1.cost
                  + 2 * PlayerConstants.MINIMUM_FLUX) {
              yield();
            }
            robotControl.setIndicatorString(1, "seqBuild FBS");
            lastScout = Clock.getRoundNum();
            seqBuild(BuildOrder.FLYING_SCOUT_1, constructionLocation);
            try {
              robotControl.turnOn(constructionLocation, RobotLevel.IN_AIR);
            } catch (Exception e) {
              System.out.println("caught exception:");
              e.printStackTrace();
            }
          }
        }
        */

        //check to see if we're trying to build a heavy bot and if we are build the factor components
        bot = (Robot)sensorSys.senseObjectAtLocation(constructionLocation, RobotLevel.ON_GROUND);
        if(bot != null && Clock.getRoundNum() > lastWarrior + PlayerConstants.HEAVY_COOLDOWN) {

          RobotInfo info = sensorSys.sensorRobotInfo(bot);
          if (!info.on && info.maxHp == 40) {
            //if a heavy turned-off bot is detected, build!
            while(robotControl.getTeamResources() < BuildOrder.RECYCLER.cost + BuildOrder.HEAVY_WARRIOR_1.cost
                + 2 * PlayerConstants.MINIMUM_FLUX) {
              yield();
            }
            robotControl.setIndicatorString(1, "seqBuild HW");
            lastWarrior = Clock.getRoundNum();
            seqBuild(BuildOrder.HEAVY_WARRIOR_1, constructionLocation);
            try {
              robotControl.turnOn(constructionLocation, RobotLevel.ON_GROUND);
            } catch (Exception e) {
              System.out.println("caught exception:");
              e.printStackTrace();
            }
          }
        }
       
      }
    }
    robotControl.turnOff();
  }

  
}
