package com.philon.rpg.mos.shot;
import java.util.LinkedList;

import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.mo.AbstractMapObj;
import com.philon.rpg.mo.CombatMapObj;
import com.philon.rpg.mo.state.AbstractMapObjState;
import com.philon.rpg.spell.AbstractSpell;

public abstract class AbstractShot extends CombatMapObj {
	public AbstractSpell ownerSpell;

	@Override
	public Class<? extends AbstractMapObjState> getDefaultState() {
	  return StateMovingStraight.class;
	}
	
	@Override
  public Vector getNewPositionOffset( Vector targetOffset ) {
    LinkedList<AbstractMapObj> potentialColls;

    potentialColls = getPotentialCollisions( targetOffset );
    if( !(potentialColls==null || potentialColls.isEmpty()) ) {
      collisionTrigger( (AbstractMapObj)(potentialColls.getFirst()) );
      if (currState==StateDying.class) return null;
    }

    return targetOffset.copy();
  }
  
  @Override
  public LinkedList<AbstractMapObj> getPotentialCollisions( Vector newOffset ) {
    LinkedList<AbstractMapObj> result = RpgGame.inst.gMap.getRectColls(Vector.add(pos, newOffset), collRect);
    result =  AbstractMapObj.filterList( result, true, true, false, false, false, true );
    if (result==null) return null;
    result.remove(this);
    if (result.isEmpty()) return null;
    
    return result;
  }

  public void collisionTrigger( AbstractMapObj otherMO ) {
    ownerSpell.shotImpactTrigger( this, otherMO );
  }

  public void changeDirection( float newRelRotation ) {
    direction = currTargetPos.copy().subInst(pos).normalizeInst().rotateDegInst(newRelRotation);
    setTarget( currTarget, pos.copy().addInst(currTargetPos.copy().subInst(pos).normalizeInst().rotateDegInst(newRelRotation)) );
  }

  @Override
  public Vector getCollRect() {
    return new Vector(0.2f);
  }
  
  @Override
  public int getDieCooldown() {
    return 0;
  }

  @Override
  public float getTilesPerSecond() {
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

}
