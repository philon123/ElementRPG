package com.philon.rpg.stat.presuf.presufs;

import java.util.LinkedHashMap;

import com.philon.engine.util.Vector;
import com.philon.rpg.stat.effect.EffectsObj.AbstractEffect;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddResistLightning;
import com.philon.rpg.stat.presuf.AbstractPrefix;

public class AddResistLightningPrefix extends AbstractPrefix {

  public AddResistLightningPrefix(int newLevel) {
    super(newLevel);
  }

  @Override
  public LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> getEffectLevelData() {
    LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> result = 
        new LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData>();
    
    result.put(EffectAddResistLightning.class, new MulResistLightningEffectData());
    
    return result;
  }
  
  @Override
  public int[] getDropValues() {
    return new int[]{
        10, 
        25, 
        40, 
        55, 
        70
        };
  }

  @Override
  public String[] getDisplayTexts() {
    return new String[]{
        "Blue",
        "Azure", 
        "Lapis",
        "Cobalt",
        "Sapphire"
        };
  }
  

  
  public class MulResistLightningEffectData extends FloatEffectLevelData {
    @Override
    public Class<? extends AbstractEffect> getEffectClass() {
      return EffectAddResistLightning.class;
    }
    
    @Override
    public Vector getNumericValueRangeForLevel(int level) {
      return getValueRangeLinear(new Vector(0.0f, 0.5f), new Vector(0.05f), level);
    }
  }
  
}
