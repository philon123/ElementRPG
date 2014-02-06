package com.philon.rpg.stat.presuf.presufs;

import java.util.LinkedHashMap;

import com.philon.engine.util.Vector;
import com.philon.rpg.stat.effect.EffectsObj.AbstractEffect;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddMagic;
import com.philon.rpg.stat.presuf.AbstractSuffix;

public class AddMagicSuffix extends AbstractSuffix {

  public AddMagicSuffix(int newLevel) {
    super(newLevel);
  }

  @Override
  public LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> getEffectLevelData() {
    LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> result = 
        new LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData>();
    
    result.put(EffectAddMagic.class, new AddMagicEffectData());
    
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
        "of the Fool", 
        "of Dyslexia", 
        "of Magic", 
        "of the Mind", 
        "of Brilliance", 
        "of Sorcery", 
        "of Wizardry"
        };
  }
  
  public class AddMagicEffectData extends IntegerEffectLevelData {
    @Override
    public Class<? extends AbstractEffect> getEffectClass() {
      return EffectAddMagic.class;
    }
    
    @Override
    public Vector getNumericValueRangeForLevel(int level) {
      return getValueRangeLinear(new Vector(-10, -6), new Vector(5, 5), level);
    }
  }
  
}
