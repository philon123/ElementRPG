package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.ClubItem;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatStrength;

public class ItemWarHammer extends ClubItem {

  @Override
  public String getItemName() {
    return "War Hammer";
  }

  @Override
  public int getDropValue() {
    return 50;
  }

  @Override
  public boolean isTwoHanded() {
    return false;
  }

  @Override
  public Vector getBaseDamage() {
    return new Vector(5, 9);
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
    return 122;
  }
  
  @Override
  public StatsObj getRequirements() {
    StatsObj result = new StatsObj();
    result.addOrCreateStat( StatStrength.class,  40 );
    return result;
  }
  
}
