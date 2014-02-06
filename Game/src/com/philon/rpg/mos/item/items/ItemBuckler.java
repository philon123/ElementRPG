package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.ShieldItem;

public class ItemBuckler extends ShieldItem {

  @Override
  public String getItemName() {
    return "Buckler";
  }

  @Override
  public int getDropValue() {
    return 5;
  }

  @Override
  public Vector getBaseArmor() {
    return new Vector(3, 6);
  }
  
  @Override
  public int getBaseDurability() {
    return 10;
  }

  @Override
  public Vector getInvSize() {
    return new Vector(2, 2);
  }

  @Override
  public int getImgInv() {
    return 84;
  }
  
}
