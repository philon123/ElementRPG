package com.philon.rpg.mos.wall;

import com.philon.engine.Data;
import com.philon.engine.FrameAnimation;
import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;
import com.philon.rpg.map.generator.MapGenerator;
import com.philon.rpg.map.mo.RpgMapObj;

public abstract class AbstractWall extends RpgMapObj {
  public boolean isBlock; //must be set before use of setWallType()
  public int wallType;

  public AbstractWall() {
    super();

    isSelectable = false;
  }

  @Override
  public Vector getCollRect() {
    return new Vector( 1, 1 );
  }

  public void setImageByWallType(int newWallType) {
    wallType = newWallType;

    switch (newWallType) {
      case MapGenerator.WALLTYPE_PILLAR :
        setImageHelper( getImgBlock() );
        break;
      case MapGenerator.WALLTYPE_STRAIGHT :
        if (isBlock) {
          setImageHelper( getImgBlockSingleSide() );
        } else {
          setImageHelper( getImgWallStraight() );
        }
        break;
      case MapGenerator.WALLTYPE_STUB :
        if (isBlock) {
          setImageHelper( getImgBlockSingleSide() );
        } else {
          setImageHelper( getImgWallStub() );
        }
        break;
      case MapGenerator.WALLTYPE_CORNER :
        if (isBlock) {
          setImageHelper( getImgBlockCorner() );
        } else {
          setImageHelper( getImgWallCorner() );
        }
        break;
      case MapGenerator.WALLTYPE_TRIPOD :
        if (isBlock) {
          setImageHelper( getImgBlockSingleSide() );
        } else {
          setImageHelper( getImgWallTripod() );
        }
        break;
      case MapGenerator.WALLTYPE_CROSS :
        if (isBlock) {
          setImageHelper( getImgBlockCross() );
        } else {
          setImageHelper( getImgWallCross() );
        }
        break;
    }
  }

  private void setImageHelper(int newImage) {
    setAnimation(new FrameAnimation(Data.textures.get(newImage)));
  }

  public abstract int getImgBlock();
  public abstract int getImgBlockSingleSide();
  public abstract int getImgBlockCorner();
  public abstract int getImgBlockCross();
  public abstract int getImgWallStub();
  public abstract int getImgWallStraight();
  public abstract int getImgWallCorner();
  public abstract int getImgWallTripod();
  public abstract int getImgWallCross();

  public WallSaveData save() {
    return new WallSaveData(this);
  }

  public static class WallSaveData extends RpgMapObjSaveData {
    public boolean isBlock;
    public int wallType;

    public WallSaveData(Class<? extends AbstractWall> newObjClass, Vector newPos, Vector newDirection, boolean newIsBlock, int newWallType) {
      super(newObjClass, newPos, newDirection);

      isBlock = newIsBlock;
      wallType = newWallType;
    }

    public WallSaveData(AbstractWall obj) {
      super(obj);
      isBlock = obj.isBlock;
      wallType = obj.wallType;
    }

    @Override
    public RpgMapObj load() { //custom load because image is not set in constructor (image has to be set first)
      AbstractWall result = (AbstractWall)Util.instantiateClass(objClass);

      result.isBlock = isBlock;
      result.setImageByWallType(wallType);
      result.setPosition(pos);
      result.turnToDirection(direction);

      return result;
    }
  }
}
