package com.philon.rpg.util;
import java.util.LinkedList;

import com.philon.engine.util.Vector;
import com.philon.rpg.map.mo.BreakableMapObj;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.mos.chest.AbstractChest;
import com.philon.rpg.mos.door.AbstractDoor;
import com.philon.rpg.mos.enemy.AbstractEnemy;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.light.Firestand;
import com.philon.rpg.mos.player.AbstractChar;
import com.philon.rpg.mos.shot.AbstractShot;
import com.philon.rpg.mos.stairs.StairsDown;
import com.philon.rpg.mos.stairs.StairsUp;
import com.philon.rpg.mos.wall.AbstractWall;

public class RpgUtil {
	public float sqrMap[];
	public float sinMap[];
	public float cosMap[];

	//----------

	public RpgUtil() {
		//init sqrMap
		sqrMap=new float[100000];
		for( int i = 0; i < sqrMap.length; i++ ) {
			sqrMap[i] = (float) Math.sqrt(i);
		}

		//init sinMap, cosMap
		sinMap = new float[360];
		cosMap = new float[360];
		for( int i = 0; i <= 359; i++ ) {
			sinMap[i] = (float) Math.sin(i);
			cosMap[i] = (float) Math.cos(i);
		}
	}

	//----------

	public static int getDir( Vector newV ) { //returns 0-7, zero is 'down', 45degree increments, clockwise
		if (newV.isAllEqual(new Vector())) return 0;
		float rot = (newV.getRotationDegInst()+45) % 360;
//		System.out.println(newV.getRotationDegInst());
		return(( (int)( rot/45.0 + 0.5 ) - 2 + 8 ) % 8);
	}

	public static int getOrientation(int newDir) { //returns 0 for facing up/down, 1 for facing left/right. favores 0
    if (newDir==0) return 0;
    if (newDir==1) return 0;
    if (newDir==2) return 0;
    if (newDir==3) return 1;
    if (newDir==4) return 1;
    if (newDir==5) return 0;
    if (newDir==6) return 0;
    if (newDir==7) return 0;
    return 0;
  }

	//----------

	public static boolean isPixelInMapObj( RpgMapObj newMo, Vector newPixel ) {
	  return true; //TODO isPixelInMapObj
//		if (newMo.baseImgPixPos==null || newMo.image==0 || newMo.baseImgPixSize==null) return false;
//
//		Vector screenPixel = newPixel.copy().subInst( newMo.baseImgPixPos.copy().addInst(Game.inst.gGraphics.currOffset) );
//		if (screenPixel.isEitherSmaller(new Vector(0))) return false;
//		if (screenPixel.isEitherLarger( newMo.baseImgPixSize)) return false;
//
//		//pixel is in image rect
//		TextureRegion currImg = ImageData.images[newMo.image].frames[newMo.currFrame];
//		Vector imagePixel = Vector.div( screenPixel, newMo.baseImgPixSize ).mulInst( new Vector(currImg.getRegionWidth(), currImg.getRegionHeight()) );
//		if (argb( currImg.readPixel((int)imagePixel.x, (int)imagePixel.y) )[0] != 0) return true;
//		return false;
	}

	public float getTileDistance(Vector tile1, Vector tile2) {
	  int sqrDist = (int) (Math.pow(tile2.x-tile1.x, 2) + Math.pow(tile2.y-tile1.y, 2));
    return sqrMap[sqrDist];
	}

  public static LinkedList<RpgMapObj> filterList( LinkedList<RpgMapObj> moList,
  		boolean keepPlayer,
  		boolean keepEnemy,
  		boolean keepItem,
  		boolean keepShot,
  		boolean keepChest,
  		boolean keepBreakable) {
  	LinkedList<RpgMapObj> result=new LinkedList<RpgMapObj>();

  	if (moList==null) return null;

  	for( RpgMapObj tmpMo : moList ) {
  	  if (tmpMo instanceof AbstractWall ||
  	      tmpMo instanceof AbstractDoor ||
  	      tmpMo instanceof StairsUp ||
  	      tmpMo instanceof StairsDown ||
  	      tmpMo instanceof Firestand ||
  	      (tmpMo instanceof AbstractChar && keepPlayer) ||
          (tmpMo instanceof AbstractEnemy && keepEnemy) ||
          (tmpMo instanceof AbstractItem && keepItem) ||
          (tmpMo instanceof AbstractShot && keepShot) ||
          (tmpMo instanceof AbstractChest && keepChest) ||
          (tmpMo instanceof BreakableMapObj && keepBreakable) ) {
  	    result.addLast(tmpMo);
  	  }
  	}

  	if (result==null || result.size()==0) return null;
  	return result;
  }

  /**
   * tries to instantiate a class without constructor parameters
   */
  public static <T> T instantiateClass(Class<T> clazz) {
    try {
      return clazz.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }
}
