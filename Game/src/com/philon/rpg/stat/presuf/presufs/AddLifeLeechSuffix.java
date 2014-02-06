package com.philon.rpg.stat.presuf.presufs;

import java.util.LinkedHashMap;

import com.philon.engine.util.Vector;
import com.philon.rpg.stat.effect.EffectsObj.AbstractEffect;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddLifeLeech;
import com.philon.rpg.stat.presuf.AbstractSuffix;

public class AddLifeLeechSuffix extends AbstractSuffix {

  public AddLifeLeechSuffix(int newLevel) {
    super(newLevel);
  }

  @Override
  public LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> getEffectLevelData() {
    LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> result = 
        new LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData>();
    
    result.put(EffectAddLifeLeech.class, new ToLifeLeechEffectData());
    
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
        "of the Leech", 
        "of the Blood"
        };
  }
  
  public class ToLifeLeechEffectData extends FloatEffectLevelData {
    @Override
    public Class<? extends AbstractEffect> getEffectClass() {
      return EffectAddLifeLeech.class;
    }
    
    @Override
    public Vector getNumericValueRangeForLevel(int level) {
      return getValueRangeLinear(new Vector(0.05f), new Vector(0.05f), level);
    }
  }
  
}
