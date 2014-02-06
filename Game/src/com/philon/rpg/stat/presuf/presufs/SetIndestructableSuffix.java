package com.philon.rpg.stat.presuf.presufs;

import java.util.LinkedHashMap;

import com.philon.rpg.stat.effect.EffectsObj.AbstractEffect;
import com.philon.rpg.stat.effect.EffectsObj.EffectSetIndestructable;
import com.philon.rpg.stat.presuf.AbstractSuffix;

public class SetIndestructableSuffix extends AbstractSuffix {

  public SetIndestructableSuffix(int newLevel) {
    super(newLevel);
  }

  @Override
  public LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> getEffectLevelData() {
    LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> result = 
        new LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData>();
    
    result.put(EffectSetIndestructable.class, new SetIndestructableEffectData());
    
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
        "of the Ages"
        };
  }
  
  public class SetIndestructableEffectData extends BooleanEffectLevelData {
    @Override
    public Class<? extends AbstractEffect> getEffectClass() {
      return EffectSetIndestructable.class;
    }
    
    @Override
    public boolean getBooleanValueForLevel(int level) {
      return true;
    }
  }
  
}
