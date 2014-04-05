package com.philon.rpg.map.mo;
import java.util.LinkedList;

import com.philon.engine.FrameAnimation;
import com.philon.engine.MapObject;
import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;
import com.philon.rpg.util.RenderMapKey;
import com.philon.rpg.util.RpgUtil;

public abstract class RpgMapObj extends MapObject {
	public Vector collRect;
	public boolean isCollObj=true;

	public float luminance = 0;
	public boolean isSelectable = true;

	public boolean dirty = true; //-> do the following need to be updated by map?
	public LinkedList<Vector> currOccTiles;
	public RenderMapKey renderMapKey;
	public Vector baseScreenPos;
	public Vector baseImgScreenPos;
	public Vector baseImgScreenSize;

	public RpgMapObj() {
	  collRect = getCollRect();
	}

	public abstract Vector getCollRect();

	@Override
	public void setPosition(Vector newPosition) {
    super.setPosition(newPosition);

    dirty = true;
  }

  @Override
  public void turnToDirection( Vector targetDir ) {
    int oldDir = direction==null ? 0 : RpgUtil.getDir( direction );

    super.turnToDirection(targetDir);

    int newDir = RpgUtil.getDir( direction );
    int oldOrientation = RpgUtil.getOrientation(oldDir);
    int newOrientation = RpgUtil.getOrientation(newDir);
    if (oldOrientation!=newOrientation) { //turn collRect
      float tmp = collRect.y;
      collRect.x = collRect.y;
      collRect.y = tmp;
    }
    animation.setDir(newDir);
  }

  @Override
  public void setAnimation(FrameAnimation newAnimation) {
    super.setAnimation(newAnimation);

    animation.setDir( direction==null ? 0 : RpgUtil.getDir(direction) );
  }

	public void deleteObject() {
	  RpgUtil.removeMapObj(this);
	}

	public void setLuminance( float newLuminance ) {
		luminance = newLuminance;
		dirty = true;
	}

	public void interactTrigger(RpgMapObj objInteracting) {
  }

	/*
	 * return null if you don't want to save the object
	 */
	public RpgMapObjSaveData save() {
	  return new RpgMapObjSaveData(this);
	}

	/**
	 * Holds all information needed to restore this object when the map is reloaded.
	 * <p></p>
	 * Extend to save custom values. Make sure to override both constructors
	 */
	public static class RpgMapObjSaveData {
	  public Vector pos;
	  public Vector direction;
	  public Class<? extends RpgMapObj> objClass;

	  /**
	   * Constructor for Generator/manual use. Override to require additional values for instantation
	   */
	  public RpgMapObjSaveData(Class<? extends RpgMapObj> newObjClass, Vector newPos, Vector newDirection) {
	    objClass = newObjClass;
      pos = newPos.copy();
      direction = newDirection.copy();
    }

	  /**
	   * Constructor for saving existing MapObjs
	   */
	  public RpgMapObjSaveData(RpgMapObj obj) {
	    this(obj.getClass(), obj.pos, obj.direction);
	  }

	  /*
	   * Load this SaveData to recieve an RpgMapObj. Override to make sure all your data is loaded
	   */
	  public RpgMapObj load() {
	    RpgMapObj result = Util.instantiateClass(objClass);

	    result.setPosition(pos.copy());
	    result.turnToDirection(direction);

	    return result;
	  }
	}

}
