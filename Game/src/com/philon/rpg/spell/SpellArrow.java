package com.philon.rpg.spell;

import com.philon.engine.util.Vector;
import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.mos.shot.ShotArrow;
import com.philon.rpg.stat.StatsObj;

public class SpellArrow extends AbstractSpell {

  public SpellArrow(CombatMapObj newOwnerMO, int newSLvl, Vector newTarPos, RpgMapObj newTarget) {
    super(newOwnerMO, newSLvl, newTarPos, newTarget);

    stats = newOwnerMO.stats;
    createSimpleShot( ShotArrow.class, pos.copy() );
  }

  public static SpellDescriptor getDescriptor() {
   return new SpellArrowDescriptor();
  }

  public static class SpellArrowDescriptor extends SpellDescriptor {
    @Override
    public String getName() {
      return "Bow & Arrow";
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
      return 0;
    }

    @Override
    protected float getShotSpeed() {
      return 10;
    }

    @Override
    public int getImgIcon() {
      return 247;
    }

    @Override
    public int getSouPrepare() {
      return 0;
    }

    @Override
    public int getSouCast() {
      return 16;
    }
  }
}
