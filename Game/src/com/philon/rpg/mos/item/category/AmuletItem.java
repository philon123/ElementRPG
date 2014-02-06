package com.philon.rpg.mos.item.category;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.AbstractItem;

public abstract class AmuletItem extends AbstractItem {
  
  @Override
  public int getSouDrop() {
    return 41;
  }

  @Override
  public int getSouFlip() {
    return 29;
  }

  @Override
  public int getImgMap() {
    return 308;
  }
  
  @Override
  public int getBaseDurability() {
    return 1;
  }

  @Override
  public Vector getInvSize() {
    return new Vector(1);
  }
  
}
