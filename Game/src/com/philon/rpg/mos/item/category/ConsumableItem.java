package com.philon.rpg.mos.item.category;

import com.philon.rpg.RpgGame;
import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.mos.item.AbstractItem;

public abstract class ConsumableItem extends AbstractItem {
  public int souConsume;
  
  public ConsumableItem() {
    super();
    
    souConsume = getSouConsume();
  }
  
  public abstract int getSouConsume();
  
  public void consumedTrigger(CombatMapObj consumedBy) {
    RpgGame.playSoundFX( souConsume );
  }
}
