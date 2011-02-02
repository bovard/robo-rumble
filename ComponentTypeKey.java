
package team122;
import battlecode.common.*;

/**
 * Makes an int 'key' from a given list of Components, used to differentiate between robots
 * with different components
 * Note: it will not differentiate between different orders of components
 * @author bovard
 */
public class ComponentTypeKey {

  private static final int NUMBER_OF_COMPONENTS = 34;


  public static int getComponentTypeKey(ComponentController [] componentControls) {
    int [] key = getComponentKey(componentControls);
    return getKeySig(key);
  }

  /**
   * Makes a 'key' for the given components, ie a number that will be the same no matter
   * what the order of components
   * @param componentControls The components to add into the key
   * @return the int[] key
   */
  private static int[] getComponentKey(ComponentController[] componentControls) {
    int[] key = new int[NUMBER_OF_COMPONENTS];
    java.util.Arrays.fill(key, 0);

    for(ComponentController componentControl : componentControls) {
      key[getComponentTypePosition(componentControl.type())]++;
    }

    return key;
  }


  public static int getComponentTypeKey(ComponentType[] componentTypes) {
    int [] key = getComponentKey(componentTypes);
    return getKeySig(key);
  }

  /**
   * Makes a 'key' for the given components, ie a number that will be the same no matter
   * what the order of components
   * @param componentTypes The components to add into the key
   * @return the int[] key
   */
  private static int[] getComponentKey(ComponentType[] componentTypes) {
    int[] key = new int[NUMBER_OF_COMPONENTS];
    java.util.Arrays.fill(key, 0);

    for(ComponentType componentType : componentTypes) {
      key[getComponentTypePosition(componentType)]++;
    }

    return key;
  }

  /**
   * Takes a given ComponentType key and adds the imputted ComponentType array into the key
   * @param key the int[] key
   * @param componentTypes the Components to add
   * @return the int[] key
   */
  private static int[] addToComponentKey(int[] key, ComponentType[] componentTypes) {
    for(ComponentType componentType : componentTypes) {
      key[getComponentTypePosition(componentType)]++;
    }

    return key;
  }

  private static int getKeySig(int key[]) {
    return java.util.Arrays.hashCode(key);
  }


  public static int getComponentTypeKey(BuildOrder order) {
    int [] key = getComponentKey(order);
    return getKeySig(key);
  }

  /**
   * Makes a 'key' for the given components, ie a number that will be the same no matter
   * what the order of components
   * @param componentTypes The components to add into the key
   * @return the int[] key
   */
  private static int[] getComponentKey(BuildOrder order) {
    int[] key = new int[NUMBER_OF_COMPONENTS];
    java.util.Arrays.fill(key, 0);

    key[getComponentTypePosition(order.chassis.motor)]++;

    key = addToComponentKey(key, order.recyclerComponents);
    key = addToComponentKey(key, order.factoryComponents);
    key = addToComponentKey(key, order.armoryComponents);

    return key;
  }



  /**
   * finds the assigned position of each ComponentType in the key int[] and returns it
   * @param component the component to find the position for
   * @return the place in the int[] key array
   */
  private static int getComponentTypePosition(ComponentType component) {
    if (component.equals(ComponentType.ANTENNA)) {
      return 0;
    }
    else if (component.equals(ComponentType.ARMORY)) {
      return 1;
    }
    else if (component.equals(ComponentType.BEAM)) {
      return 2;
    }
    else if (component.equals(ComponentType.BLASTER)) {
      return 3;
    }
    else if (component.equals(ComponentType.BUG)) {
      return 4;
    }
    else if (component.equals(ComponentType.BUILDING_MOTOR)) {
      return 5;
    }
    else if (component.equals(ComponentType.BUILDING_SENSOR)) {
      return 6;
    }
    else if (component.equals(ComponentType.CONSTRUCTOR)) {
      return 7;
    }
    else if (component.equals(ComponentType.DISH)) {
      return 8;
    }
    else if (component.equals(ComponentType.DROPSHIP)) {
      return 9;
    }
    else if (component.equals(ComponentType.DUMMY)) {
      return 10;
    }
    else if (component.equals(ComponentType.FACTORY)) {
      return 11;
    }
    else if (component.equals(ComponentType.FLYING_MOTOR)) {
      return 12;
    }
    else if (component.equals(ComponentType.HAMMER)) {
      return 13;
    }
    else if (component.equals(ComponentType.HARDENED)) {
      return 14;
    }
    else if (component.equals(ComponentType.IRON)) {
      return 15;
    }
    else if (component.equals(ComponentType.JUMP)) {
      return 16;
    }
    else if (component.equals(ComponentType.LARGE_MOTOR)) {
      return 17;
    }
    else if (component.equals(ComponentType.MEDIC)) {
      return 18;
    }
    else if (component.equals(ComponentType.MEDIUM_MOTOR)) {
      return 19;
    }
    else if (component.equals(ComponentType.NETWORK)) {
      return 20;
    }
    else if (component.equals(ComponentType.PLASMA)) {
      return 21;
    }
    else if (component.equals(ComponentType.PLATING)) {
      return 22;
    }
    else if (component.equals(ComponentType.PROCESSOR)) {
      return 23;
    }
    else if (component.equals(ComponentType.RADAR)) {
      return 24;
    }
    else if (component.equals(ComponentType.RAILGUN)) {
      return 25;
    }
    else if (component.equals(ComponentType.RECYCLER)) {
      return 26;
    }
    else if (component.equals(ComponentType.REGEN)) {
      return 27;
    }
    else if (component.equals(ComponentType.SATELLITE)) {
      return 28;
    }
    else if (component.equals(ComponentType.SHIELD)) {
      return 29;
    }
    else if (component.equals(ComponentType.SIGHT)) {
      return 30;
    }
    else if (component.equals(ComponentType.SMALL_MOTOR)) {
      return 31;
    }
    else if (component.equals(ComponentType.SMG)) {
      return 32;
    }
    else if (component.equals(ComponentType.TELESCOPE)) {
      return 33;
    }
    System.out.println("WARNING: ComponentType not found in getComponentTypePosition");
    return -1;
  }
}
