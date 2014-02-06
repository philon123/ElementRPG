package com.philon.rpg.mos.wall;

public class CellarWall extends AbstractWall {
  @Override
  public int getImgBlock() {
    return 331;
  }
  
  @Override
  public int getImgBlockSingleSide() {
    return 328;
  }
  
  @Override
  public int getImgBlockCorner() {
    return 332;
  }
  
  @Override
  public int getImgBlockCross() {
    return 330;
  }
  
  @Override
  public int getImgWallStub() {
    return 329;
  }
  
  @Override
  public int getImgWallStraight() {
    return 299;
  }
  
  @Override
  public int getImgWallCorner() {
    return 305;
  }
  
  @Override
  public int getImgWallTripod() {
    return 306;
  }
  
  @Override
  public int getImgWallCross() {
    return 307;
  }
}
