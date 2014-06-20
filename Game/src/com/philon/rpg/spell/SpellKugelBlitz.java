package com.philon.rpg.spell;

import java.util.LinkedList;

import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;
import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.map.mo.UpdateMapObj.StateMovingParam;
import com.philon.rpg.map.mo.UpdateMapObj.StateMovingStraight;
import com.philon.rpg.mos.shot.AbstractShot;
import com.philon.rpg.mos.shot.ShotChargedBolt;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatLightningDamage;
import com.philon.rpg.util.RpgUtil;

public class SpellKugelBlitz extends AbstractSpell {

  public SpellKugelBlitz(CombatMapObj newOwnerMO, int newSLvl, Vector newTarPos, RpgMapObj newTarget) {
    super(newOwnerMO, newSLvl, newTarPos, newTarget);

    createSimpleShot( ShotChargedBolt.class, pos.copy() );
  }

  @Override
  public void update() {
    LinkedList<AbstractShot> newShots = new LinkedList<AbstractShot>();
    for (AbstractShot currShot : shots) {
      if(!(currShot.currState instanceof StateMovingStraight)) continue;
      if (Math.random()<0.1) { //change direction
        ((StateMovingStraight)currShot.currState).turn(Util.random(-30, 30));
      } else if (Math.random()<0.03) { //split
        AbstractShot newShot = Util.instantiateClass(currShot.getClass());
        newShot.tilesPerSecond = currShot.getTilesPerSecond();
        newShot.setPosition(currShot.pos);
        newShot.ownerSpell = this;
        RpgUtil.insertMapObj(newShot);
        newShot.changeState(StateMovingStraight.class, new StateMovingParam(Vector.rotateDeg(currShot.orientation, Util.random(-60, 60))));
        newShots.add(newShot);

        currShot.turn(Util.random(-60, 60));

      }
    }
    shots.addAll(newShots);

    super.update();
  }

  public static SpellDescriptor getDescriptor() {
   return new SpellKugelblitzDescriptor();
  }

  public static class SpellKugelblitzDescriptor extends SpellDescriptor {
    @Override
    public String getName() {
      return "Kugelblitz";
    }

    @Override
    public StatsObj getDamageForLevel(int level) {
      StatsObj result = new StatsObj();
      result.addOrCreateStat(StatLightningDamage.class, new Vector(4, 8).addInst(new Vector(3).mulScalarInst(level)));
      return result;
    }

    @Override
    public int getManacostForLevel(int level) {
      return 3 + (2 * level);
    }

    @Override
    protected float getLifeTime() {
      return 2;
    }

    @Override
    protected float getShotSpeed() {
      return 4;
    }

    @Override
    public int getImgIcon() {
      return 270;
    }

    @Override
    public int getSouPrepare() {
      return 51;
    }

    @Override
    public int getSouCast() {
      return 56;
    }
  }

}
