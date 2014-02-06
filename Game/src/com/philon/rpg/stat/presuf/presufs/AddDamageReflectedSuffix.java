package com.philon.rpg.stat.presuf.presufs;

import java.util.LinkedHashMap;

import com.philon.engine.util.Vector;
import com.philon.rpg.stat.effect.EffectsObj.AbstractEffect;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddDamageReflected;
import com.philon.rpg.stat.presuf.AbstractSuffix;

public class AddDamageReflectedSuffix extends AbstractSuffix {

  public AddDamageReflectedSuffix(int newLevel) {
    super(newLevel);
  }

  @Override
  public LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> getEffectLevelData() {
    LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> result = 
        new LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData>();
    
    result.put(EffectAddDamageReflected.class, new AddDamageReflectedEffectData());
    
    return result;
  }
  
  @Override
  public int[] getDropValues() {
    return new int[]{
        20, 
        30, 
        40, 
        50, 
        60
        };
  }

  @Override
  public String[] getDisplayTexts() {
    return new String[]{
        "of Roughness", 
        "of Thorns", 
        "of Spikes", 
        "of Stakes", 
        "of Impaling"
        };
  }
  
  public class AddDamageReflectedEffectData extends FloatEffectLevelData {
    @Override
    public Class<? extends AbstractEffect> getEffectClass() {
      return EffectAddDamageReflected.class;
    }
    
    @Override
    public Vector getNumericValueRangeForLevel(int level) {
      return getValueRangeLinear(new Vector(0.05f, 0.1f), new Vector(0.1f), level);
    }
  }
  
}
