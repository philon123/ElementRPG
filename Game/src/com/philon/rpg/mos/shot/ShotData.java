package com.philon.rpg.mos.shot;

public class ShotData {

  public static AbstractShot createShot(Class<? extends AbstractShot> clazz) {
	  AbstractShot result = null;
	  try {
      result = clazz.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
	  return result;
	}
	
	public static AbstractShot cloneShot(AbstractShot fromShot) {
	  AbstractShot newShot = createShot(fromShot.getClass());
	  newShot.tilesPerSecond = fromShot.tilesPerSecond;
	  newShot.setPosition(fromShot.pos);
	  newShot.setTarget( fromShot.currTarget, fromShot.currTargetPos );
	  
    return newShot;
  }
}
