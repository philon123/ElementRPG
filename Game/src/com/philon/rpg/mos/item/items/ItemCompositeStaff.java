package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.StaffItem;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatMagic;

public class ItemCompositeStaff extends StaffItem {

  @Override
  public String getItemName() {
    return "Composite Staff";
  }

  @Override
  public int getDropValue() {
    return 45;
  }

  @Override
  public Vector getBaseDamage() {
    return new Vector(6, 12);
  }
  
  @Override
  public int getBaseDurability() {
    return 10;
  }

  @Override
  public Vector getInvSize() {
    return new Vector(2, 3);
  }

  @Override
  public int getImgInv() {
    return 167;
  }
  
  @Override
  public StatsObj getRequirements() {
    StatsObj result = new StatsObj();
    result.addOrCreateStat( StatMagic.class,  75 );
    return result;
  }
  
}
