package com.philon.rpg.mos.item.items;

import com.philon.rpg.mos.item.category.RingItem;

public class ItemRing1 extends RingItem {

  @Override
  public String getItemName() {
    return "Ring";
  }

  @Override
  public int getDropValue() {
    return 15;
  }

  @Override
  public int getImgInv() {
    return 12;
  }
  
}
