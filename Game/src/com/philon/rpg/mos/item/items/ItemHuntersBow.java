package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.BowItem;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatDexterity;
import com.philon.rpg.stat.StatsObj.StatStrength;

public class ItemHuntersBow extends BowItem {

  @Override
  public String getItemName() {
    return "Hunters' Bow";
  }

  @Override
  public int getDropValue() {
    return 10;
  }

  @Override
  public Vector getBaseDamage() {
    return new Vector(2, 5);
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
    return 103;
  }
  
  @Override
  public StatsObj getRequirements() {
    StatsObj result = new StatsObj();
    result.addOrCreateStat( StatStrength.class,  20 );
    result.addOrCreateStat( StatDexterity.class,  35 );
    return result;
  }
  
}
