
package team122;
import battlecode.common.*;

/**
 *
 * @author bovard
 */
public class RSFactory extends BuilderSensorRobotSystem {


  private MapLocation constructionLocation;
  private int lastWarrior = 0;


  public RSFactory(RobotController robotControl, SensorSystem sensorSys, BuilderSystem buildSys) {
    super(robotControl, sensorSys, buildSys);
    robotControl.setIndicatorString(0, "RSFactory");
    constructionLocation = findBaseBuilder();
  }


  @Override
  public void go() {
    if(constructionLocation != null) {
      while(true) {
        //wait for money!
        while(robotControl.getTeamResources() < BuildOrder.RECYCLER.cost + BuildOrder.HEAVY_WARRIOR_1.cost
                + PlayerConstants.MINIMUM_FLUX || buildSys.isActive()) {
          yield();
        }

        //build a heavy if we can
        Robot bot = (Robot)sensorSys.senseObjectAtLocation(constructionLocation, RobotLevel.ON_GROUND);
        if (bot==null && Clock.getRoundNum() > lastWarrior + PlayerConstants.HEAVY_COOLDOWN) {
          while(robotControl.getTeamResources() < BuildOrder.RECYCLER.cost + BuildOrder.HEAVY_WARRIOR_1.cost
                + PlayerConstants.MINIMUM_FLUX) {
            yield();
          }
          
          robotControl.setIndicatorString(1, "seqBuild HW");
          lastWarrior = Clock.getRoundNum();
          seqBuild(BuildOrder.HEAVY_WARRIOR_1, constructionLocation);
        }
      }
    }
    robotControl.turnOff();
  }

  
}
