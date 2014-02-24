package com.philon.rpg.util;
import com.philon.engine.util.Vector;
import com.philon.rpg.mo.RpgMapObj;

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
}
