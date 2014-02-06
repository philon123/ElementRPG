package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.HelmItem;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatMagic;

public class ItemCrown extends HelmItem {

  @Override
  public String getItemName() {
    return "Crown";
  }

  @Override
  public int getDropValue() {
    return 40;
  }

  @Override
  public Vector getBaseArmor() {
    return new Vector(10, 15);
  }
  
  @Override
  public int getBaseDurability() {
    return 10;
  }

  @Override
  public int getImgInv() {
    return 96;
  }
  
  @Override
  public StatsObj getRequirements() {
    StatsObj result = new StatsObj();
    result.addOrCreateStat( StatMagic.class,  20 );
    return result;
  }

}
