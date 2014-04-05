package com.philon.rpg.mos.shot;

import com.philon.engine.util.Vector;
import com.philon.rpg.map.mo.state.AbstractMapObjState;

public class ShotMelee extends AbstractShot {

  @Override
  public Class<? extends AbstractMapObjState> getDefaultState() {
    return StateMovingStraight.class; //collision ony checked while moving
  }

  @Override
  public int getSouAttack() {
    return 0;
  }

  @Override
  public int getImgIdle() {
    return 0;
  }

  @Override
  public int getImgMoving() {
    return 0;
  }

  @Override
  public int getImgDying() {
    return 325;
  }

  @Override
  public int getSouDie() {
    return 0;
  }

  @Override
  public Vector getCollRect() {
    return new Vector(0.5f);
  }

}
