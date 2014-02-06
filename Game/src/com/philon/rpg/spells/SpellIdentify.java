package com.philon.rpg.spells;

import com.philon.engine.util.Vector;
import com.philon.rpg.mo.CombatMapObj;
import com.philon.rpg.mo.Selectable;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.player.AbstractChar;
import com.philon.rpg.spell.AbstractSpell;

public class SpellIdentify extends AbstractSpell {

  public SpellIdentify(CombatMapObj newOwnerMO, int newSType, int newSLvl, Vector newTarPos, Selectable newTarget) {
    init(newOwnerMO, newSType, newSLvl, newTarPos, newTarget);
    
    if( target!=null && target instanceof AbstractItem ) {
      ((AbstractChar)ownerMO).inv.identifyItem( (AbstractItem)target );
    }
  }
  
}
