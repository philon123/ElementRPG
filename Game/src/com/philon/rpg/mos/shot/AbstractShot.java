package com.philon.rpg.mos.shot;
import java.util.LinkedList;

import com.philon.engine.util.Vector;
import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.spell.AbstractSpell;
import com.philon.rpg.util.RpgUtil;

public abstract class AbstractShot extends CombatMapObj {
	public AbstractSpell ownerSpell;
	public float tilesPerSecond=0;

	public AbstractShot() {
	  super();

	  isSelectable = false;
	  tilesPerSecond = getTilesPerSecond();
	}

	@Override
  public Vector getNewPositionOffset( Vector targetOffset ) {
    LinkedList<RpgMapObj> potentialColls;

    potentialColls = getPotentialCollisions( targetOffset );
    if( !(potentialColls==null || potentialColls.isEmpty()) ) {
      collisionTrigger( (RpgMapObj)(potentialColls.getFirst()) );
    }

    return targetOffset.copy();
  }

  @Override
  public LinkedList<RpgMapObj> getPotentialCollisions( Vector newOffset ) {
    LinkedList<RpgMapObj> result = RpgUtil.getRectColls(Vector.add(pos, newOffset), collRect);
    result =  RpgUtil.filterList( result, true, true, false, false, false, true );
    if (result==null) return null;
    result.remove(this);
    if (result.isEmpty()) return null;

    return result;
  }

  public void collisionTrigger( RpgMapObj otherMO ) {
    ownerSpell.shotImpactTrigger( this, otherMO );
  }

  @Override
  public Vector getCollRect() {
    return new Vector(0.25f);
  }

  @Override
  public float getDieCooldown() {
    return 0;
  }

	@Override
	public int getImgCasting() {
	  return 0;
	}

	@Override
	public int getSouHit() {
	  return 0;
	}

	@Override
	public int getSouFootstep() {
	  return 0;
	}

  @Override
  public int getImgHit() {
    return 0;
  }

  @Override
  public int getImgIdle() {
    return 0;
  }

  @Override
  public RpgMapObjSaveData save() {
    return null; //don't save shots
  }

}
