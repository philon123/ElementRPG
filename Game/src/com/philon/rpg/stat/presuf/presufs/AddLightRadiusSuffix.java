package com.philon.rpg.stat.presuf.presufs;

import java.util.LinkedHashMap;

import com.philon.engine.util.Vector;
import com.philon.rpg.stat.effect.EffectsObj.AbstractEffect;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddLightRadius;
import com.philon.rpg.stat.presuf.AbstractSuffix;

public class AddLightRadiusSuffix extends AbstractSuffix {

  public AddLightRadiusSuffix(int newLevel) {
    super(newLevel);
  }

  @Override
  public LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> getEffectLevelData() {
    LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> result = 
        new LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData>();
    
    result.put(EffectAddLightRadius.class, new MulLightRadiusEffectData());
    
    return result;
  }
  
  @Override
  public int[] getDropValues() {
    return new int[]{
        1, 
        1, 
        20, 
        30
        };
  }

  @Override
  public String[] getDisplayTexts() {
    return new String[]{
        "of the Dark", 
        "of the Night", 
        "of Light", 
        "of the Radiance"
        };
  }
  
  public class MulLightRadiusEffectData extends FloatEffectLevelData {
    @Override
    public Class<? extends AbstractEffect> getEffectClass() {
      return EffectAddLightRadius.class;
    }
    
    @Override
    public Vector getNumericValueRangeForLevel(int level) {
      return getValueRangeLinear(new Vector(-0.4f, -0.19f), new Vector(0.2f), level);
    }
  }
  
}
