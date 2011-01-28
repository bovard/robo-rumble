
package team122;
import battlecode.common.*;

/**
 *
 * @author bovard
 */
public class RSArmory extends BuilderSensorRobotSystem {


  public RSArmory(RobotController robotControl, SensorSystem sensorSys, BuilderSystem buildSys) {
    
    super(robotControl, sensorSys, buildSys);
    robotControl.setIndicatorString(2, "RSArmory");
    comSys.setFilter(new int[] {1,1,0});
  }

  @Override
  public void go() {
    while(true) {
      yield();
      if(gameEvents.hasDirective()) {
        robotControl.setIndicatorString(1, "Building a directive!");
        Message directive = comSys.getLastDirective(PlayerConstants.MESSAGE_BUILD_DIRECTIVE);
        MapLocation location = comSys.getMapLocationFromBuildDirective(directive);
        if(location.isAdjacentTo(birthPlace)) {
          BuildOrder order = BuildOrderID.getBuildOrderFromID(comSys.getBuildOrderIDFromBuildDirective(directive));
          while(robotControl.getTeamResources() < BuildOrder.RECYCLER.cost + order.cost + 2*PlayerConstants.MINIMUM_FLUX) {
            yield();
          }
          seqBuild(order, location);
        }
      }
    }
  }



}
