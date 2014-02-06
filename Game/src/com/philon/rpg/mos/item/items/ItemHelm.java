package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.HelmItem;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatStrength;

public class ItemHelm extends HelmItem {

  @Override
  public String getItemName() {
    return "Helm";
  }

  @Override
  public int getDropValue() {
    return 20;
  }

  @Override
  public Vector getBaseArmor() {
    return new Vector(4, 8);
  }
  
  @Override
  public int getBaseDurability() {
    return 10;
  }

  @Override
  public int getImgInv() {
    return 83;
  }
  
  @Override
  public StatsObj getRequirements() {
    StatsObj result = new StatsObj();
    result.addOrCreateStat( StatStrength.class,  25 );
    return result;
  }

}
