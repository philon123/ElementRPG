package com.philon.rpg.mos.door;

import com.philon.engine.util.Vector;

public class Door1 extends AbstractDoor {

  @Override
  public Vector getCollRect() {
    return new Vector(1, 0.7f);
  }
  
  @Override
  public int getToggleImage() {
    return 333;
  }
  
  @Override
  public int getSouOpening() {
    return 64;
  }
  
  @Override
  public int getSouClosed() {
    return 65;
  }
  
}
