package com.philon.rpg.mos.wall;

import com.philon.engine.FrameAnimation;
import com.philon.engine.util.Vector;
import com.philon.rpg.ImageData;
import com.philon.rpg.mo.GameMapObj;

public abstract class AbstractWall extends GameMapObj {
  public boolean isBlock; //must be set before use of setWallType()

  @Override
  public Vector getCollRect() {
    return new Vector( 1, 1 );
  }

  private void setImageHelper(int newImage) {
    setAnimation(new FrameAnimation(ImageData.images[newImage]));
  }

  public void setWallType(int newWallType) {
    switch (newWallType) {
      case WallData.WALLTYPE_PILLAR :
        setImageHelper( getImgBlock() );
        break;
      case WallData.WALLTYPE_STRAIGHT :
        if (isBlock) {
          setImageHelper( getImgBlockSingleSide() );
        } else {
          setImageHelper( getImgWallStraight() );
        }
        break;
      case WallData.WALLTYPE_STUB :
        if (isBlock) {
          setImageHelper( getImgBlockSingleSide() );
        } else {
          setImageHelper( getImgWallStub() );
        }
        break;
      case WallData.WALLTYPE_CORNER :
        if (isBlock) {
          setImageHelper( getImgBlockCorner() );
        } else {
          setImageHelper( getImgWallCorner() );
        }
        break;
      case WallData.WALLTYPE_TRIPOD :
        if (isBlock) {
          setImageHelper( getImgBlockSingleSide() );
        } else {
          setImageHelper( getImgWallTripod() );
        }
        break;
      case WallData.WALLTYPE_CROSS :
        if (isBlock) {
          setImageHelper( getImgBlockCross() );
        } else {
          setImageHelper( getImgWallCross() );
        }
        break;
    }
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
}
