package com.philon.rpg.stat.presuf.presufs;

import java.util.LinkedHashMap;

import com.philon.rpg.stat.effect.EffectsObj.AbstractEffect;
import com.philon.rpg.stat.effect.EffectsObj.EffectSetIgnoreArmor;
import com.philon.rpg.stat.presuf.AbstractSuffix;

public class SetIgnoreArmorSuffix extends AbstractSuffix {

  public SetIgnoreArmorSuffix(int newLevel) {
    super(newLevel);
  }

  @Override
  public LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> getEffectLevelData() {
    LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> result = 
        new LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData>();
    
    result.put(EffectSetIgnoreArmor.class, new SetIgnoreArmorEffectData());
    
    return result;
  }
  
  @Override
  public int[] getDropValues() {
    return new int[]{
        70
        };
  }

  @Override
  public String[] getDisplayTexts() {
    return new String[]{
        "of Bashing"
        };
  }
  
  public class SetIgnoreArmorEffectData extends BooleanEffectLevelData {
    @Override
    public Class<? extends AbstractEffect> getEffectClass() {
      return EffectSetIgnoreArmor.class;
    }
    
    @Override
    public boolean getBooleanValueForLevel(int level) {
      return true;
    }
  }
  
}
