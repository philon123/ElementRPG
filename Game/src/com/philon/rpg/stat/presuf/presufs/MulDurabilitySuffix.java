package com.philon.rpg.stat.presuf.presufs;

import java.util.LinkedHashMap;

import com.philon.engine.util.Vector;
import com.philon.rpg.stat.effect.EffectsObj.AbstractEffect;
import com.philon.rpg.stat.effect.EffectsObj.EffectMulDurability;
import com.philon.rpg.stat.presuf.AbstractSuffix;

public class MulDurabilitySuffix extends AbstractSuffix {

  public MulDurabilitySuffix(int newLevel) {
    super(newLevel);
  }

  @Override
  public LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> getEffectLevelData() {
    LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> result = 
        new LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData>();
    
    result.put(EffectMulDurability.class, new MulDurabilityEffectData());
    
    return result;
  }
  
  @Override
  public int[] getDropValues() {
    return new int[]{
        1, 
        1, 
        40, 
        50, 
        60
        };
  }

  @Override
  public String[] getDisplayTexts() {
    return new String[]{
        "of Brittleness", 
        "of Fragility", 
        "of Sturdiness", 
        "of Craftsmanship", 
        "of Structure", 
        };
  }
  
  public class MulDurabilityEffectData extends FloatEffectLevelData {
    @Override
    public Class<? extends AbstractEffect> getEffectClass() {
      return EffectMulDurability.class;
    }
    
    @Override
    public Vector getNumericValueRangeForLevel(int level) {
      return getValueRangeLinear(new Vector(-0.04f, -0.19f), new Vector(0.2f), level);
    }
  }
  
}
