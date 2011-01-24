
package team122;
import battlecode.common.*;

/**
 * Builds up the home base for global domination!
 * @author bovard
 */
public class RSBaseBuilder extends BuilderSensorRobotSystem {
  public RSBaseBuilder(RobotController robotControl, SensorSystem sensorSys, BuilderSystem buildSys) {
    super(robotControl, sensorSys, buildSys);
  }

  @Override
  public void go() {

    buildBaseScript();

    //go look for new mines
    new RSBuilderScout(robotControl, sensorSys, buildSys).go();

  }


  public void buildBaseScript() {

  }
}
