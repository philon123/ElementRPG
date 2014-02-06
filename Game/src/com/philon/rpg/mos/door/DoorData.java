package com.philon.rpg.mos.door;

public class DoorData {
  
  public static AbstractDoor createDoor(Class<? extends AbstractDoor> clazz) {
    AbstractDoor result = null;
    
    try {
      result = clazz.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    
    return result;
  }
  
//  public static AbstractDoor loadDoor( DoorSaveData dsd ) {
//    AbstractDoor result = createDoor(dsd.doorClass);
//    
//    
//    return result;
//  }
  
}
