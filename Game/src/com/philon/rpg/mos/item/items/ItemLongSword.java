package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.SwordItem;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatDexterity;
import com.philon.rpg.stat.StatsObj.StatStrength;

public class ItemLongSword extends SwordItem {

  @Override
  public String getItemName() {
    return "Long Sword";
  }

  @Override
  public int getDropValue() {
    return 30;
  }

  @Override
  public boolean isTwoHanded() {
    return true;
  }

  @Override
  public Vector getBaseDamage() {
    return new Vector(2, 10);
  }
  
  @Override
  public int getBaseDurability() {
    return 10;
  }

  @Override
  public Vector getInvSize() {
    return new Vector(1, 3);
  }

  @Override
  public int getImgInv() {
    return 57;
  }
  
  @Override
  public StatsObj getRequirements() {
    StatsObj result = new StatsObj();
    result.addOrCreateStat( StatStrength.class,  30 );
    result.addOrCreateStat( StatDexterity.class,  30 );
    return result;
  }
  
}
