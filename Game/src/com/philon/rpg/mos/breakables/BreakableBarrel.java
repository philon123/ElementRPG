package com.philon.rpg.mos.breakables;

import com.philon.engine.util.Vector;
import com.philon.rpg.mo.BreakableMapObj;

public class BreakableBarrel extends BreakableMapObj {
  
  @Override
  public Vector getCollRect() {
    return new Vector(0.3f);
  }

  @Override
  public int getImgBreak() {
    return 342;
  }

  @Override
  public int getSouBreak() {
    return 67;
  }

  @Override
  public int getDropValue() {
    return 10;
  }

}
