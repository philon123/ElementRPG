package com.philon.rpg.spell;

import com.philon.engine.util.Vector;
import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.mos.shot.ShotFire;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatFireDamage;
import com.philon.rpg.util.RpgUtil;

public class SpellFireWall extends AbstractSpell {

  public SpellFireWall(CombatMapObj newOwnerMO, int newSLvl, Vector newTarPos, RpgMapObj newTarget) {
    super(newOwnerMO, newSLvl, newTarPos, newTarget);

    Vector targetPos = Vector.add( pos, Vector.sub(tarPos, pos).normalizeInst().mulScalarInst(1) );
    Vector newCollRadius = new Vector(0.7f);
    Vector newShotSize = Vector.sub(targetPos, pos).normalizeInst().mulInst(newCollRadius).rotateDegInst(-90);

    Vector newPos = targetPos.copy();
    if( !RpgUtil.getIsRectCollidingWithMap(newPos, newCollRadius) ) {
      for( int i = -4; i <= 4; i++ ) {
        Vector newOffset = Vector.mulScalar(newShotSize, i);
        newPos = Vector.add( targetPos, newOffset );
        if( !RpgUtil.getIsRectCollidingWithMap( newPos, newCollRadius ) ) {
          createSimpleShot( ShotFire.class, newPos.copy() );
        } else {
          break;
        }
      }
    }
  }

  public static SpellDescriptor getDescriptor() {
   return new SpellFirewallDescriptor();
  }

  public static class SpellFirewallDescriptor extends SpellDescriptor {
    @Override
    public String getName() {
      return "Firewall";
    }

    @Override
    public StatsObj getDamageForLevel(int level) {
      StatsObj result = new StatsObj();
      result.addOrCreateStat(StatFireDamage.class, new Vector(4, 8).addInst(new Vector(3).mulScalarInst(level)));
      return result;
    }

    @Override
    public int getManacostForLevel(int level) {
      return 5 + (5 * level);
    }

    @Override
    protected float getLifeTime() {
      return 3;
    }

    @Override
    protected float getShotSpeed() {
      return 0;
    }

    @Override
    public int getImgIcon() {
      return 263;
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
