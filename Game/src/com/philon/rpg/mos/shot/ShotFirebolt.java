package com.philon.rpg.mos.shot;

public class ShotFirebolt extends AbstractShot {

  @Override
  public float getTilesPerSecond() {
    return 15;
  }

  @Override
  public int getSouAttack() {
    return 0;
  }

  @Override
  public int getImgMoving() {
    return 233;
  }

  @Override
  public int getImgDying() {
    return 0;
  }

  @Override
  public int getSouDie() {
    return 0;
  }

}
