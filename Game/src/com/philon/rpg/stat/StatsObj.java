package com.philon.rpg.stat;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.philon.engine.util.Vector;
import com.philon.rpg.spell.SpellData;

public class StatsObj {
	public int[] spells = new int[SpellData.numSpells];

	@SuppressWarnings("rawtypes")
  public LinkedHashMap<Class<? extends AbstractStat>, AbstractStat> statMap 
	 = new LinkedHashMap<Class<? extends AbstractStat>, AbstractStat>();
	
	public StatsObj() {
	}

	@SuppressWarnings("unchecked")
  public <T> T getStat(Class<T> clazz) {
	  T result = (T) statMap.get(clazz);
	  return result;
  }
	
	@SuppressWarnings("rawtypes")
  public Object getStatValue(Class<? extends AbstractStat> clazz) {
	  AbstractStat stat = getStat(clazz);
	  return stat==null ? createStat(clazz, null).getValue() : stat.getValue();
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public AbstractStat<?> createStat(Class<? extends AbstractStat> clazz, Object newValue) {
	  AbstractStat newStat = null;
	  try {
        newStat = clazz.getDeclaredConstructor(StatsObj.class).newInstance( this );
        newStat.setValue(newValue);
        
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
	  
	  return newStat;
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
  public void addOrCreateStat( Class<? extends AbstractStat> clazz, Object newValue ) {
	  if (statMap.containsKey(clazz)) {
	    statMap.get(clazz).addValue(newValue);
	  } else {
      statMap.put(clazz, createStat(clazz, newValue));
  	}
  }

	public Vector getTotalDamage() {
	  Vector norDmg = (Vector) getStatValue(StatNormalDamage.class);
	  if (norDmg==null) norDmg=new Vector();
	  Vector firDmg = (Vector) getStatValue(StatFireDamage.class);
	  if (firDmg==null) firDmg=new Vector();
	  Vector eleDmg = (Vector) getStatValue(StatLightningDamage.class);
	  if (eleDmg==null) eleDmg=new Vector();
	  Vector magDmg = (Vector) getStatValue(StatIceDamage.class);
	  if (magDmg==null) magDmg=new Vector();
	  
	  return new Vector().
	      addInst(norDmg).
	      addInst(firDmg).
	      addInst(eleDmg).
	      addInst(magDmg);
	}
	
	@SuppressWarnings("rawtypes")
  public boolean isReqMet( StatsObj req ) {
	  for (Entry<Class<? extends AbstractStat>, AbstractStat> currReqEntry : req.statMap.entrySet()) {
	    Object reqValue = req.getStatValue(currReqEntry.getKey());
	    Object thisValue = getStatValue(currReqEntry.getKey());
	    
	    if ( IntegerStat.class.isAssignableFrom(currReqEntry.getKey()) ) {
	      if ((Integer)thisValue < (Integer)reqValue) return false;
	    } else if ( FloatStat.class.isAssignableFrom(currReqEntry.getKey()) ) {
	      if ((Float)thisValue < (Float)reqValue) return false;
	    } else if ( VectorStat.class.isAssignableFrom(currReqEntry.getKey()) ) {
	      if (((Vector)thisValue).isAllSOE((Vector)reqValue)) return false;
	    }
	  }
		return true;
	}

	public String getReqDisplayTextBody() {
		String dt = "";

		if (getStat(StatStrength.class) != null)  dt += "Required  Strength: "  + (Integer)getStatValue(StatStrength.class) +  "\r\n";
		if (getStat(StatDexterity.class) != null) dt += "Required  Dexterity: " + (Integer)getStatValue(StatDexterity.class) + "\r\n";
		if (getStat(StatMagic.class) != null)     dt += "Required  Magic: "     + (Integer)getStatValue(StatMagic.class) +     "\r\n";

		return dt;
	}

	public abstract class AbstractStat<T> {
    T value;
    
    public T getValue() {
      return value;
    }
    
    public void setValue(T newValue) {
      if (newValue==null) {
        value = getDefaultValue();
      } else {
        value = newValue;
      }
    }
    
    public abstract T getDefaultValue();
    public abstract void addValue(T newValue);
  }
  
  public abstract class IntegerStat extends AbstractStat<Integer> {
    public void addValue(Integer newValue) {
      setValue(getValue() + newValue);
    }
    @Override
    public Integer getDefaultValue() {
      return 0;
    }
  }
  
  public abstract class FloatStat extends AbstractStat<Float> {
    public void addValue(Float newValue) {
      setValue(getValue() + newValue);
    }
    @Override
    public Float getDefaultValue() {
      return 0f;
    }
  }
  
  public abstract class BooleanStat extends AbstractStat<Boolean> {
    public void addValue(Boolean newValue) {
      if (newValue) setValue(true);
    }
    @Override
    public Boolean getDefaultValue() {
      return false;
    }
  }
  
  public abstract class VectorStat extends AbstractStat<Vector> {
    public void addValue(Vector newValue) {
      setValue( Vector.add(getValue(), (Vector)newValue) );
    }
    @Override
    public Vector getDefaultValue() {
      return new Vector();
    }
    public int getRandomIntValue() {
      return getValue().getRandomIntValue();
    }
    public float getRandomFloatValue() {
      return getValue().getRandomFloatValue();
    }
  }
  
  public class StatStrength extends IntegerStat {}
  public class StatDexterity extends IntegerStat {}
  public class StatVitality extends IntegerStat {}
  public class StatMagic extends IntegerStat {}
  public class StatMaxHealth extends IntegerStat {
    @Override
    public void addValue(Integer newValue) {
      int newMaxHealth = getValue() + newValue;
      if (newMaxHealth < 0) {
        setValue(0);
      } else {
        setValue(newValue);
      }
    }
  }
  public class StatHealth extends IntegerStat {
    @Override
    public void addValue(Integer newValue) {
      int maxHealth = (Integer)getStatValue(StatMaxHealth.class);
      int newHealth = getValue() + newValue;
      if (newHealth > maxHealth) {
        setValue(maxHealth);
      } else if (newHealth<0) {
        setValue(0);
      } else {
        setValue(newHealth);
      }
    }
  }
  public class StatMaxMana extends IntegerStat {
    @Override
    public void addValue(Integer newValue) {
      int newMaxMana = getValue() + newValue;
      if (newMaxMana < 0) {
        setValue(0);
      } else {
        setValue(newValue);
      }
    }
  }
  public class StatMana extends IntegerStat {
    @Override
    public void addValue(Integer newValue) {
      int maxMana = (Integer)getStatValue(StatMaxMana.class);
      int newMana = getValue() + newValue;
      if ( newMana > maxMana) {
        setValue(maxMana);
      } else if (newMana<0) {
        setValue(0);
      } else {
        setValue(newMana);
      }
    }
  }
  public class StatLifeLeech extends FloatStat {}
  public class StatManaLeech extends FloatStat {}
  public class StatHitRecovery extends FloatStat {}
  public class StatMulNorDmgReduce extends FloatStat {}
  public class StatMulDmgReflected extends FloatStat {}
  public class StatIgnoreArmor extends BooleanStat {}
  public class StatMulMagicFind extends FloatStat {}
  public class StatAttackRate extends FloatStat {}
  public class StatCastRate extends FloatStat {}
  public class StatMulLightRadius extends FloatStat {}
  public class StatSpelllevels extends IntegerStat {}
  public class StatLifePerVit extends FloatStat {}
  public class StatManaPerMag extends FloatStat {}
  public class StatCurrLevel extends IntegerStat {}
  public class StatReduceDmgTaken extends IntegerStat {}
  public class StatResistFire extends FloatStat {}
  public class StatResistLightning extends FloatStat {}
  public class StatResistIce extends FloatStat {}
  public class StatArmor extends IntegerStat {
    @Override
    public void addValue(Integer newValue) {
      setValue(newValue);
      addOrCreateStat( StatMulNorDmgReduce.class, newValue/200f );
    }
  }
  public class StatNormalDamage extends VectorStat {}
  public class StatFireDamage extends VectorStat {}
  public class StatLightningDamage extends VectorStat {}
  public class StatIceDamage extends VectorStat {}
  public class StatDurability extends IntegerStat {}
  public class StatIndestructable extends BooleanStat {}
  public class StatM1Stype extends IntegerStat {
    @Override
    public Integer getDefaultValue() {
      return SpellData.EMPTY;
    }
    @Override
    public void addValue(Integer newValue) {
      if (getValue()==SpellData.EMPTY || (getValue()==SpellData.MELEE || getValue()==SpellData.ARROW)) {
        setValue(newValue);
      }
    }
//    @Override
//    public void addValue(Object newValue) {
//      if (newValue instanceof Class<?> &&  AbstractSpell.class.isAssignableFrom((Class<?>)newValue)) {
//        setValue(newValue);
//      }
//    }
  }
  public class StatM2Stype extends IntegerStat {
    @Override
    public Integer getDefaultValue() {
      return SpellData.EMPTY;
    }
//    @Override
//    public void addValue(Object newValue) {
//      if (newValue instanceof Class<?> &&  AbstractSpell.class.isAssignableFrom((Class<?>)newValue)) {
//        setValue(newValue);
//      }
//    }
  }
  public class StatDefaultSpell extends IntegerStat {
    @Override
    public Integer getDefaultValue() {
      return SpellData.EMPTY;
    }
    @Override
    public void addValue(Integer newValue) {
      setValue(newValue);
    }
  }

}