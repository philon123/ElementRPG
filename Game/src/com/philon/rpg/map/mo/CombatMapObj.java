package com.philon.rpg.map.mo;

import java.util.LinkedList;

import com.philon.engine.Data;
import com.philon.engine.FrameAnimation;
import com.philon.engine.PhilonGame;
import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.map.mo.state.AbstractMapObjState;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.spell.AbstractSpell;
import com.philon.rpg.spell.SpellData;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.StatAttackRate;
import com.philon.rpg.stat.StatsObj.StatCastRate;
import com.philon.rpg.stat.StatsObj.StatFireDamage;
import com.philon.rpg.stat.StatsObj.StatHealth;
import com.philon.rpg.stat.StatsObj.StatHitRecovery;
import com.philon.rpg.stat.StatsObj.StatIceDamage;
import com.philon.rpg.stat.StatsObj.StatIgnoreArmor;
import com.philon.rpg.stat.StatsObj.StatLifeLeech;
import com.philon.rpg.stat.StatsObj.StatLightningDamage;
import com.philon.rpg.stat.StatsObj.StatMana;
import com.philon.rpg.stat.StatsObj.StatManaLeech;
import com.philon.rpg.stat.StatsObj.StatMaxHealth;
import com.philon.rpg.stat.StatsObj.StatMaxMana;
import com.philon.rpg.stat.StatsObj.StatMulNorDmgReduce;
import com.philon.rpg.stat.StatsObj.StatNormalDamage;
import com.philon.rpg.stat.StatsObj.StatReduceDmgTaken;
import com.philon.rpg.stat.StatsObj.StatResistFire;
import com.philon.rpg.stat.StatsObj.StatResistIce;
import com.philon.rpg.stat.StatsObj.StatResistLightning;
import com.philon.rpg.stat.effect.EffectsObj;
import com.philon.rpg.util.RpgUtil;

public abstract class CombatMapObj extends UpdateMapObj {
  public EffectsObj baseEffects;
  public EffectsObj effects;

  public StatsObj stats;
  public LinkedList<AbstractSpell> activeSpells = new LinkedList<AbstractSpell>();

	public int footstepCooldown;
	public int castCooldown;
	public int hitCooldown;

	public int currSelectedSpell;
	public int preparedSpell;
	public Vector preparedTarPos;
	public RpgMapObj preparedTarget;
	public boolean isPreparedManual;

	public CombatMapObj() {
	  super();

		baseEffects = getBaseEffects();
		updateStats();
	}

	public abstract int getImgCasting();
	public abstract int getImgHit();
	public abstract int getSouAttack();
  public abstract int getSouHit();
  public abstract int getSouFootstep();

  public EffectsObj getBaseEffects() {
    EffectsObj result = new EffectsObj();
    return result;
  }

  public float getMaxMeleeRange() {
    return 0.5f;
  }

	public void update() {
		updateSpells();

		super.update();
	}

	public void updateStats() {
	  effects = EffectsObj.add(baseEffects, getAdditionalEffects());

	  int tmpHealth=0;
	  int tmpMana=0;
	  boolean statsWereEmpty = true;
	  if (stats!=null) {
	    statsWereEmpty = false;
      tmpHealth = (Integer) stats.getStatValue(StatHealth.class);
      tmpMana= (Integer) stats.getStatValue(StatMana.class);
	  }

    stats = effects.getStats();

    if (statsWereEmpty) {
      stats.addOrCreateStat(StatHealth.class, stats.getStatValue(StatMaxHealth.class));
      stats.addOrCreateStat(StatMana.class, stats.getStatValue(StatMaxMana.class));
    } else {
      stats.addOrCreateStat(StatHealth.class, tmpHealth);
      stats.addOrCreateStat(StatMana.class, tmpMana);
    }
  }

  public EffectsObj getAdditionalEffects() {
    return new EffectsObj();
  }

	public void updateCooldowns() {
		super.updateCooldowns();

		//hit
		if( hitCooldown>0 ) {
			hitCooldown -= 1;
			if (hitCooldown==0 && !(currState instanceof StateDying) ) changeState( StateIdle.class ); //TODO move to StateHit
		}

		//footsteps
		if( footstepCooldown>0 ) {
			footstepCooldown -= 1;
		}

		//casting
		if( castCooldown>0 ) {
			castCooldown -= 1;
			if( castCooldown==0 && !isPreparedManual ) {
				castPreparedSpell();
			}
		}
	}

	//----------

	@Override
	public void deathTrigger(CombatMapObj killedBy) {
	  super.deathTrigger(killedBy);

		for( AbstractSpell s : activeSpells ) {
			s.deleteObject();
		}
	}

	//----------

	public void updateSpells() {
	  for (int i=0; i<activeSpells.size(); i++) {
	    AbstractSpell s = activeSpells.get(i);
	    s.update();
			if (s.isDying) activeSpells.remove(s);
		}
	}

	//----------

	public boolean useMana(int amount) {
	  stats.addOrCreateStat( StatMana.class, -1*amount );
	  return true;
	}

	//----------

	public boolean prepareSpell( int newSpellID, boolean isManual, Vector newTarPos, RpgMapObj newTarget ) {
		if (castCooldown>0) return false;
		if( newSpellID==SpellData.EMPTY ) return true;
		if ( !(newSpellID==SpellData.MELEE || newSpellID==SpellData.ARROW)
		    && !useMana(SpellData.manaCost[newSpellID][stats.spells[newSpellID]]) ) {
		  return false;
		}

		v = 0;

		if( newSpellID==SpellData.MELEE || newSpellID==SpellData.ARROW ) {
			castCooldown = (int) (PhilonGame.inst.fps / (Float)stats.getStatValue(StatAttackRate.class));
		} else {
			castCooldown = (int) (PhilonGame.inst.fps / (Float)stats.getStatValue(StatCastRate.class));
		}

		if( newTarget!=null ) {
			if( !(newTarget instanceof AbstractItem && !(((AbstractItem)newTarget).currState instanceof AbstractItem.StateMap) ) ) {
				newTarPos=((RpgMapObj)newTarget).pos.copy();
			}
		}
		if (newTarPos!=null && !newTarPos.isAllEqual(new Vector())) turnToTarget(newTarPos);

		preparedSpell = newSpellID;
		preparedTarPos = newTarPos;
		preparedTarget = newTarget;
		isPreparedManual = isManual;

		RpgGame.inst.playSoundFX( SpellData.souPrepare[newSpellID] );
		RpgGame.inst.playSoundFX( getSouAttack() );

		changeState( StateCasting.class );
		return true;
	}

	//----------

	public void castPreparedSpell( Vector newTarPos, RpgMapObj newTarget ) {
		if( isPreparedManual ) {
			castSpell(  preparedSpell, newTarPos, newTarget ); //use fresh values
		} else {
			castSpell(  preparedSpell, preparedTarPos, preparedTarget ); //use old values
		}
		preparedSpell = 0;
		preparedTarPos = null;
		preparedTarget = null;
		isPreparedManual = false;
		changeState( StateIdle.class );
	}

	//----------

	public void castPreparedSpell() {
		castPreparedSpell(null, null);
	}

	//----------

	public boolean castSpell( int newSpellID, Vector newTarPos, RpgMapObj newTarget ) {
		if (newTarget==null && newTarPos.isAllEqual(pos)) return false; //targeted outside game field;

		AbstractSpell s = SpellData.createSpell(this, preparedSpell, stats.spells[preparedSpell], newTarPos, newTarget);
		if (s!=null) activeSpells.addLast(s);

		return true;
	}

	public void attack( CombatMapObj mo, AbstractSpell spell ) {
    float lifeLoss = 0;

    int norDmg = ((Vector)spell.stats.getStatValue(StatNormalDamage.class)).getRandomIntValue();
    if( (Boolean) spell.stats.getStatValue(StatIgnoreArmor.class) ) {
      lifeLoss = norDmg;
    } else {
      lifeLoss = norDmg * (1 - (Float)mo.stats.getStatValue(StatMulNorDmgReduce.class));
    }
    spell.stats.addOrCreateStat( StatHealth.class, (int)(lifeLoss * (Float)spell.stats.getStatValue(StatLifeLeech.class) ));
    spell.stats.addOrCreateStat( StatMana.class,   (int)(lifeLoss * (Float)spell.stats.getStatValue(StatManaLeech.class) ));

    lifeLoss += ((Vector)spell.stats.getStatValue(StatFireDamage.class)).getRandomIntValue()     *
        (1-(Float)mo.stats.getStatValue(StatResistFire.class));
    lifeLoss += ((Vector)spell.stats.getStatValue(StatLightningDamage.class)).getRandomIntValue() *
        (1-(Float)mo.stats.getStatValue(StatResistLightning.class));
    lifeLoss += ((Vector)spell.stats.getStatValue(StatIceDamage.class)).getRandomIntValue()    *
        (1-(Float)mo.stats.getStatValue(StatResistIce.class));

    lifeLoss -= (Integer)mo.stats.getStatValue(StatReduceDmgTaken.class);
    mo.stats.addOrCreateStat( StatHealth.class, Util.round(lifeLoss * -1) );
    mo.damageRecievedTrigger(this);
  }

	public void damageRecievedTrigger(CombatMapObj attackedBy) {
    if( (Integer)stats.getStatValue(StatHealth.class) <= 0 ) {
      killedBy = attackedBy;
      changeState( StateDying.class );
    } else {
      if( !(currState instanceof StateHit) && Math.random()<0.8 ) {
        changeState(StateHit.class);
      }
      RpgGame.inst.playSoundFX( getSouHit() );
    }

  }

	//----------

	public boolean getCanSeeMO( RpgMapObj newTarget ) {
		Vector tile1=pos.copy().roundAllInst();
		Vector tile2=newTarget.pos.copy().roundAllInst();
		return RpgUtil.tilesInSight( tile1, tile2 );
	}

	//----------

	public class StateMovingCombat extends StateMovingStraight {

	  public boolean execUpdate() {
	    if( footstepCooldown==0 ) {
	      RpgGame.inst.playSoundFX( getSouFootstep() );
	      footstepCooldown = (int) (PhilonGame.inst.fps / 3);
	    }

	    return super.execUpdate();
	  }

	}

	//----------

	public class StateCasting extends AbstractMapObjState {

	  @Override
	  public void execOnChange() {
	    setAnimation(new FrameAnimation(Data.textures.get(getImgCasting()), (int)(PhilonGame.inst.fps/3), false));
	  }

	  @Override
	  public boolean execUpdate() {
	    return true;
	  }

	}

	//----------

	public class StateHit extends AbstractMapObjState {

	  @Override
	  public void execOnChange() {
	    v=0;
	    int newHitFrames = (int)( ((Float)stats.getStatValue(StatHitRecovery.class)+1) * (PhilonGame.inst.fps/3) );
	    hitCooldown = newHitFrames;
	    setAnimation(new FrameAnimation(Data.textures.get(getImgHit()), newHitFrames, false));
	  }

	  @Override
	  public boolean execUpdate() {
	    return true;
	  }

	}

	//----------

	public class StateAttackingSmart extends AbstractMapObjState {
	  private StateMovingTarget m_movingTarget;
	  private int currImg = 0;

	  protected StateMovingTarget getStateMovingTarget() {
	    return m_movingTarget!=null ? m_movingTarget : createState(StateMovingTarget.class);
	  }

	  @Override
	  public void execOnChange() {
	    pathfindCooldown=0;

	    setAnimation(new FrameAnimation(Data.textures.get(getImgCasting()), (int)(PhilonGame.inst.fps/3), false));
	    currImg = getImgCasting();
	  }

	  @Override
	  public boolean execUpdate() {
	    if( currSelectedSpell==SpellData.MELEE ) {
	      if( currTargetDist < getMaxMeleeRange() ) {
	        if(currImg!=getImgCasting()) setAnimation(new FrameAnimation(Data.textures.get(getImgCasting()), (int)(PhilonGame.inst.fps/3), false));
	        prepareSpell( currSelectedSpell, false, currTargetPos, currTarget );
	        return true;
	      } else {
	        if(currImg==getImgCasting()) setAnimation(new FrameAnimation(Data.textures.get(getImgMoving()), (int)(PhilonGame.inst.fps/3), false));
  	      return getStateMovingTarget().execUpdate();
	      }

	    } else {
	      if(currImg==getImgCasting()) setAnimation(new FrameAnimation(Data.textures.get(getImgMoving()), (int)(PhilonGame.inst.fps/3), false));
	      if( !prepareSpell( currSelectedSpell, false, currTargetPos, currTarget ) ) {
	        changeState(getDefaultState());
	        return false;
	      } else {
	        return true;
	      }
	    }
	  }

	}

	//----------

	public class StateInteracting extends AbstractMapObjState {
	  private StateMovingTarget m_movingTarget;

    protected StateMovingTarget getStateMovingTarget() {
      return m_movingTarget!=null ? m_movingTarget : createState(StateMovingTarget.class);
    }

	  @Override
	  public void execOnChange() {
	    assert currTarget.isSelectable;

	    pathfindCooldown=0;
	    if(animation.image!=Data.textures.get(getImgMoving())) setAnimation(new FrameAnimation(Data.textures.get(getImgMoving()), (int)(PhilonGame.inst.fps/3), false));
	  }

	  @Override
	  public boolean execUpdate() {
	    if (currTarget==null) return false; //nothing to interact with;

	    if( !getStateMovingTarget().execUpdate() ) {
        if (currTargetDist < 1.5) { //moved to pos, ready to interact
          if(animation.image!=Data.textures.get(getImgIdle())) setAnimation(new FrameAnimation(Data.textures.get(getImgIdle()), (int)(PhilonGame.inst.fps/3), false));
          interact(currTarget);
          return false; //finished
        } else {
          return false; //couldnt reach target
        }
      }

      return true;
	  }

	}

}












