package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.LightArmorItem;

public class ItemLeatherArmor extends LightArmorItem {

  @Override
  public String getItemName() {
    return "Leather Armor";
  }

  @Override
  public int getDropValue() {
    return 15;
  }

  @Override
  public Vector getBaseArmor() {
    return new Vector(10, 14);
  }
  
  @Override
  public int getBaseDurability() {
    return 10;
  }

  @Override
  public Vector getInvSize() {
    return new Vector(2, 3);
  }

  @Override
  public int getImgInv() {
    return 136;
  }

}
