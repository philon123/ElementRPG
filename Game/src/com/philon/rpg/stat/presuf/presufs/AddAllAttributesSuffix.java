package com.philon.rpg.stat.presuf.presufs;

import java.util.LinkedHashMap;

import com.philon.engine.util.Vector;
import com.philon.rpg.stat.effect.EffectsObj.AbstractEffect;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddAllAttributes;
import com.philon.rpg.stat.presuf.AbstractSuffix;

public class AddAllAttributesSuffix extends AbstractSuffix {

  public AddAllAttributesSuffix(int newLevel) {
    super(newLevel);
  }

  @Override
  public LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> getEffectLevelData() {
    LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> result = 
        new LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData>();
    
    result.put(EffectAddAllAttributes.class, new AddAllAttributesEffectData());
    
    return result;
  }
  
  @Override
  public int[] getDropValues() {
    return new int[]{
        1, 
        1, 
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
        "of Trouble", 
        "of the Pit", 
        "of the Sky", 
        "of the Moon", 
        "of the Stars", 
        "of the Heavens", 
        "of the Zodiac"
        };
  }
  
  public class AddAllAttributesEffectData extends IntegerEffectLevelData {
    @Override
    public Class<? extends AbstractEffect> getEffectClass() {
      return EffectAddAllAttributes.class;
    }

    @Override
    public Vector getNumericValueRangeForLevel(int level) {
      return getValueRangeLinear(new Vector(-10, -6), new Vector(5, 5), level);
    }
  }
  
}
