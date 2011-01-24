
package team122;
import battlecode.common.*;

/**
 *
 * @author bovard
 */
public class RSArmory extends BuilderSensorRobotSystem {

  private MapLocation constructionLocation;


  public RSArmory(RobotController robotControl, SensorSystem sensorSys, BuilderSystem buildSys) {
    
    super(robotControl, sensorSys, buildSys);
    robotControl.setIndicatorString(0, "RSFactory");
    constructionLocation = findBaseBuilder();
  }

  @Override
  public void go() {
    if(constructionLocation != null) {
      while(true) {
        //wait for money!
        while(robotControl.getTeamResources() < BuildOrder.RECYCLER.cost + BuildOrder.FLYING_BUILDER_SCOUT_1.cost
                + PlayerConstants.MINIMUM_FLUX || buildSys.isActive()) {
          yield();
        }
        //check to see if we're trying to build a heavy bot and if we are build the factor components
        Robot bot = (Robot)sensorSys.senseObjectAtLocation(constructionLocation, RobotLevel.ON_GROUND);
        if(bot != null) {
          RobotInfo info = sensorSys.sensorRobotInfo(bot);
          if (!info.on && info.maxHp == 40) {
            //if a heavy turned-off bot is detected, build!
            while(robotControl.getTeamResources() < BuildOrder.RECYCLER.cost + BuildOrder.HEAVY_WARRIOR_1.cost
                + PlayerConstants.MINIMUM_FLUX) {
              yield();
            }
            robotControl.setIndicatorString(1, "seqBuild HW");
            System.out.println("trying to build a heavy");
            seqBuild(BuildOrder.HEAVY_WARRIOR_1, constructionLocation);
          }
        }
        //build a flying scout builder if one isn't already there
        bot = (Robot)sensorSys.senseObjectAtLocation(constructionLocation, RobotLevel.IN_AIR);
        if (bot==null) {
          robotControl.setIndicatorString(1, "seqBuild FBS");
          System.out.println("trying to build a scout");
          seqBuild(BuildOrder.FLYING_BUILDER_SCOUT_1, constructionLocation);
        }
      }
    }
    robotControl.turnOff();
  }



}