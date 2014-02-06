package com.philon.rpg.spells;

import com.philon.engine.util.Vector;
import com.philon.rpg.mo.CombatMapObj;
import com.philon.rpg.mo.Selectable;
import com.philon.rpg.mos.shot.ShotFirebolt;
import com.philon.rpg.spell.AbstractSpell;

public class SpellFireBolt extends AbstractSpell {

  public SpellFireBolt(CombatMapObj newOwnerMO, int newSType, int newSLvl, Vector newTarPos, Selectable newTarget) {
    init(newOwnerMO, newSType, newSLvl, newTarPos, newTarget);
    
    createSimpleShot( ShotFirebolt.class, pos.copy() );
  }
  
}
