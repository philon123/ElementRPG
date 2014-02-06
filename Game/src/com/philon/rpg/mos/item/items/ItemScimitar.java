package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.SwordItem;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatDexterity;
import com.philon.rpg.stat.StatsObj.StatStrength;

public class ItemScimitar extends SwordItem {

  @Override
  public String getItemName() {
    return "Scimitar";
  }

  @Override
  public int getDropValue() {
    return 7;
  }

  @Override
  public boolean isTwoHanded() {
    return false;
  }

  @Override
  public Vector getBaseDamage() {
    return new Vector(3, 7);
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
    return 73;
  }
  
  @Override
  public StatsObj getRequirements() {
    StatsObj result = new StatsObj();
    result.addOrCreateStat( StatStrength.class,  20 );
    result.addOrCreateStat( StatDexterity.class,  20 );
    return result;
  }
  
}