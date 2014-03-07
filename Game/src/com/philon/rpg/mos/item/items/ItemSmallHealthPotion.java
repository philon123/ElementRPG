package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Util;
import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.mos.item.category.PotionItem;
import com.philon.rpg.mos.player.CharAmazon;
import com.philon.rpg.mos.player.CharBarbarian;
import com.philon.rpg.mos.player.CharSorcerer;
import com.philon.rpg.stat.StatsObj.StatHealth;
import com.philon.rpg.stat.StatsObj.StatMaxHealth;

public class ItemSmallHealthPotion extends PotionItem {

  @Override
  public String getItemName() {
    return "Small Health Potion";
  }

  @Override
  public int getDropValue() {
    return 5;
  }

  @Override
  public int getImgInv() {
    return 33;
  }
  
  @Override
  public void consumedTrigger(CombatMapObj consumedBy) {
    super.consumedTrigger(consumedBy);
    
    int maxHealth = (Integer)consumedBy.stats.getStatValue(StatMaxHealth.class);
    
    if (consumedBy instanceof CharBarbarian) {
      consumedBy.stats.addOrCreateStat( StatHealth.class, Util.round(maxHealth * 0.4f) );
    } else if (consumedBy instanceof CharAmazon) {
      consumedBy.stats.addOrCreateStat( StatHealth.class, Util.round(maxHealth * 0.33f) );
    } else if (consumedBy instanceof CharSorcerer) {
      consumedBy.stats.addOrCreateStat( StatHealth.class, Util.round(maxHealth * 0.25f) );
    }
  }
  
}
