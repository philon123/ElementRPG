package com.philon.rpg.spells;

import com.philon.engine.util.Vector;
import com.philon.rpg.mo.CombatMapObj;
import com.philon.rpg.mo.Selectable;
import com.philon.rpg.spell.AbstractSpell;

public class SpellNone extends AbstractSpell {

  public SpellNone(CombatMapObj newOwnerMO, int newSType, int newSLvl, Vector newTarPos, Selectable newTarget) {
    init(newOwnerMO, newSType, newSLvl, newTarPos, newTarget);
  }
  
}
