package com.philon.rpg.stat.presuf;

import java.util.LinkedHashMap;

import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;
import com.philon.rpg.stat.effect.EffectsObj;
import com.philon.rpg.stat.effect.EffectsObj.AbstractEffect;

public abstract class AbstractPrefixSuffix {
  public LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> effectLevelData;
  public int level;
  public String[] displayTexts;
  public int[] dropValues;
  public EffectsObj effects;
  
  public AbstractPrefixSuffix(int maxDropValue) {
    effectLevelData = getEffectLevelData();
    displayTexts = getDisplayTexts();
    dropValues = getDropValues();
    level = getRandomLevelFromDropValue(maxDropValue);
    
    updateEffects();
  }
  
  public int getRandomLevelFromDropValue(int maxDropValue) {
    int maxLevel=0;
    for (int currLevel=dropValues.length-1; currLevel>=0; currLevel--) {
      if (dropValues[currLevel]<=maxDropValue) {
        maxLevel = currLevel;
        break;
      }
    }
    return (int) Util.random(0, maxLevel);
  }
  
  public abstract LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> getEffectLevelData();
  public abstract int[] getDropValues();
  public abstract String[] getDisplayTexts();
  
  public void updateEffects() {
    effects = new EffectsObj();
    for (AbstractEffectLevelData currELD : effectLevelData.values()) {
      effects.addOrCreateEffect(currELD.getEffectClass(), currELD.getRandomValueForLevel(level));
    }
  }
  
  public int getDropValue() {
    return dropValues[level];
  }
  
  public String getDisplayTet() {
    return displayTexts[level];
  }
  
  public PrefixSuffixSaveData save() {
    return PrefixSuffixSaveData.create(this);
  }
  
  public abstract class AbstractEffectLevelData {
    public AbstractEffectLevelData() {
    }
    
    public abstract Class<? extends AbstractEffect> getEffectClass();
    public abstract Object getRandomValueForLevel(int level);
  }
  
  public abstract class NumericEffectLevelData extends AbstractEffectLevelData  {
    @Override
    public Object getRandomValueForLevel(int level) {
      Vector range = getNumericValueRangeForLevel(level);
      return Util.random(range.x, range.y);
    }
    
    public abstract Vector getNumericValueRangeForLevel(int level);
    
    public Vector getValueRangeLinear(Vector minRange, Vector step, int forLevel) {
      return Vector.add(minRange, Vector.mulScalar(step, forLevel));
    }
  }
  
  public abstract class IntegerEffectLevelData extends NumericEffectLevelData  {
    @Override
    public Object getRandomValueForLevel(int level) {
      return getRandomIntValueForLevel(level);
    }
    
    public int getRandomIntValueForLevel(int level) {
      Vector range = getNumericValueRangeForLevel(level);
      return Util.round( Util.random(range.x, range.y) );
    }
  }
  
  public abstract class FloatEffectLevelData extends NumericEffectLevelData  {
    @Override
    public Object getRandomValueForLevel(int level) {
      return getRandomFloatValueForLevel(level);
    }
    
    public float getRandomFloatValueForLevel(int level) {
      Vector range = getNumericValueRangeForLevel(level);
      return Util.random(range.x, range.y);
    }
  }
  
  public abstract class VectorEffectLevelData extends AbstractEffectLevelData {
    @Override
    public Object getRandomValueForLevel(int level) {
      return getRandomVectorValueForLevel(level);
    }
    
    public Vector getRandomVectorValueForLevel(int level) {
      Vector[] range = getVectorValueRangeForLevel(level);
      return new Vector( new Vector(range[0].x, range[1].x).getRandomIntValue(), 
          new Vector(range[0].y, range[1].y).getRandomIntValue() );
    }
    
    public Vector[] getVectorValueRangeLinear(Vector[] minRange, Vector[] step, int forLevel) {
      Vector minRangeForLevel = Vector.add( minRange[0], step[0].mulScalarInst(forLevel) );
      Vector maxRangeForLevel = Vector.add( minRange[1], step[1].mulScalarInst(forLevel) );
      return new Vector[]{minRangeForLevel, maxRangeForLevel};
    }
    
    public abstract Vector[] getVectorValueRangeForLevel(int level);
  }
  
  public abstract class BooleanEffectLevelData extends AbstractEffectLevelData {
    @Override
    public Object getRandomValueForLevel(int level) {
      return getBooleanValueForLevel(level);
    }
    
    public abstract boolean getBooleanValueForLevel(int level);
  }
  
}
