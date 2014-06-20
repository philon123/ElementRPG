package com.philon.rpg.spell;

import com.philon.engine.util.Vector;
import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.mos.player.AbstractChar;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatHealth;
import com.philon.rpg.stat.StatsObj.StatMaxHealth;

public class SpellHealing extends AbstractSpell {

  public SpellHealing(CombatMapObj newOwnerMO, int newSLvl, Vector newTarPos, RpgMapObj newTarget) {
    super(newOwnerMO, newSLvl, newTarPos, newTarget);

    AbstractChar p = (AbstractChar)ownerMO;
    p.stats.addOrCreateStat( StatHealth.class, (Integer)p.stats.getStatValue(StatMaxHealth.class) );
    isDying = true;
  }

  public static SpellDescriptor getDescriptor() {
   return new SpellHealingDescriptor();
  }

  public static class SpellHealingDescriptor extends SpellDescriptor {
    @Override
    public String getName() {
      return "Healing";
    }

    @Override
    public StatsObj getDamageForLevel(int level) {
      return new StatsObj();
    }

    @Override
    public int getManacostForLevel(int level) {
      return 3 + (1 * level);
    }

    @Override
    protected float getLifeTime() {
      return 0;
    }

    @Override
    protected float getShotSpeed() {
      return 0;
    }

    @Override
    public int getImgIcon() {
      return 249;
    }

    @Override
    public int getSouPrepare() {
      return 55;
    }

    @Override
    public int getSouCast() {
      return 14;
    }
  }

}
