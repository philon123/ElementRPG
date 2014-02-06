package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.ShieldItem;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatStrength;

public class ItemLargeShield extends ShieldItem {

  @Override
  public String getItemName() {
    return "Large Shield";
  }

  @Override
  public int getDropValue() {
    return 20;
  }

  @Override
  public Vector getBaseArmor() {
    return new Vector(8, 15);
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
    return 148;
  }
  
  @Override
  public StatsObj getRequirements() {
    StatsObj result = new StatsObj();
    result.addOrCreateStat( StatStrength.class,  40 );
    return result;
  }

}
