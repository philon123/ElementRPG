package com.philon.rpg.map.mo;

import java.util.LinkedList;

import com.philon.engine.Data;
import com.philon.engine.FrameAnimation;
import com.philon.engine.PhilonGame;
import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.map.mo.state.MapObjState;
import com.philon.rpg.map.mo.state.StateParam;
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
  protected LinkedList<AbstractSpell> activeSpells = new LinkedList<AbstractSpell>();

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
    return 0.8f;
  }

  @Override
	public boolean update() {
		updateSpells();

		return super.update();
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

	@Override
	public void deathTrigger(CombatMapObj killedBy) {
	  super.deathTrigger(killedBy);

		for( AbstractSpell s : activeSpells ) {
			s.deleteObject();
		}
	}

	public void updateSpells() {
	  for (int i=0; i<activeSpells.size(); i++) {
	    AbstractSpell s = activeSpells.get(i);
	    s.update();
			if (s.isDying) activeSpells.remove(s);
		}
	}

	public boolean useMana(int amount) {
	  if(((Integer)stats.getStatValue(StatMana.class)) - amount < 0) {
	    return false;
	  }
	  stats.addOrCreateStat(StatMana.class, -1*amount);
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

	private void damageRecievedTrigger(CombatMapObj attackedBy) {
    if( (Integer)stats.getStatValue(StatHealth.class) <= 0 ) {
      changeState(StateDying.class, new StateDyingParam(attackedBy));
    } else {
      if( !(currState instanceof StateHit) && Math.random()<0.8 ) {
        changeState(StateHit.class, new StateParam());
      }
      RpgGame.inst.playSoundFX( getSouHit() );
    }

  }

	public boolean getCanSeeMO( RpgMapObj newTarget ) {
		Vector tile1=pos.copy().roundAllInst();
		Vector tile2=newTarget.pos.copy().roundAllInst();
		return RpgUtil.tilesInSight( tile1, tile2 );
	}

  //##################################################

  public class StateHit extends MapObjState<StateParam> {
    private int hitCooldown;
    public StateHit(StateParam param) {
      super(param);
    }
    @Override
    public void execOnChange() {
      hitCooldown = (int)( ((Float)stats.getStatValue(StatHitRecovery.class)+1) * (PhilonGame.inst.fps/3) );
      setAnimation(new FrameAnimation(Data.textures.get(getImgHit()), hitCooldown, false));
    }
    @Override
    public boolean execUpdate() {
      if (hitCooldown==0) {
        return false;
      } else {
        hitCooldown--;
      }
      return true;
    }
    @Override
    public boolean isStateChangeAllowed(Class<? extends MapObjState<?>> newStateClazz) {
      return false;
    }
  }

  public class StateCasting extends MapObjState<StateCastingParam> {
    private int spellID;
    private Vector targetPos;
    private RpgMapObj target;
    private int castCooldown;
    public StateCasting(StateCastingParam param) {
      super(param);
      spellID = param.spellID;
      targetPos = param.targetPos;
      target = param.target;
      if(target!=null) targetPos = target.pos.copy();
      if (targetPos!=null && !targetPos.isAllEqual(new Vector())) turnToTarget(targetPos);
      if (target==null && targetPos.isAllEqual(pos)) spellID = SpellData.EMPTY; //targeted outside game field;
    }
    @Override
    public void execOnChange() {
      if(spellID==SpellData.EMPTY) return;
      if ( !(spellID==SpellData.MELEE || spellID==SpellData.ARROW)
          && !useMana(SpellData.manaCost[spellID][stats.spells[spellID]]) ) {
        return;
      }
      if(spellID==SpellData.MELEE || spellID==SpellData.ARROW) {
        castCooldown = (int) (PhilonGame.inst.fps / (Float)stats.getStatValue(StatAttackRate.class));
      } else {
        castCooldown = (int) (PhilonGame.inst.fps / (Float)stats.getStatValue(StatCastRate.class));
      }
      RpgGame.inst.playSoundFX( SpellData.souPrepare[spellID] );
      RpgGame.inst.playSoundFX( getSouAttack() );
      turnToTarget(targetPos);
      setAnimation(new FrameAnimation(Data.textures.get(getImgCasting()), castCooldown, false));
    }
    @Override
    public boolean execUpdate() {
      if(castCooldown>0) {
        castCooldown--;
        return true;
      }
      castSpell(spellID, targetPos, target);
      spellID = 0;
      targetPos = null;
      target = null;
      return false;
    }
    @Override
    public boolean isStateChangeAllowed(Class<? extends MapObjState<?>> newStateClass) {
      if(castCooldown!=0 && StateCasting.class.isAssignableFrom(newStateClass)) return false;
      return true;
    }
    public void castSpell( int newSpellID, Vector newTarPos, RpgMapObj newTarget ) {
      AbstractSpell s = SpellData.createSpell(CombatMapObj.this, spellID, stats.spells[spellID], newTarPos, newTarget);
      if (s!=null) activeSpells.addLast(s);
    }
  }
  public static class StateCastingParam extends StateParam {
    public int spellID;
    public Vector targetPos;
    public RpgMapObj target;
    public StateCastingParam(int newSpellID, Vector newTargetPos, RpgMapObj newTarget) {
      spellID = newSpellID;
      targetPos = newTargetPos.copy();
      target = newTarget;
    }
  }

	public class StateInteracting extends DefaultAI {
	  private MoveToTargetAI stateMoving;
    private RpgMapObj m_target;
    private Vector m_targetPos;
    public StateInteracting(RpgMapObj newTarget) {
      m_target = newTarget;
      if(m_target!=null) m_targetPos = m_target.pos;
      stateMoving = new MoveToTargetAI(m_targetPos);
    }
    @Override
    public void updateTimed() {
      if(m_target!=null) m_targetPos = m_target.pos;
      if(m_targetPos==null) return;

      float targetDist = Vector.getDistance(pos, m_targetPos);
      if(targetDist>getMaxMeleeRange()) {
        stateMoving.setTargetPos(m_targetPos);
        stateMoving.updateTimed();
      } else {
        interact(m_target);
      }
    }
	}

}


