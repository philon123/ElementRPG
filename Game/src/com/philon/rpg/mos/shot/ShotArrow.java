package com.philon.rpg.mos.shot;

public class ShotArrow extends AbstractShot {

  @Override
  public float getTilesPerSecond() {
    return 10;
  }

  @Override
  public int getSouAttack() {
    return 0;
  }

  @Override
  public int getImgMoving() {
    return 235;
  }

  @Override
  public int getImgDying() {
    return 235;
  }

  @Override
  public int getSouDie() {
    return 0;
  }

}
