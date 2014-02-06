package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.SwordItem;

public class ItemDagger extends SwordItem {

  @Override
  public String getItemName() {
    return "Dagger";
  }

  @Override
  public int getDropValue() {
    return 5;
  }

  @Override
  public boolean isTwoHanded() {
    return false;
  }

  @Override
  public Vector getBaseDamage() {
    return new Vector(1, 4);
  }
  
  @Override
  public int getBaseDurability() {
    return 10;
  }

  @Override
  public Vector getInvSize() {
    return new Vector(1, 2);
  }

  @Override
  public int getImgInv() {
    return 52;
  }
  
}
