package com.philon.rpg.stat.presuf.presufs;

import java.util.LinkedHashMap;

import com.philon.engine.util.Vector;
import com.philon.rpg.stat.effect.EffectsObj.AbstractEffect;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddDamageReduce;
import com.philon.rpg.stat.presuf.AbstractSuffix;

public class AddDamageReduceSuffix extends AbstractSuffix {

  public AddDamageReduceSuffix(int newLevel) {
    super(newLevel);
  }

  @Override
  public LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> getEffectLevelData() {
    LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> result = 
        new LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData>();
    
    result.put(EffectAddDamageReduce.class, new AddDamageReduceEffectData());
    
    return result;
  }
  
  @Override
  public int[] getDropValues() {
    return new int[]{
        10, 
        20,
        30,
        40,
        50,
        60,
        70
        };
  }

  @Override
  public String[] getDisplayTexts() {
    return new String[]{
        "of Pain", 
        "of Tears", 
        "of Health", 
        "of Protection", 
        "of Absorption", 
        "of Deflection", 
        "of Osmosis"
        };
  }
  
  public class AddDamageReduceEffectData extends IntegerEffectLevelData {
    @Override
    public Class<? extends AbstractEffect> getEffectClass() {
      return EffectAddDamageReduce.class;
    }
    
    @Override
    public Vector getNumericValueRangeForLevel(int level) {
      return getValueRangeLinear(new Vector(1, 3), new Vector(3), level);
    }
  }
  
}
