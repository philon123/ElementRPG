package com.philon.rpg.mos.chest;

public class ChestData {
  
  public static AbstractChest createChest(Class<? extends AbstractChest> clazz) {
    AbstractChest result = null;
    
    try {
      result = clazz.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    
    return result;
  }
  
//  public static AbstractChest loadChar( ChestSaveData csd ) {
//    AbstractChest result = createChest(csd.chestClass);
//    
//    
//    return result;
//  }
  
}
