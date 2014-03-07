package com.philon.rpg.mos.shot;

import com.philon.engine.util.Vector;
import com.philon.rpg.map.mo.state.AbstractMapObjState;

public class ShotFire extends AbstractShot {

  @Override
  public Class<? extends AbstractMapObjState> getDefaultState() {
    return StateIdle.class;
  }
  
  @Override
  public int getSouAttack() {
    return 0;
  }
  
  @Override
  public int getImgIdle() {
    return 236;
  }
  
  @Override
  public Vector getCollRect() {
    return new Vector(0.7f);
  }

  @Override
  public int getImgMoving() {
    return 0;
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
