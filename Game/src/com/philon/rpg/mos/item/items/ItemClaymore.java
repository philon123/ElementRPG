package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.SwordItem;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatStrength;

public class ItemClaymore extends SwordItem {

  @Override
  public String getItemName() {
    return "Claymore";
  }

  @Override
  public int getDropValue() {
    return 40;
  }

  @Override
  public boolean isTwoHanded() {
    return true;
  }

  @Override
  public Vector getBaseDamage() {
    return new Vector(1, 12);
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
    return 66;
  }
  
  @Override
  public StatsObj getRequirements() {
    StatsObj result = new StatsObj();
    result.addOrCreateStat( StatStrength.class,  35 );
    return result;
  }
  
}
