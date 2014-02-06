package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.HelmItem;

public class ItemCap extends HelmItem {

  @Override
  public String getItemName() {
    return "Cap";
  }

  @Override
  public int getDropValue() {
    return 5;
  }

  @Override
  public Vector getBaseArmor() {
    return new Vector(1, 3);
  }
  
  @Override
  public int getBaseDurability() {
    return 10;
  }

  @Override
  public int getImgInv() {
    return 92;
  }
  
}
