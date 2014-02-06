package com.philon.rpg.mos.wall;

import com.philon.engine.util.Vector;
import com.philon.rpg.mo.AbstractMapObj;

public abstract class AbstractWall extends AbstractMapObj {
  public boolean isBlock; //must be set before use of setWallType()
  
  @Override
  public Vector getCollRect() {
    return new Vector( 1, 1 );
  }
  
  public void setWallType(int newWallType) {
    switch (newWallType) {
      case WallData.WALLTYPE_PILLAR :
        setImage( getImgBlock() );
        break;
      case WallData.WALLTYPE_STRAIGHT :
        if (isBlock) {
          setImage( getImgBlockSingleSide() );
        } else {
          setImage( getImgWallStraight() );
        }
        break;
      case WallData.WALLTYPE_STUB :
        if (isBlock) {
          setImage( getImgBlockSingleSide() );
        } else {
          setImage( getImgWallStub() );
        }
        break;
      case WallData.WALLTYPE_CORNER :
        if (isBlock) {
          setImage( getImgBlockCorner() );
        } else {
          setImage( getImgWallCorner() );
        }
        break;
      case WallData.WALLTYPE_TRIPOD :
        if (isBlock) {
          setImage( getImgBlockSingleSide() );
        } else {
          setImage( getImgWallTripod() );
        }
        break;
      case WallData.WALLTYPE_CROSS :
        if (isBlock) {
          setImage( getImgBlockCross() );
        } else {
          setImage( getImgWallCross() );
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
