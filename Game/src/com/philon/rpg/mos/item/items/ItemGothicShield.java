package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.ShieldItem;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatStrength;

public class ItemGothicShield extends ShieldItem {

  @Override
  public String getItemName() {
    return "Gothic Shield";
  }

  @Override
  public int getDropValue() {
    return 60;
  }

  @Override
  public Vector getBaseArmor() {
    return new Vector(20, 30);
  }
  
  @Override
  public int getBaseDurability() {
    return 40;
  }

  @Override
  public Vector getInvSize() {
    return new Vector(2, 3);
  }

  @Override
  public int getImgInv() {
    return 149;
  }
  
  @Override
  public StatsObj getRequirements() {
    StatsObj result = new StatsObj();
    result.addOrCreateStat( StatStrength.class,  80 );
    return result;
  }

}
