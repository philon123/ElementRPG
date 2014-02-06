package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.SwordItem;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatStrength;

public class ItemShortSword extends SwordItem {

  @Override
  public String getItemName() {
    return "Short Sword";
  }

  @Override
  public int getDropValue() {
    return 7;
  }

  @Override
  public boolean isTwoHanded() {
    return false;
  }

  @Override
  public Vector getBaseDamage() {
    return new Vector(2, 6);
  }
  
  @Override
  public int getBaseDurability() {
    return 10;
  }

  @Override
  public Vector getInvSize() {
    return new Vector(1, 3);
  }

  @Override
  public int getImgInv() {
    return 65;
  }
  
  @Override
  public StatsObj getRequirements() {
    StatsObj result = new StatsObj();
    result.addOrCreateStat( StatStrength.class,  18 );
    return result;
  }
  
}
