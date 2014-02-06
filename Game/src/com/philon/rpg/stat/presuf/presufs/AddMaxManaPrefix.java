package com.philon.rpg.stat.presuf.presufs;

import java.util.LinkedHashMap;

import com.philon.engine.util.Vector;
import com.philon.rpg.stat.effect.EffectsObj.AbstractEffect;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddMaxMana;
import com.philon.rpg.stat.presuf.AbstractPrefix;

public class AddMaxManaPrefix extends AbstractPrefix {

  public AddMaxManaPrefix(int newLevel) {
    super(newLevel);
  }

  @Override
  public LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> getEffectLevelData() {
    LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> result = 
        new LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData>();
    
    result.put(EffectAddMaxMana.class, new AddMaxManaEffectData());
    
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
        "Hyenas'", 
        "Frogs'", 
        "Spiders'", 
        "Ravens'", 
        "Snakes'", 
        "Serpents'", 
        "Drakes'", 
        "Dragons'", 
        "Worms'", 
        "Hydras'"
        };
  }
  
  public class AddMaxManaEffectData extends IntegerEffectLevelData {
    @Override
    public Class<? extends AbstractEffect> getEffectClass() {
      return EffectAddMaxMana.class;
    }
    
    @Override
    public Vector getNumericValueRangeForLevel(int level) {
      return getValueRangeLinear(new Vector(-40, -21), new Vector(20, 20), level);
    }
  }
  
}
