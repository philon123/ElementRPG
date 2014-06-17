package com.philon.rpg.mos.shot;

public class ShotChargedBolt extends AbstractShot {

  @Override
  public float getTilesPerSecond() {
    return 7;
  }

  @Override
  public int getSouAttack() {
    return 0;
  }

  @Override
  public int getImgMoving() {
    return 232;
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
