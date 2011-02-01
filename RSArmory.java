
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
    comSys.setAcceptMessageType(PlayerConstants.MESSAGE_INFO);
    comSys.setAcceptMessageType(PlayerConstants.MESSAGE_BUILD_DIRECTIVE);
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
          if(order.chassisBuilder != buildSys.type()) {
            while(sensorSys.senseObjectAtLocation(location, order.chassis.level)==null) {
              yield();
            }
          }
          while(robotControl.getTeamResources() < BuildOrder.RECYCLER.cost + order.cost + PlayerConstants.MINIMUM_FLUX) {
            yield();
          }
          seqBuild(order, location);
        }
      }
    }
  }



}
