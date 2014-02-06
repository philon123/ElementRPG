package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.AxeItem;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatStrength;

public class ItemGreatAxe extends AxeItem {

  @Override
  public String getItemName() {
    return "Great Axe";
  }

  @Override
  public int getDropValue() {
    return 60;
  }

  @Override
  public boolean isTwoHanded() {
    return true;
  }

  @Override
  public Vector getBaseDamage() {
    return new Vector(12, 30);
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
    return 144;
  }
  
  @Override
  public StatsObj getRequirements() {
    StatsObj result = new StatsObj();
    result.addOrCreateStat( StatStrength.class,  80 );
    return result;
  }

}
