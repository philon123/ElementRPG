package com.philon.rpg.mos.chest;

import com.philon.engine.util.Vector;

public class Chest1 extends AbstractChest {
  
  @Override
  public int getToggleImage() {
    return 334;
  }
  
  @Override
  public Vector getCollRect() {
    return new Vector(0.5f, 0.2f);
  }
  
  @Override
  public int getSouOpening() {
    return 63;
  }

  @Override
  public int getSouClosed() {
    return 0;
  }
  
}
