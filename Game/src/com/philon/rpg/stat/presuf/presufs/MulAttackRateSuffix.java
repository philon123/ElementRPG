package com.philon.rpg.stat.presuf.presufs;

import java.util.LinkedHashMap;

import com.philon.engine.util.Vector;
import com.philon.rpg.stat.effect.EffectsObj.AbstractEffect;
import com.philon.rpg.stat.effect.EffectsObj.EffectMulAttackRate;
import com.philon.rpg.stat.presuf.AbstractSuffix;

public class MulAttackRateSuffix extends AbstractSuffix {

  public MulAttackRateSuffix(int newLevel) {
    super(newLevel);
  }

  @Override
  public LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> getEffectLevelData() {
    LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> result = 
        new LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData>();
    
    result.put(EffectMulAttackRate.class, new MulAttackRateEffectData());
    
    return result;
  }
  
  @Override
  public int[] getDropValues() {
    return new int[]{
        40, 
        50, 
        60, 
        70
        };
  }

  @Override
  public String[] getDisplayTexts() {
    return new String[]{
        "of Readiness", 
        "of Swiftness", 
        "of Speed", 
        "of Haste"
        };
  }
  
  public class MulAttackRateEffectData extends FloatEffectLevelData {
    @Override
    public Class<? extends AbstractEffect> getEffectClass() {
      return EffectMulAttackRate.class;
    }
    
    @Override
    public Vector getNumericValueRangeForLevel(int level) {
      return getValueRangeLinear(new Vector(0.1f, 0.19f), new Vector(0.1f), level);
    }
  }
  
}
