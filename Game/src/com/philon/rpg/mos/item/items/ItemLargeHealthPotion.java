package com.philon.rpg.mos.item.items;

import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.mos.item.category.PotionItem;
import com.philon.rpg.stat.StatsObj.StatHealth;
import com.philon.rpg.stat.StatsObj.StatMaxHealth;

public class ItemLargeHealthPotion extends PotionItem {

  @Override
  public String getItemName() {
    return "Large Health Potion";
  }

  @Override
  public int getDropValue() {
    return 25;
  }

  @Override
  public int getImgInv() {
    return 36;
  }
  
  @Override
  public void consumedTrigger(CombatMapObj consumedBy) {
    super.consumedTrigger(consumedBy);
    
    consumedBy.stats.addOrCreateStat( StatHealth.class, (Integer)consumedBy.stats.getStatValue(StatMaxHealth.class) );
  }
  
}
