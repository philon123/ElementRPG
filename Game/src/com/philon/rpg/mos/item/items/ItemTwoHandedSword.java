package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.SwordItem;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatStrength;

public class ItemTwoHandedSword extends SwordItem {

  @Override
  public String getItemName() {
    return "Two-Handed Sword";
  }

  @Override
  public int getDropValue() {
    return 60;
  }

  @Override
  public boolean isTwoHanded() {
    return true;
  }

  @Override
  public Vector getBaseDamage() {
    return new Vector(8, 16);
  }
  
  @Override
  public int getBaseDurability() {
    return 30;
  }

  @Override
  public Vector getInvSize() {
    return new Vector(2, 3);
  }

  @Override
  public int getImgInv() {
    return 111;
  }
  
  @Override
  public StatsObj getRequirements() {
    StatsObj result = new StatsObj();
    result.addOrCreateStat( StatStrength.class,  65 );
    return result;
  }
  
}
