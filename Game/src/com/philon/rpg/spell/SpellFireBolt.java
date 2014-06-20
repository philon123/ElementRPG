package com.philon.rpg.spell;

import com.philon.engine.util.Vector;
import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.mos.shot.ShotFirebolt;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatFireDamage;

public class SpellFireBolt extends AbstractSpell {

  public SpellFireBolt(CombatMapObj newOwnerMO, int newSLvl, Vector newTarPos, RpgMapObj newTarget) {
    super(newOwnerMO, newSLvl, newTarPos, newTarget);

    createSimpleShot( ShotFirebolt.class, pos.copy() );
  }

  public static SpellDescriptor getDescriptor() {
   return new SpellFireboltDescriptor();
  }

  public static class SpellFireboltDescriptor extends SpellDescriptor {
    @Override
    public String getName() {
      return "Firebolt";
    }

    @Override
    public StatsObj getDamageForLevel(int level) {
      StatsObj result = new StatsObj();
      result.addOrCreateStat(StatFireDamage.class, new Vector(4, 8).addInst(new Vector(3).mulScalarInst(level)));
      return result;
    }

    @Override
    public int getManacostForLevel(int level) {
      return 2 + (1 * level);
    }

    @Override
    protected float getLifeTime() {
      return 0;
    }

    @Override
    protected float getShotSpeed() {
      return 15;
    }

    @Override
    public int getImgIcon() {
      return 250;
    }

    @Override
    public int getSouPrepare() {
      return 52;
    }

    @Override
    public int getSouCast() {
      return 18;
    }
  }

}
