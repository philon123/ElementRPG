package com.philon.rpg.spells;

import com.philon.engine.util.Vector;
import com.philon.rpg.mo.CombatMapObj;
import com.philon.rpg.mo.Selectable;
import com.philon.rpg.mos.player.AbstractChar;
import com.philon.rpg.spell.AbstractSpell;
import com.philon.rpg.stat.StatsObj.StatHealth;
import com.philon.rpg.stat.StatsObj.StatMaxHealth;

public class SpellHealing extends AbstractSpell {

  public SpellHealing(CombatMapObj newOwnerMO, int newSType, int newSLvl, Vector newTarPos, Selectable newTarget) {
    init(newOwnerMO, newSType, newSLvl, newTarPos, newTarget);
    
    AbstractChar p = (AbstractChar)ownerMO;
    p.stats.addOrCreateStat( StatHealth.class, (Integer)p.stats.getStatValue(StatMaxHealth.class) );
  }
  
}
