package com.philon.rpg.stat.presuf.presufs;

import java.util.LinkedHashMap;

import com.philon.engine.util.Vector;
import com.philon.rpg.stat.effect.EffectsObj.AbstractEffect;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddDexterity;
import com.philon.rpg.stat.presuf.AbstractSuffix;

public class AddDexteritySuffix extends AbstractSuffix {

  public AddDexteritySuffix(int newLevel) {
    super(newLevel);
  }

  @Override
  public LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> getEffectLevelData() {
    LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> result = 
        new LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData>();
    
    result.put(EffectAddDexterity.class, new ToDexterityEffectData());
    
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
        "of Paralysis", 
        "of Atrophy", 
        "of Dexterity", 
        "of Skill", 
        "of Accuracy", 
        "of Precision", 
        "of Perfection"
        };
  }
  
  public class ToDexterityEffectData extends IntegerEffectLevelData {
    @Override
    public Class<? extends AbstractEffect> getEffectClass() {
      return EffectAddDexterity.class;
    }
    
    @Override
    public Vector getNumericValueRangeForLevel(int level) {
      return getValueRangeLinear(new Vector(-10, -6), new Vector(5, 5), level);
    }
  }
  
}
