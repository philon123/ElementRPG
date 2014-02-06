package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.HeavyArmorItem;

public class ItemScaleMail extends HeavyArmorItem {

  @Override
  public String getItemName() {
    return "Scale Mail";
  }

  @Override
  public int getDropValue() {
    return 35;
  }

  @Override
  public Vector getBaseArmor() {
    return new Vector(25, 30);
  }
  
  @Override
  public int getBaseDurability() {
    return 20;
  }

  @Override
  public Vector getInvSize() {
    return new Vector(2, 3);
  }

  @Override
  public int getImgInv() {
    return 115;
  }

}
