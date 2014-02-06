package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.SwordItem;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatStrength;

public class ItemBastardSword extends SwordItem {

  @Override
  public String getItemName() {
    return "Bastard Sword";
  }

  @Override
  public int getDropValue() {
    return 55;
  }

  @Override
  public boolean isTwoHanded() {
    return true;
  }

  @Override
  public Vector getBaseDamage() {
    return new Vector(6, 15);
  }
  
  @Override
  public int getBaseDurability() {
    return 30;
  }

  @Override
  public Vector getInvSize() {
    return new Vector(1, 3);
  }

  @Override
  public int getImgInv() {
    return 58;
  }
  
  @Override
  public StatsObj getRequirements() {
    StatsObj result = new StatsObj();
    result.addOrCreateStat( StatStrength.class,  50 );
    return result;
  }
  
}
