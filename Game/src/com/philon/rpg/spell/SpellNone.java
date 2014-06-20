package com.philon.rpg.spell;

import com.philon.engine.util.Vector;
import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.stat.StatsObj;

public class SpellNone extends AbstractSpell {

  public SpellNone(CombatMapObj newOwnerMO, int newSLvl, Vector newTarPos, RpgMapObj newTarget) {
    super(newOwnerMO, newSLvl, newTarPos, newTarget);
    isDying = true;
  }

  public static SpellDescriptor getDescriptor() {
   return new SpellNoneDescriptor();
  }

  public static class SpellNoneDescriptor extends SpellDescriptor {
    @Override
    public String getName() {
      return "None";
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
      return 0;
    }

    @Override
    public int getImgIcon() {
      return 0;
    }

    @Override
    public int getSouPrepare() {
      return 0;
    }

    @Override
    public int getSouCast() {
      return 0;
    }
  }

}
