package com.philon.rpg.map.mo;

import java.util.LinkedList;

import com.philon.engine.Data;
import com.philon.engine.FrameAnimation;
import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.map.mo.state.MapObjState;
import com.philon.rpg.map.mo.state.StateParam;
import com.philon.rpg.spell.AbstractSpell;
import com.philon.rpg.spell.AbstractSpell.SpellDescriptor;
import com.philon.rpg.spell.SpellArrow;
import com.philon.rpg.spell.SpellMelee;
import com.philon.rpg.spell.SpellNone;
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
	public boolean update(float deltaTime) {
		updateSpells(deltaTime);

		return super.update(deltaTime);
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

	public void updateSpells(float deltaTime) {
	  for (int i=0; i<activeSpells.size(); i++) {
	    AbstractSpell s = activeSpells.get(i);
	    s.update(deltaTime);
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
    stats.addOrCreateStat( StatHealth.class, (int)(lifeLoss * (Float)spell.stats.getStatValue(StatLifeLeech.class) ));
    stats.addOrCreateStat( StatMana.class,   (int)(lifeLoss * (Float)spell.stats.getStatValue(StatManaLeech.class) ));

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
    private float hitCooldown;
    public StateHit(StateParam param) {
      super(param);
    }
    @Override
    public void execOnChange() {
      hitCooldown = 0.25f * ((Float)stats.getStatValue(StatHitRecovery.class)+1);
      setAnimation(new FrameAnimation(Data.textures.get(getImgHit()), hitCooldown, false));
    }
    @Override
    public boolean execUpdate(float deltaTime) {
      if (hitCooldown<0) {
        return false;
      } else {
        hitCooldown -= deltaTime;
      }
      return true;
    }
    @Override
    public boolean isStateChangeAllowed(Class<? extends MapObjState<?>> newStateClazz) {
      return false;
    }
  }

  public class StateCasting extends MapObjState<StateCastingParam> {
    private Class<? extends AbstractSpell> spellClass;
    private Vector targetPos;
    private RpgMapObj target;
    private float castCooldown;
    public StateCasting(StateCastingParam param) {
      super(param);
      spellClass = param.spellClass;
      targetPos = param.targetPos;
      target = param.target;
      if(target!=null) targetPos = target.pos.copy();
      if (targetPos!=null && !targetPos.isAllEqual(new Vector())) turnToTarget(targetPos);
      if (target==null && targetPos.isAllEqual(pos)) spellClass = SpellNone.class; //targeted outside game field;
    }
    @Override
    public void execOnChange() {
      if(spellClass==SpellNone.class) return;
      SpellDescriptor descriptor = AbstractSpell.getDescriptor(spellClass);
      if ( !(spellClass==SpellMelee.class || spellClass==SpellArrow.class)
          && !useMana(descriptor.getManacostForLevel(stats.spells.get(spellClass))) ) {
        return;
      }
      if(spellClass==SpellMelee.class || spellClass==SpellArrow.class) {
        castCooldown = 1 / (Float)stats.getStatValue(StatAttackRate.class);
      } else {
        castCooldown = 1 / (Float)stats.getStatValue(StatCastRate.class);
      }
      if(descriptor.getSouPrepare()!=0) RpgGame.inst.playSoundFX(descriptor.getSouPrepare());
      else RpgGame.inst.playSoundFX(getSouAttack());

      turnToTarget(targetPos);
      setAnimation(new FrameAnimation(Data.textures.get(getImgCasting()), castCooldown, false));
    }
    @Override
    public boolean execUpdate(float deltaTime) {
      if(castCooldown>0) {
        castCooldown -= deltaTime;
        return true;
      }
      castPreparedSpell();
      spellClass = null;
      targetPos = null;
      target = null;
      return false;
    }
    @Override
    public boolean isStateChangeAllowed(Class<? extends MapObjState<?>> newStateClass) {
      if(castCooldown>0 && StateCasting.class.isAssignableFrom(newStateClass)) return false;
      return true;
    }
    private void castPreparedSpell() {
      AbstractSpell s = Util.instantiateClass(spellClass, CombatMapObj.this, Util.nvl(stats.spells.get(spellClass), 0), targetPos, target);
      if (s!=null) activeSpells.addLast(s);
    }
  }
  public static class StateCastingParam extends StateParam {
    public Class<? extends AbstractSpell> spellClass;
    public Vector targetPos;
    public RpgMapObj target;
    public StateCastingParam(Class<? extends AbstractSpell> newSpellClass, Vector newTargetPos, RpgMapObj newTarget) {
      spellClass = newSpellClass;
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


