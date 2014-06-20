package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.BowItem;

public class ItemShortBow extends BowItem {

  @Override
  public String getItemName() {
    return "Short Bow";
  }

  @Override
  public int getDropValue() {
    return 10;
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
    return new Vector(2, 3);
  }

  @Override
  public int getImgInv() {
    return 119;
  }

}
