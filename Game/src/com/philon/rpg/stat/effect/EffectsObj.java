package com.philon.rpg.stat.effect;
import java.lang.reflect.InvocationTargetException;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.management.RuntimeErrorException;

import com.philon.engine.util.Util;
import com.philon.engine.util.Util.Order;
import com.philon.engine.util.Util.OrderComparator;
import com.philon.engine.util.Vector;
import com.philon.rpg.spell.AbstractSpell;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.StatsObj.AbstractStat;
import com.philon.rpg.stat.StatsObj.StatArmor;
import com.philon.rpg.stat.StatsObj.StatAttackRate;
import com.philon.rpg.stat.StatsObj.StatCastRate;
import com.philon.rpg.stat.StatsObj.StatDefaultSpell;
import com.philon.rpg.stat.StatsObj.StatDexterity;
import com.philon.rpg.stat.StatsObj.StatFireDamage;
import com.philon.rpg.stat.StatsObj.StatHitRecovery;
import com.philon.rpg.stat.StatsObj.StatIceDamage;
import com.philon.rpg.stat.StatsObj.StatIgnoreArmor;
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

  public EffectsObj() {
		effects = new TreeMap<Class<? extends AbstractEffect>, AbstractEffect>(new OrderComparator());
	}


	public EffectsObj( EffectsObjSaveData eod ) {
		this();

		for( Entry<Class<? extends AbstractEffect>, AbstractEffect> currEntry : eod.effects.entrySet() ) {
		  addOrCreateEffect( currEntry.getKey(), currEntry.getValue().getValue() );
		}
	}

	public EffectsObjSaveData save() {
		return EffectsObjSaveData.create(this);
	}

	public static EffectsObj add( EffectsObj eo1, EffectsObj eo2 ) {
		EffectsObj result = new EffectsObj();

		result.addToSelf( eo1 );
		result.addToSelf( eo2 );

		return result;
	}

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
		targetStats.finalize();
		return targetStats;
	}

	public String getDisplayTextBody() {
		String result = "";
		for( AbstractEffect i : effects.values() ) {
			result += i.getDisplayText() + "\r\n";
		}

		return result;
	}

	public String toString() {
	  String result = "";
	  for( AbstractEffect i : effects.values() ) {
	    result += "[" + i.getClass().getSimpleName() + ": " + i.getValue().toString() + "] ";
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
	    new RuntimeErrorException( null, "Must implement either getStateClass() or effect(StatsObj stats)" );
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
  public class EffectSetDefaultSpell extends AbstractEffect {
    @Override
    public void effect(StatsObj targetStats) {
      super.effect(targetStats);
      targetStats.addOrCreateStat(StatM1Stype.class, getSpellClass());
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
      return AbstractSpell.getDescriptor(getSpellClass()).getName();
    }
    @Override
    public void addValue(Object newValue) {
      setValue(newValue);
    }
    @SuppressWarnings("unchecked")
    @Override
    public void setValue(Object newValue) {
      super.setValue((Class<? extends AbstractSpell>)newValue);
    }
    @SuppressWarnings("unchecked")
    public Class<? extends AbstractSpell> getSpellClass() {
      return (Class<? extends AbstractSpell>)getValue();
    }
  }

  @Order(20)
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

  @Order(30)
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

  @Order(40)
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

  @Order(50)
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

  @Order(60)
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

  @Order(70)
  public class EffectAddVitality extends IntegerEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatVitality.class;
    }
    @Override
    public void effect(StatsObj targetStats) {
      super.effect(targetStats);
      int newAddHealth = Util.round(getIntValue() * (Float)targetStats.getStatValue(StatLifePerVit.class));
      targetStats.addOrCreateStat(StatMaxHealth.class, newAddHealth);
    }
    @Override
    public String getTextWithWildcard() {
      return "## to Vitality";
    }
  }

  @Order(80)
  public class EffectAddMagic extends IntegerEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatMagic.class;
    }
    @Override
    public void effect(StatsObj targetStats) {
      super.effect(targetStats);
      int newAddMana = Util.round(getIntValue() * (Float)targetStats.getStatValue(StatManaPerMag.class));
      targetStats.addOrCreateStat(StatMaxMana.class, newAddMana);
    }
    @Override
    public String getTextWithWildcard() {
      return "## to Magic";
    }
  }

  //TODO AddAllAttributes: remove this, add logic to AddAllAttributesSuffix.
  @Order(90)
  public class EffectAddAllAttributes extends IntegerEffect {
    @Override
    public void effect(StatsObj targetStats) {
      int intValue = getIntValue();

      //strength
      targetStats.addOrCreateStat( StatStrength.class, intValue );
      targetStats.addOrCreateStat( StatNormalDamage.class, new Vector(intValue/3.0f) );
      targetStats.addOrCreateStat( StatArmor.class, Util.round(intValue/5.0f) );

      //dexterity
      targetStats.addOrCreateStat( StatDexterity.class, intValue );
      targetStats.addOrCreateStat( StatArmor.class, Util.round(intValue/4.0f) );

      //vitality
      targetStats.addOrCreateStat( StatVitality.class, intValue );
      int newAddHealth = Util.round(getIntValue() * (Float)targetStats.getStatValue(StatLifePerVit.class));
      targetStats.addOrCreateStat(StatMaxHealth.class, newAddHealth);

      //magic
      targetStats.addOrCreateStat( StatMagic.class, intValue );
      int newAddMana = Util.round(getIntValue() * (Float)targetStats.getStatValue(StatManaPerMag.class));
      targetStats.addOrCreateStat(StatMaxMana.class, newAddMana);
    }
    @Override
    public String getTextWithWildcard() {
      return "## to all Attributes";
    }
  }

  @Order(100)
  public class EffectAddArmor extends IntegerEffect {
    @Override
    public void effect(StatsObj targetStats) {
      targetStats.addOrCreateStat( StatArmor.class, getIntValue() );
    }
    @Override
    public String getTextWithWildcard() {
      return "Armor: ##";
    }
  }

  @Order(110)
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

  @Order(120)
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

  @Order(130)
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

  @Order(140)
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

  @Order(150)
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

  @Order(160)
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

  @Order(170)
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

  @Order(180)
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

  @Order(190)
  public class EffectAddElectricDamage extends VectorEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatLightningDamage.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "Adds Electric Damage: ##";
    }
  }

  @Order(200)
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

  @Order(210)
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

  @Order(220)
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

  @Order(230)
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

  @Order(240)
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

  @Order(250)
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

  @Order(260)
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

  @Order(270)
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

  @Order(280)
  public class EffectAddMaxMana extends IntegerEffect {
    @Override
    public Class<? extends AbstractStat<?>> getStatClass() {
      return StatMaxMana.class;
    }
    @Override
    public String getTextWithWildcard() {
      return "## to Max Mana";
    }
    @Override
    public void effect(StatsObj targetStats) {
      super.effect(targetStats);
    }
  }

  @Order(290)
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

  @Order(310)
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

  @Order(320)
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

  @Order(330)
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

  @Order(340)
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

}
