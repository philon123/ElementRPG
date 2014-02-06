package com.philon.rpg.mos.item.category;

import com.philon.engine.util.Vector;


public abstract class PotionItem extends ConsumableItem {
  
  @Override
  public int getSouDrop() {
    return 40;
  }

  @Override
  public int getSouFlip() {
    return 28;
  }

  @Override
  public int getImgMap() {
    return 317;
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
    return 40;
  }
  
}
