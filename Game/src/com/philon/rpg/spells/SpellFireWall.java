package com.philon.rpg.spells;

import com.philon.engine.util.Vector;
import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.mos.shot.ShotFire;
import com.philon.rpg.spell.AbstractSpell;
import com.philon.rpg.util.RpgUtil;

public class SpellFireWall extends AbstractSpell {

  public SpellFireWall(CombatMapObj newOwnerMO, int newSType, int newSLvl, Vector newTarPos, RpgMapObj newTarget) {
    init(newOwnerMO, newSType, newSLvl, newTarPos, newTarget);

    Vector newPos;
    Vector newOffset;
    Vector newCollRadius=new Vector(0.7f);
    Vector newShotSize=Vector.sub(tarPos, pos).normalizeInst().copy().mulInst(newCollRadius).rotateDegInst(-90);

    newOffset = new Vector();
    newPos = tarPos.copy();
    if( !RpgUtil.getIsRectCollidingWithMap( newPos, newCollRadius ) ) {
      for( int i = -4; i <= 4; i++ ) {
        newOffset = newShotSize.mulScalarInst(i);
        newPos = Vector.add( tarPos, newOffset );
        if( !RpgUtil.getIsRectCollidingWithMap( newPos, newCollRadius ) ) {
          createSimpleShot( ShotFire.class, pos.copy() );
        } else {
          break;
        }
      }
    }
  }

}
