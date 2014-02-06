package com.philon.rpg.mos.breakables;

import com.philon.engine.util.Vector;
import com.philon.rpg.mo.BreakableMapObj;

public class BreakableVase extends BreakableMapObj {
  
  @Override
  public Vector getCollRect() {
    return new Vector(0.3f);
  }

  @Override
  public int getImgBreak() {
    return 343;
  }

  @Override
  public int getSouBreak() {
    return 66;
  }

  @Override
  public int getDropValue() {
    return 10;
  }

}
