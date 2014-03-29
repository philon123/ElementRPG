package com.philon.rpg.stat.effect;
import java.lang.reflect.InvocationTargetException;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.management.RuntimeErrorException;

import com.philon.engine.util.Util;
import com.philon.engine.util.Util.Order;
import com.philon.engine.util.Util.OrderComperator;
import com.philon.engine.util.Vector;
import com.philon.rpg.spell.SpellData;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.AbstractStat;
import com.philon.rpg.stat.StatsObj.StatArmor;
import com.philon.rpg.stat.StatsObj.StatAttackRate;
import com.philon.rpg.stat.StatsObj.StatCastRate;
import com.philon.rpg.stat.StatsObj.StatDefaultSpell;
import com.philon.rpg.stat.StatsObj.StatDexterity;
import com.philon.rpg.stat.StatsObj.StatDurability;
import com.philon.rpg.stat.StatsObj.StatFireDamage;
import com.philon.rpg.stat.StatsObj.StatHitRecovery;
import com.philon.rpg.stat.StatsObj.StatIceDamage;
import com.philon.rpg.stat.StatsObj.StatIgnoreArmor;
import com.philon.rpg.stat.StatsObj.StatIndestructable;
import com.philon.rpg.stat.StatsObj.StatLifeLeech;
import com.philon.rpg.stat.StatsObj.StatLifePerVit;
import com.philon.rpg.stat.StatsObj.StatLightningDamage;
import com.philon.rpg.stat.StatsObj.StatM1Stype;
import com.philon.rpg.stat.StatsObj.StatMagic;
import com.philon.rpg.stat.StatsObj.StatManaLeech;
import com.philon.rpg.stat.StatsObj.StatManaPerMag;
import com.philon.rpg.stat.StatsObj.StatMaxHealth;
import com.philon.rpg.stat.StatsObj.StatMaxMana;
import com.philon.rpg.stat.StatsObj.StatMulDmgReflected;
import com.philon.rpg.stat.StatsObj.StatMulLightRadius;
import com.philon.rpg.stat.StatsObj.StatMulMagicFind;
import com.philon.rpg.stat.StatsObj.StatMulNorDmgReduce;
import com.philon.rpg.stat.StatsObj.StatNormalDamage;
import com.philon.rpg.stat.StatsObj.StatReduceDmgTaken;
import com.philon.rpg.stat.StatsObj.StatResistFire;
import com.philon.rpg.stat.StatsObj.StatResistIce;
import com.philon.rpg.stat.StatsObj.StatResistLightning;
import com.philon.rpg.stat.StatsObj.StatSpelllevels;
import com.philon.rpg.stat.StatsObj.StatStrength;
import com.philon.rpg.stat.StatsObj.StatVitality;

public class EffectsObj {
  public TreeMap<Class<? extends AbstractEffect>, AbstractEffect> effects;

	//----------

  public EffectsObj() {
		effects = new TreeMap<Class<? extends AbstractEffect>, AbstractEffect>(new OrderComperator());
	}


	//----------

	public EffectsObj( EffectsObjSaveData eod ) {
		this();

		for( Entry<Class<? extends AbstractEffect>, AbstractEffect> currEntry : eod.effects.entrySet() ) {
		  addOrCreateEffect( currEntry.getKey(), currEntry.getValue().getValue() );
		}
	}

	//----------

	public EffectsObjSaveData save() {
		return EffectsObjSaveData.create(this);
	}

	//----------

	public static EffectsObj add( EffectsObj eo1, EffectsObj eo2 ) {
		EffectsObj result = new EffectsObj();

		result.addToSelf( eo1 );
		result.addToSelf( eo2 );

		return result;
	}

	//----------

	public void addOrCreateEffect( Class<? extends AbstractEffect> clazz, Object newValue ) {
	  if (effects.containsKey(clazz)) {
	    effects.get(clazz).addValue(newValue);
    } else {
      try {
        AbstractEffect newEffect = clazz.getDeclaredConstructor(EffectsObj.class).newInstance( this );
        newEffect.setValue(newValue);
        effects.put(clazz, newEffect);
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (SecurityException e) {
        e.printStackTrace();
      }
    }
	}

	@SuppressWarnings("unchecked")
  public <T> T getEffect(Class<T> clazz) {
    return (T) effects.get(clazz);
  }

	public void addToSelf( EffectsObj otherEO ) {
		if (otherEO.effects == null) return;

		for( AbstractEffect i : otherEO.effects.values() ) {
		  addOrCreateEffect(i.getClass(), i.getValue());
		}
	}

	public StatsObj getStats() {

	  StatsObj targetStats  = new StatsObj();
		for( AbstractEffect i : effects.values() ) {
			i.effect(targetStats);
		}
		return targetStats;
	}

	public String getDisplayTextBody() {
		String result = "";
		for( AbstractEffect i : effects.values() ) {
			result += i.getDisplayText() + "\r\n";
		}

		return result;
	}

	public abstract class AbstractEffect {
	  public Object value;

	  public Object getValue() {
      return value;
    }

    public void setValue(Object newValue) {
      value = newValue;
    }

    public abstract void addValue(Object newValue);

	  public void effect(StatsObj targetStats) { //override to effect multiple stats
	    targetStats.addOrCreateStat(getStatClass(), getValue());
	  }

	  public Class<? extends AbstractStat<?>> getStatClass() { //override to perform simple add to given Stat
	    new RuntimeErrorException( null, "Must implement eather getStateClass() or effect(StatsObj stats)" );
	    return null;
	  }

	  public String getDisplayText() {
	    return getTextWithWildcard().replaceAll( "##", getValueText() );
	  }

	  public abstract String getTextWithWildcard();
	  public abstract String getValueText();

	}

  public abstract class IntegerEffect extends AbstractEffect {
    @Override
    public String getValueText() {
      return Util.getSignedIntString(getIntValue());
    }

    @Override
    public void setValue(Object newValue) {
      if (!(newValue instanceof Integer)) new RuntimeErrorException(null, "Wrong datatype");
      setIntValue((Integer)newValue);
    }

    @Override
    public void addValue(Object newValue) {
      if (!(newValue instanceof Integer)) new RuntimeErrorException(null, "Wrong datatype");
      addIntValue((Integer)newValue);
    }

    public int getIntValue() {
      return (Integer)getValue();
    }

    public void setIntValue(int newValue) {
      value = newValue;
    }

    public void addIntValue(int newValue) {
      setIntValue(getIntValue() + newValue);
    }
  }

  public abstract class FloatEffect extends AbstractEffect {
    @Override
    public String getValueText() {
      return Util.getSignedPercentageString(getFloatValue());
    }

    @Override
    public void setValue(Object newValue) {
      if (!(newValue instanceof Float)) new RuntimeErrorException(null, "Wrong datatype");
      setFloatValue((Float)newValue);
    }

    @Override
    public void addValue(Object newValue) {
      if (!(newValue instanceof Float)) new RuntimeErrorException(null, "Wrong datatype");
      addFloatValue((Float)newValue);
    }

    public float getFloatValue() {
      return (Float)getValue();
    }

    public void setFloatValue(float newValue) {
      value = newValue;
    }

    public void addFloatValue(float newValue) {
      setFloatValue(getFloatValue() + newValue);
    }
  }

  public abstract class BooleanEffect extends AbstractEffect {
    @Override
    public String getValueText() {
      return getBooleanValue()==true ? "True" : "False";
    }

    @Override
    public void setValue(Object newValue) {
      if (!(newValue instanceof Boolean)) new RuntimeErrorException(null, "Wrong datatype");
      setBooleanValue((Boolean)newValue);
    }

    @Override
    public void addValue(Object newValue) {
      if (!(newValue instanceof Boolean)) new RuntimeErrorException(null, "Wrong datatype");
      addBooleanValue((Boolean)newValue);
    }

    public boolean getBooleanValue() {
      return (Boolean)getValue();
    }

    public void setBooleanValue(boolean newValue) {
      value = newValue;
    }

    public void addBooleanValue(boolean newValue) {
      if (newValue) setBooleanValue(true);
    }
  }

  public abstract class VectorEffect extends AbstractEffect {
    @Override
    public String getValueText() {
      return getVectorValue().toStringIntRange();
    }

    @Override
    public void setValue(Object newValue) {
      if (!(newValue instanceof Vector)) new RuntimeErrorException(null, "Wrong datatype");
      setVectorValue((Vector)newValue);
    }

    @Override
    public void addValue(Object newValue) {
      if (!(newValue instanceof Vector)) new RuntimeErrorException(null, "Wrong datatype");
      addVectorValue((Vector)newValue);
    }

    public Vector getVectorValue() {
      return (Vector)getValue();
    }

    public void setVectorValue(Vector newValue) {
      value = newValue;
    }

    public void addVectorValue(Vector newValue) {
      setVectorValue( Vector.add(getVectorValue(), (Vector)newValue) );
    }
  }

  //########################################################################################################

  @Order(10)
  public class EffectAddLifePerVit extends FloatEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatLifePerVit.class;
    }
    @Override
    public String getValueText() {
      return Util.getSignedFloatString(getFloatValue());
    }
    @Override
    public String getTextWithWildcard() {
      return "Life per Vitality: ##";
    }
  }

  @Order(20)
  public class EffectAddManaPerMag extends FloatEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatManaPerMag.class;
    }
    @Override
    public String getValueText() {
      return Util.getSignedFloatString(getFloatValue());
    }
    @Override
    public String getTextWithWildcard() {
      return "Mana per Magic: ##";
    }
  }

  @Order(30)
  public class EffectAddArmor extends IntegerEffect {
    @Override
    public void effect(StatsObj targetStats) {
      targetStats.addOrCreateStat( StatMulNorDmgReduce.class, getIntValue()/200f );
    }
    @Override
    public String getValueText() {
      return String.valueOf(getValue());
    }
    @Override
    public String getTextWithWildcard() {
      return "Armor: ##";
    }
  }

  @Order(40)
  public class EffectMulArmor extends FloatEffect {
    @Override
    public void effect(StatsObj stats) {
      if (stats.getStat(StatArmor.class)!=null) {
        stats.getStat(StatArmor.class).setValue(
            Util.round((Integer)stats.getStatValue(StatArmor.class) * (getFloatValue()+1))
            );
      }
    }
    @Override
    public String getTextWithWildcard() {
      return "## Armor";
    }
  }

  @Order(50)
  public class EffectAddDamageReflected extends FloatEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatMulDmgReflected.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "## Damage Reflected";
    }
  }

  @Order(60)
  public class EffectAddHitRecovery extends FloatEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatHitRecovery.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "## Faster Hit Recovery";
    }
  }

  @Order(70)
  public class EffectAddDamageReduce extends IntegerEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatReduceDmgTaken.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "Damage Reduced by ##";
    }
  }

  @Order(80)
  public class EffectAddResistFire extends FloatEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatResistFire.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "## Fire Resist";
    }
  }

  @Order(90)
  public class EffectAddResistLightning extends FloatEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatResistLightning.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "## Lightning Resist";
    }
  }

  @Order(100)
  public class EffectAddResistIce extends FloatEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatResistIce.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "## Resist Magic";
    }
  }

  @Order(110)
  public class EffectAddResistAll extends FloatEffect {
    @Override
    public void effect(StatsObj targetStats) {
      targetStats.addOrCreateStat( StatResistFire.class, getFloatValue() );
      targetStats.addOrCreateStat( StatResistLightning.class, getFloatValue() );
      targetStats.addOrCreateStat( StatResistIce.class, getFloatValue() );
    }
    @Override
    public String getTextWithWildcard() {
      return "## Resist All";
    }
  }

  @Order(120)
  public class EffectAddNormalDamage extends VectorEffect {
    @Override
    public void effect(StatsObj targetStats) {
      targetStats.addOrCreateStat(StatNormalDamage.class, getVectorValue() );
    }
    @Override
    public String getTextWithWildcard() {
      return "Damage: ##";
    }
  }

  @Order(130)
  public class EffectMulNormalDamage extends FloatEffect {
    @Override
    public void effect(StatsObj stats) {
      if (stats.getStat(StatNormalDamage.class)!=null) {
        stats.getStat(StatNormalDamage.class).setValue(
            Vector.mulScalar((Vector)stats.getStatValue(StatNormalDamage.class), getFloatValue()+1)
            );
      }
    }
    @Override
    public String getTextWithWildcard() {
      return "## Normal Damage";
    }
  }

  @Order(140)
  public class EffectAddFireDamage extends VectorEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatFireDamage.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "Adds Fire Damage: ##";
    }
  }

  @Order(150)
  public class EffectAddLightningDamage extends VectorEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatLightningDamage.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "Adds Lightning Damage: ##";
    }
  }

  @Order(160)
  public class EffectAddIceDamage extends VectorEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatIceDamage.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "Adds Ice Damage: ##";
    }
  }

  @Order(170)
  public class EffectSetIgnoreArmor extends BooleanEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatIgnoreArmor.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "Ignores Target Armor";
    }
  }

  @Order(180)
  public class EffectAddAttackRate extends FloatEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatAttackRate.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "Attack Rate: ##";
    }
  }

  @Order(190)
  public class EffectMulAttackRate extends FloatEffect {
    @Override
    public void effect(StatsObj targetStats) {
      if (targetStats.getStat(StatAttackRate.class)!=null) {
        targetStats.getStat(StatAttackRate.class).setValue(
            (Float)targetStats.getStatValue(StatAttackRate.class) * (getFloatValue()+1)
          );
      }
    }
    @Override
    public String getTextWithWildcard() {
      return "## Attack Rate";
    }
  }

  @Order(200)
  public class EffectAddCastRate extends FloatEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatCastRate.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "Attack Rate: ##";
    }
  }

  @Order(210)
  public class EffectMulCastRate extends FloatEffect {
    @Override
    public void effect(StatsObj targetStats) {
      if (targetStats.getStat(StatCastRate.class)!=null) {
        targetStats.getStat(StatCastRate.class).setValue(
            (Float)targetStats.getStatValue(StatCastRate.class) * (getFloatValue()+1)
            );
      }
    }
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatAttackRate.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "## Attack Rate";
    }
  }

  @Order(220)
  public class EffectAddLifeLeech extends FloatEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatLifeLeech.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "## Life Leech";
    }
  }

  @Order(230)
  public class EffectAddManaLeech extends FloatEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatManaLeech.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "## Mana Leech";
    }
  }

  @Order(240)
  public class EffectAddLightRadius extends FloatEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatMulLightRadius.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "## Light Radius";
    }
  }

  @Order(250)
  public class EffectAddMagicFind extends FloatEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatMulMagicFind.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "## Magic Find";
    }
  }

  @Order(260)
  public class EffectAddMaxHealth extends IntegerEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatMaxHealth.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "## to Max Health";
    }
  }

  @Order(270)
  public class EffectAddMaxMana extends IntegerEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatMaxMana.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "## to Max Mana";
    }
  }

  @Order(280)
  public class EffectAddSpellLevels extends IntegerEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatSpelllevels.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "## to All Spell Levels";
    }
  }

  @Order(300)
  public class EffectAddStrength extends IntegerEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatStrength.class;
    }
    @Override
    public void effect(StatsObj targetStats) {
      super.effect(targetStats);
      targetStats.addOrCreateStat( StatNormalDamage.class, new Vector(getIntValue()/3.0f) );
      targetStats.addOrCreateStat( StatArmor.class, Util.round(getIntValue()/5.0f) );
    }
    @Override
    public String getTextWithWildcard() {
      return "## to Strength";
    }
  }

  @Order(310)
  public class EffectAddDexterity extends IntegerEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatDexterity.class;
    }
    @Override
    public void effect(StatsObj targetStats) {
      super.effect(targetStats);
      targetStats.addOrCreateStat( StatArmor.class, Util.round(getIntValue()/4.0f) );
    }
    @Override
    public String getTextWithWildcard() {
      return "## to Dexterity";
    }
  }

  @Order(320)
  public class EffectAddVitality extends IntegerEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatVitality.class;
    }
    @Override
    public void effect(StatsObj targetStats) {
      super.effect(targetStats);
      targetStats.addOrCreateStat( StatMaxHealth.class,
          Util.round(getIntValue() * (Float)targetStats.getStatValue(StatLifePerVit.class)) );
    }
    @Override
    public String getTextWithWildcard() {
      return "## to Vitality";
    }
  }

  @Order(330)
  public class EffectAddMagic extends IntegerEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatMagic.class;
    }
    @Override
    public void effect(StatsObj targetStats) {
      super.effect(targetStats);
      targetStats.addOrCreateStat( StatMaxMana.class,
          Util.round(getIntValue() * (Float)targetStats.getStatValue(StatManaPerMag.class)) );
    }
    @Override
    public String getTextWithWildcard() {
      return "## to Magic";
    }
  }

  @Order(340)
  public class EffectAddAllAttributes extends IntegerEffect {
    @Override
    public void effect(StatsObj targetStats) {
      targetStats.addOrCreateStat( StatStrength.class, getIntValue() );
      targetStats.addOrCreateStat( StatDexterity.class, getIntValue() );
      targetStats.addOrCreateStat( StatVitality.class, getIntValue() );
      targetStats.addOrCreateStat( StatMagic.class, getIntValue() );
    }
    @Override
    public String getTextWithWildcard() {
      return "## to all Attributes";
    }
  }

  @Order(350)
  public class EffectSetLooseAllMana extends BooleanEffect {
    @Override
    public void effect(StatsObj targetStats) {
      if (targetStats.getStat(StatMaxMana.class)!=null) {
        targetStats.getStat(StatMaxMana.class).setValue(0);
      }
    }
    @Override
    public String getTextWithWildcard() {
      return "Loose All Mana";
    }
  }

  @Order(360)
  public class EffectAddDurability extends IntegerEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatDurability.class;
    }
    @Override
    public String getValueText() {
      return String.valueOf(getIntValue());
    }
    @Override
    public String getTextWithWildcard() {
      return "Durability: ##";
    }
  }

  @Order(370)
  public class EffectMulDurability extends FloatEffect {
    @Override
    public void effect(StatsObj targetStats) {
      if (targetStats.getStat(StatDurability.class)!=null) {
        targetStats.getStat(StatDurability.class).setValue(
            Util.round((Integer)targetStats.getStatValue(StatDurability.class) * (getFloatValue()+1))
            );
      }
    }
    @Override
    public String getTextWithWildcard() {
      return "## Durability";
    }
  }

  @Order(380)
  public class EffectSetIndestructable extends BooleanEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatIndestructable.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "Indestructable";
    }
  }

  @Order(390)
  public class EffectSetDefaultSpell extends IntegerEffect {
    @Override
    public void effect(StatsObj targetStats) {
      super.effect(targetStats);
      targetStats.addOrCreateStat(StatM1Stype.class, getIntValue());
    }
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatDefaultSpell.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "##";
    }
    @Override
    public String getValueText() {
      return getIntValue()==SpellData.MELEE ? "Melee" : getIntValue()==SpellData.ARROW ? "Ranged" : "Spell";
    }
  }

}
