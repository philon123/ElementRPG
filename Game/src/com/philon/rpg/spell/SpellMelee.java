package com.philon.rpg.spell;

import com.philon.engine.util.Vector;
import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.mos.shot.ShotMelee;
import com.philon.rpg.stat.StatsObj;

public class SpellMelee extends AbstractSpell {

  public SpellMelee(CombatMapObj newOwnerMO, int newSLvl, Vector newTarPos, RpgMapObj newTarget) {
    super(newOwnerMO, newSLvl, newTarPos, newTarget);

    stats = newOwnerMO.stats;
    createSimpleShot( ShotMelee.class, pos.copy() );
  }

  public static SpellDescriptor getDescriptor() {
   return new SpelllMeleeDescriptor();
  }

  public static class SpelllMeleeDescriptor extends SpellDescriptor {
    @Override
    public String getName() {
      return "Melee";
    }

    @Override
    public StatsObj getDamageForLevel(int level) {
      return new StatsObj();
    }

    @Override
    public int getManacostForLevel(int level) {
      return 0;
    }

    @Override
    protected float getLifeTime() {
      return 0.01f;
    }

    @Override
    protected float getShotSpeed() {
      return 0;
    }

    @Override
    public int getImgIcon() {
      return 246;
    }

    @Override
    public int getSouPrepare() {
      return 0;
    }

    @Override
    public int getSouCast() {
      return 15;
    }
  }

}
