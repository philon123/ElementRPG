package com.philon.rpg.spells;

import com.philon.engine.util.Vector;
import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.mos.shot.ShotArrow;
import com.philon.rpg.spell.AbstractSpell;

public class SpellArrow extends AbstractSpell {

  public SpellArrow(CombatMapObj newOwnerMO, int newSType, int newSLvl, Vector newTarPos, RpgMapObj newTarget) {
    init(newOwnerMO, newSType, newSLvl, newTarPos, newTarget);

    stats = newOwnerMO.stats;
    createSimpleShot( ShotArrow.class, pos.copy() );
  }

}
