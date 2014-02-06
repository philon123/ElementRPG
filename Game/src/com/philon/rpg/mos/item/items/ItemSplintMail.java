package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.HeavyArmorItem;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatStrength;

public class ItemSplintMail extends HeavyArmorItem {

  @Override
  public String getItemName() {
    return "Splint Mail";
  }

  @Override
  public int getDropValue() {
    return 35;
  }

  @Override
  public Vector getBaseArmor() {
    return new Vector(31, 38);
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
    return 137;
  }
  
  @Override
  public StatsObj getRequirements() {
    StatsObj result = new StatsObj();
    result.addOrCreateStat( StatStrength.class,  40 );
    return result;
  }

}