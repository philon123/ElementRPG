package com.philon.rpg.mos.item.items;

import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.mos.item.category.ScrollItem;
import com.philon.rpg.spell.SpellData;

public class ItemScrollOfIdentify extends ScrollItem {

  @Override
  public String getItemName() {
    return "Scroll of Identify";
  }

  @Override
  public int getDropValue() {
    return 5;
  }

  @Override
  public int getImgInv() {
    return 2;
  }
  
  @Override
  public void consumedTrigger(CombatMapObj consumedBy) {
    super.consumedTrigger(consumedBy);
    
    consumedBy.prepareSpell( SpellData.IDENTIFY, true, null, null );
  }

}
