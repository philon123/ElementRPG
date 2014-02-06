package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.BowItem;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatDexterity;
import com.philon.rpg.stat.StatsObj.StatStrength;

public class ItemLongWarBow extends BowItem {

  @Override
  public String getItemName() {
    return "Long War Bow";
  }

  @Override
  public int getDropValue() {
    return 70;
  }

  @Override
  public Vector getBaseDamage() {
    return new Vector(1, 14);
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
    return 120;
  }
  
  @Override
  public StatsObj getRequirements() {
    StatsObj result = new StatsObj();
    result.addOrCreateStat( StatStrength.class,  45 );
    result.addOrCreateStat( StatDexterity.class,  80 );
    return result;
  }
  
}
