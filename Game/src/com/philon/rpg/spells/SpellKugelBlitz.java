package com.philon.rpg.spells;

import java.util.LinkedList;

import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;
import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.mos.shot.AbstractShot;
import com.philon.rpg.mos.shot.ShotChargedBolt;
import com.philon.rpg.spell.AbstractSpell;

public class SpellKugelBlitz extends AbstractSpell {

  public SpellKugelBlitz(CombatMapObj newOwnerMO, int newSType, int newSLvl, Vector newTarPos, RpgMapObj newTarget) {
    init(newOwnerMO, newSType, newSLvl, newTarPos, newTarget);

    createSimpleShot( ShotChargedBolt.class, pos.copy() );
  }

  @Override
  public void update() {
    LinkedList<AbstractShot> newShots = new LinkedList<AbstractShot>();
    for (AbstractShot currShot : shots) {
      if (Math.random()<0.05) { //change direction
        shots.getFirst().turn(Util.random(-30, 30));
      } else if (Math.random()<0.02) { //split
        AbstractShot newShot = cloneShot(currShot);

        newShot.turn(Util.random(-45, 45));
        currShot.turn(Util.random(-45, 45));

        newShots.add(newShot);
      }
    }
    shots.addAll(newShots);

    super.update();
  }

  private static AbstractShot cloneShot(AbstractShot fromShot) {
    AbstractShot newShot = Util.instantiateClass(fromShot.getClass());
    newShot.tilesPerSecond = fromShot.getTilesPerSecond();
    newShot.setPosition(fromShot.pos);

    return newShot;
  }

}
