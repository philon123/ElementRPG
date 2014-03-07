package com.philon.rpg.mos.item.items;

import com.philon.engine.util.Util;
import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.mos.item.category.PotionItem;
import com.philon.rpg.mos.player.CharAmazon;
import com.philon.rpg.mos.player.CharBarbarian;
import com.philon.rpg.mos.player.CharSorcerer;
import com.philon.rpg.stat.StatsObj.StatMana;
import com.philon.rpg.stat.StatsObj.StatMaxMana;

public class ItemSmallManaPotion extends PotionItem {

  @Override
  public String getItemName() {
    return "Small Mana Potion";
  }

  @Override
  public int getDropValue() {
    return 5;
  }

  @Override
  public int getImgInv() {
    return 40;
  }
  
  @Override
  public void consumedTrigger(CombatMapObj consumedBy) {
    super.consumedTrigger(consumedBy);
    
    int maxMana = (Integer)consumedBy.stats.getStatValue(StatMaxMana.class);
    
    if (consumedBy instanceof CharBarbarian) {
      consumedBy.stats.addOrCreateStat( StatMana.class, Util.round(maxMana * 0.25f) );
    } else if (consumedBy instanceof CharAmazon) {
      consumedBy.stats.addOrCreateStat( StatMana.class, Util.round(maxMana * 0.33f) );
    } else if (consumedBy instanceof CharSorcerer) {
      consumedBy.stats.addOrCreateStat( StatMana.class, Util.round(maxMana * 0.4f) );
    }
  }
  
}
