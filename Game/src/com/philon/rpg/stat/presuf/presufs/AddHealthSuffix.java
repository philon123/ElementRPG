package com.philon.rpg.stat.presuf.presufs;

import java.util.LinkedHashMap;

import com.philon.engine.util.Vector;
import com.philon.rpg.stat.effect.EffectsObj.AbstractEffect;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddMaxHealth;
import com.philon.rpg.stat.presuf.AbstractSuffix;

public class AddHealthSuffix extends AbstractSuffix {

  public AddHealthSuffix(int newLevel) {
    super(newLevel);
  }

  @Override
  public LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> getEffectLevelData() {
    LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> result = 
        new LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData>();
    
    result.put(EffectAddMaxHealth.class, new AddMaxHealthEffectData());
    
    return result;
  }
  
  @Override
  public int[] getDropValues() {
    return new int[]{
        1, 
        1, 
        10, 
        15, 
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
        "of the Vulture", 
        "of the Jackal", 
        "of the Fox", 
        "of the Jaguar", 
        "of the Eagle", 
        "of the Wolf", 
        "of the Tiger", 
        "of the Lion", 
        "of the Mammoth", 
        "of the Whale"
        };
  }
  
  public class AddMaxHealthEffectData extends IntegerEffectLevelData {
    @Override
    public Class<? extends AbstractEffect> getEffectClass() {
      return EffectAddMaxHealth.class;
    }
    
    @Override
    public Vector getNumericValueRangeForLevel(int level) {
      return getValueRangeLinear(new Vector(-40, -21), new Vector(20, 20), level);
    }
  }
  
}
