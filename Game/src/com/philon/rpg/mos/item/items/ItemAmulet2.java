package com.philon.rpg.mos.item.items;

import com.philon.rpg.mos.item.category.AmuletItem;

public class ItemAmulet2 extends AmuletItem {

  @Override
  public String getItemName() {
    return "Amulet";
  }

  @Override
  public int getDropValue() {
    return 20;
  }

  @Override
  public int getImgInv() {
    return 46;
  }
  
}
