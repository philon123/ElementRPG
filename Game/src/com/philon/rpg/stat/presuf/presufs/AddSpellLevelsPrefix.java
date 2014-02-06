package com.philon.rpg.stat.presuf.presufs;

import java.util.LinkedHashMap;

import com.philon.engine.util.Vector;
import com.philon.rpg.stat.effect.EffectsObj.AbstractEffect;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddSpellLevels;
import com.philon.rpg.stat.presuf.AbstractPrefix;

public class AddSpellLevelsPrefix extends AbstractPrefix {

  public AddSpellLevelsPrefix(int newLevel) {
    super(newLevel);
  }

  @Override
  public LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> getEffectLevelData() {
    LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> result = 
        new LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData>();
    
    result.put(EffectAddSpellLevels.class, new AddSpellLevelsEffectData());
    
    return result;
  }
  
  @Override
  public int[] getDropValues() {
    return new int[]{
        50, 
        70
        };
  }

  @Override
  public String[] getDisplayTexts() {
    return new String[]{
        "Angels",
        "Arch-Angels"
        };
  }
  
  public class AddSpellLevelsEffectData extends IntegerEffectLevelData {
    @Override
    public Class<? extends AbstractEffect> getEffectClass() {
      return EffectAddSpellLevels.class;
    }
    
    @Override
    public Vector getNumericValueRangeForLevel(int level) {
      return getValueRangeLinear(new Vector(1), new Vector(1), level);
    }
  }
  
}
