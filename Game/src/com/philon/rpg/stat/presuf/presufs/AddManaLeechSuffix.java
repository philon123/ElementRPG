package com.philon.rpg.stat.presuf.presufs;

import java.util.LinkedHashMap;

import com.philon.engine.util.Vector;
import com.philon.rpg.stat.effect.EffectsObj.AbstractEffect;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddManaLeech;
import com.philon.rpg.stat.presuf.AbstractSuffix;

public class AddManaLeechSuffix extends AbstractSuffix {

  public AddManaLeechSuffix(int newLevel) {
    super(newLevel);
  }

  @Override
  public LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> getEffectLevelData() {
    LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> result = 
        new LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData>();
    
    result.put(EffectAddManaLeech.class, new ToManaLeechEffectData());
    
    return result;
  }
  
  @Override
  public int[] getDropValues() {
    return new int[]{
        30, 
        70
        };
  }

  @Override
  public String[] getDisplayTexts() {
    return new String[]{
        "of the Bat", 
        "of the Vampire"
        };
  }
  
  public class ToManaLeechEffectData extends FloatEffectLevelData {
    @Override
    public Class<? extends AbstractEffect> getEffectClass() {
      return EffectAddManaLeech.class;
    }
    
    @Override
    public Vector getNumericValueRangeForLevel(int level) {
      return getValueRangeLinear(new Vector(0.05f), new Vector(0.05f), level);
    }
  }
  
}
