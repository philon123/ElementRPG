package com.philon.rpg.mos.item.category;

import com.philon.engine.util.Vector;


public abstract class ScrollItem extends ConsumableItem {
  
  @Override
  public int getSouDrop() {
    return 42;
  }

  @Override
  public int getSouFlip() {
    return 30;
  }

  @Override
  public int getImgMap() {
    return 319;
  }
  
  @Override
  public int getBaseDurability() {
    return 1;
  }

  @Override
  public Vector getInvSize() {
    return new Vector(1);
  }
  
  @Override
  public int getSouConsume() {
    return 46;
  }
  
}
