/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package team122;

/**
 * WeaponModes should be used to make target priority or prevent firing at certain things
 * This should make target priority easier to manage (hopefully)
 * ex: robots that are sent to take out the enemies building, robots looking to kill enemy
 * builders, long range robots targetting medics first, etc...
 *
 * (not sure this is a great idea...)
 *
 * @author bovard
 */
public final class WeaponMode {
  public static final int HOLD_FIRE = 0;
  public static final int RAIDER = 1;
  public static final int OPEN_FIRE = 2;
  public static final int LONG_RANGE = 3;
  public static final int SHORT_RANGE = 4;
  public static final int ECONOMY = 5;
}
