package com.philon.rpg.mo;
import java.util.LinkedList;

import com.philon.engine.AnimImage;
import com.philon.engine.Game;
import com.philon.engine.util.Vector;
import com.philon.rpg.ImageData;
import com.philon.rpg.RpgGame;
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
import com.philon.rpg.util.GameImage;
import com.philon.rpg.util.GameUtil;
import com.philon.rpg.util.RenderMapKey;

public abstract class AbstractMapObj extends AnimImage {
  public Vector imgTileSize=new Vector();
  public int currAnimFrame;
  public Vector imgScale=new Vector(1);
	public int dir;
	
	public LinkedList<Vector> currOccTiles;
	public LinkedList<Vector> oldOccTiles;
	public RenderMapKey renderMapKey;
	public Vector collRect;
	public boolean isCollObj=true;
	public Vector lastOffset=new Vector();

	public float luminance = 0;

	public Vector basePixPos;
	public Vector baseImgPixPos;
	public Vector baseImgPixSize;

	public AbstractMapObj() {
	  collRect = getCollRect();
	}

	public abstract Vector getCollRect();

	@Override
	public void setPosition(Vector newPosition) {
    super.setPosition(newPosition);
    
    updateOccTiles();
    bakeBasePosition();
    updateRenderMapKey();
  }

	public void deleteObject() {
		oldOccTiles=currOccTiles;
		currOccTiles=null;
		RpgGame.inst.gMap.updateOccTiles(this);
		if (renderMapKey!=null) RpgGame.inst.gGraphics.renderMap.remove(renderMapKey);
		if (luminance!=0) RpgGame.inst.gGraphics.removeDynamicLightSource(this);
	}

	public void bakeBasePosition() {
		basePixPos = RpgGame.inst.gGraphics.getBasePixPosByTilePos( pos );
		baseImgPixSize = RpgGame.inst.gGraphics.getPixSizeByTileSize( imgTileSize );

		Vector tmpPixOffset = baseImgPixSize.copy().mulScalarInst(-1);
		tmpPixOffset.x *= 0.5;
		tmpPixOffset.y += RpgGame.inst.gGraphics.getPixSizeByTileSize(new Vector(0.5f)).y;

		baseImgPixPos = Vector.add( basePixPos, tmpPixOffset );
	}

	public void updateRenderMapKey() {
		RenderMapKey newRenderMapKey = new RenderMapKey(pos, hashCode());

		if( renderMapKey==null ) {
			renderMapKey = newRenderMapKey;
			RpgGame.inst.gGraphics.renderMap.put(newRenderMapKey, this);
		} else if( !newRenderMapKey.equals(renderMapKey) ) {
			RpgGame.inst.gGraphics.renderMap.remove(renderMapKey);
			RpgGame.inst.gGraphics.renderMap.put(newRenderMapKey, this);
			renderMapKey = newRenderMapKey;
		}
	}
	
	public void setImage( int newImgID, int newAnimFrame ) {
    super.setImage(ImageData.images[newImgID], newAnimFrame);
    
    imgAnimFrames /= 8;
    imgTileSize = ((GameImage)image).tileSize.copy().mulInst(imgScale);
    currFrame = dir*imgAnimFrames + newAnimFrame;
  }

	@Override
	public void updateAnimFrame() { //different logic for multidirectional animation
		if (imgAnimFrames==0) return;

		currAnimFrame = (int) (( ((Game.currFrame-currAnimStart) / (imgAnimLen*1.0)) * imgAnimFrames ) % imgAnimFrames);
		if (currAnimReversed) currAnimFrame=(imgAnimFrames-1)-currAnimFrame;

		currFrame = dir*imgAnimFrames + currAnimFrame;
	}
	
	public void setImage( int newImgID ) {
		setImage(newImgID, 0);
	}

	public void setImgScale( float scaleFactor ) {
		imgScale = new Vector(scaleFactor);
		imgTileSize.mulInst(imgScale);
		bakeBasePosition();
	}

	@Override
	public void startAnim( int newAnimLen, boolean reverseAnim ) { //different logic for multidirectional animation
		imgAnimLen = newAnimLen;
		currAnimStart = RpgGame.currFrame;
		currAnimFrame = 0;
		currAnimReversed = reverseAnim;
	}
	
	@Override
	public void turnToDirection( Vector targetDir ) {
	  super.turnToDirection(targetDir);
	  
		int newDir = GameUtil.getDir( direction );
		int oldOrientation = getOrientation(dir);
		int newOrientation = getOrientation(newDir);
		if (oldOrientation!=newOrientation) { //turn collRect
		  float tmp = collRect.y;
		  collRect.x = collRect.y;
		  collRect.y = tmp;
		}
		dir = newDir;
		
		updateAnimFrame();
	}
	
	public int getOrientation(int newDir) { //returns 0 for facing up/down, 1 for facing left/right. favores 0
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

	public static LinkedList<Vector> getOccTilesByRect( Vector newRectPos, Vector newRectSize ) {
		LinkedList<Vector> result=new LinkedList<Vector>();

		Vector vmin = Vector.sub( newRectPos, newRectSize.copy().mulScalarInst(0.5f) ).roundAllInst();
		Vector vmax = Vector.add( newRectPos, newRectSize.copy().mulScalarInst(0.5f) ).subInst(new Vector(0.001f)).roundAllInst();
		if (vmin.x<0) vmin.x=0;
		if (vmin.y<0) vmin.y=0;
		if (vmax.x>RpgGame.inst.gMap.gridSize.x-1) vmax.x=RpgGame.inst.gMap.gridSize.x-1;
		if (vmax.y>RpgGame.inst.gMap.gridSize.y-1) vmax.y=RpgGame.inst.gMap.gridSize.y-1;

		for( int x = (int) vmin.x; x <= vmax.x; x++ ) {
			for( int y = (int) vmin.y; y <= vmax.y; y++ ) {
				result.addLast(new Vector(x, y));
			}
		}

		return result;
	}

	public void updateOccTiles() {
		LinkedList<Vector> newOccTiles = getOccTilesByRect( pos, collRect );
		if( currOccTiles==null ) {
			currOccTiles = newOccTiles;
		} else if( !currOccTiles.equals(newOccTiles) ) {
			oldOccTiles = currOccTiles;
			currOccTiles = newOccTiles;
		} else {
		  return;
		}
		RpgGame.inst.gMap.updateOccTiles(this);
	}

	public void setLuminance( float newLuminance ) {
		luminance = newLuminance;
		if( luminance>0 ) {
			RpgGame.inst.gGraphics.insertStaticLightSource(this);
		}
	}

	public static LinkedList<AbstractMapObj> filterList( LinkedList<AbstractMapObj> moList, 
			boolean keepPlayer, 
			boolean keepEnemy, 
			boolean keepItem, 
			boolean keepShot, 
			boolean keepChest, 
			boolean keepBrekable ) {
		LinkedList<AbstractMapObj> result=new LinkedList<AbstractMapObj>();

		if (moList==null) return null;

		for( AbstractMapObj tmpMo : moList ) {
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
          (tmpMo instanceof BreakableMapObj && keepBrekable) ) {
		    result.addLast(tmpMo);
		  }
		}

		if (result==null || result.size()==0) return null;
		return result;
	}

	public static boolean compareLists( LinkedList<AbstractMapObj> moList1, LinkedList<AbstractMapObj> moList2 ) {
		if (moList1==null && moList2==null) return true;
		if (moList1==null || moList2==null) return false;
		if (moList1.size() != moList2.size()) return false;

		for (int i=0; i<moList1.size(); i++) {
			if( moList1.get(i) != moList2.get(i) ) {
				return false;
			}
		}

		return true;
	}

}
