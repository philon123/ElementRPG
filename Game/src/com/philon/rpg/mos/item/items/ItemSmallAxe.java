package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.AxeItem;

public class ItemSmallAxe extends AxeItem {

  @Override
  public String getItemName() {
    return "Small Axe";
  }

  @Override
  public int getDropValue() {
    return 10;
  }

  @Override
  public boolean isTwoHanded() {
    return false;
  }

  @Override
  public Vector getBaseDamage() {
    return new Vector(2, 7);
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
    return 113;
  }
  
}
