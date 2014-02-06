package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.HelmItem;

public class ItemSkullCap extends HelmItem {

  @Override
  public String getItemName() {
    return "Skull Cap";
  }

  @Override
  public int getDropValue() {
    return 10;
  }

  @Override
  public Vector getBaseArmor() {
    return new Vector(2, 4);
  }
  
  @Override
  public int getBaseDurability() {
    return 10;
  }

  @Override
  public int getImgInv() {
    return 91;
  }
  
}
