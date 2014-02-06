package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.HelmItem;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatStrength;

public class ItemFullHelm extends HelmItem {

  @Override
  public String getItemName() {
    return "Full Helm";
  }

  @Override
  public int getDropValue() {
    return 30;
  }

  @Override
  public Vector getBaseArmor() {
    return new Vector(6, 10);
  }
  
  @Override
  public int getBaseDurability() {
    return 10;
  }

  @Override
  public int getImgInv() {
    return 76;
  }
  
  @Override
  public StatsObj getRequirements() {
    StatsObj result = new StatsObj();
    result.addOrCreateStat( StatStrength.class,  35 );
    return result;
  }

}
