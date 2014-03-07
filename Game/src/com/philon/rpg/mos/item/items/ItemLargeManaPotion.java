package com.philon.rpg.mos.item.items;

import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.mos.item.category.PotionItem;
import com.philon.rpg.stat.StatsObj.StatMana;
import com.philon.rpg.stat.StatsObj.StatMaxMana;

public class ItemLargeManaPotion extends PotionItem {

  @Override
  public String getItemName() {
    return "Large Mana Potion";
  }

  @Override
  public int getDropValue() {
    return 25;
  }

  @Override
  public int getImgInv() {
    return 1;
  }
  
  @Override
  public void consumedTrigger(CombatMapObj consumedBy) {
    super.consumedTrigger(consumedBy);
    
    consumedBy.stats.addOrCreateStat( StatMana.class, (Integer)consumedBy.stats.getStatValue(StatMaxMana.class) );
  }
  
}
