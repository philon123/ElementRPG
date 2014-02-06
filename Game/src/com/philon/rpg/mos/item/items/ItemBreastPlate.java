package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.HeavyArmorItem;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatStrength;

public class ItemBreastPlate extends HeavyArmorItem {

  @Override
  public String getItemName() {
    return "Breast Plate";
  }

  @Override
  public int getDropValue() {
    return 30;
  }

  @Override
  public Vector getBaseArmor() {
    return new Vector(22, 26);
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
    return 154;
  }
  
  @Override
  public StatsObj getRequirements() {
    StatsObj result = new StatsObj();
    result.addOrCreateStat( StatStrength.class,  40 );
    return result;
  }

}
