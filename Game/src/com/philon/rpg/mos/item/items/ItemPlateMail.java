package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.HeavyArmorItem;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatStrength;

public class ItemPlateMail extends HeavyArmorItem {

  @Override
  public String getItemName() {
    return "Plate Mail";
  }

  @Override
  public int getDropValue() {
    return 50;
  }

  @Override
  public Vector getBaseArmor() {
    return new Vector(45, 55);
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
    return 104;
  }
  
  @Override
  public StatsObj getRequirements() {
    StatsObj result = new StatsObj();
    result.addOrCreateStat( StatStrength.class,  60 );
    return result;
  }

}
