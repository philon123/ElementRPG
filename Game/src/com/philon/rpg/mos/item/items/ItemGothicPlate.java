package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.HeavyArmorItem;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatStrength;

public class ItemGothicPlate extends HeavyArmorItem {

  @Override
  public String getItemName() {
    return "Gothic Plate";
  }

  @Override
  public int getDropValue() {
    return 60;
  }

  @Override
  public Vector getBaseArmor() {
    return new Vector(55, 70);
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
    return 153;
  }
  
  @Override
  public StatsObj getRequirements() {
    StatsObj result = new StatsObj();
    result.addOrCreateStat( StatStrength.class,  80 );
    return result;
  }

}
