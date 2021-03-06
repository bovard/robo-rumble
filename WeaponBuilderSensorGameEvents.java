
package team122;
import battlecode.common.*;

/**
 * The GameEvents class for the FighterBuidlerSensorRobotClass
 * @author bovard
 */
public class WeaponBuilderSensorGameEvents extends BuilderSensorGameEvents {

  public WeaponBuilderSensorGameEvents(RobotController robotControl, CommunicationsSystem comSys,
          SensorSystem sensorSys) {
    super(robotControl, comSys, sensorSys);
  }

  /**
   * Overridden to add back calculating the seeDebirs game event
   * Here we should calculate all the game events that will matter to the BuilderSensorRobotSystem
   */
  @Override
  public void calcGameEvents() {
    //System.out.println("In WeaponBuilderSensorGameEvents.calcGameEvents at bytecode: "+Clock.getBytecodeNum());
    calcLostHealth();
    //Note: calcRecentlyLostHeath() must be called AFTER calcLostHealth()
    calcRecentlyLostHealth();
    calcSeeMine();
    calcSeeEnemy();
    calcSeeDebris();
    calcFluxRegen();
    //System.out.println("Leaving at bytecode: "+Clock.getBytecodeNum());
  }
}
