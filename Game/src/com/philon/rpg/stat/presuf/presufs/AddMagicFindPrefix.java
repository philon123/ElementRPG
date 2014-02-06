package com.philon.rpg.stat.presuf.presufs;

import java.util.LinkedHashMap;

import com.philon.engine.util.Vector;
import com.philon.rpg.stat.effect.EffectsObj.AbstractEffect;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddMagicFind;
import com.philon.rpg.stat.presuf.AbstractPrefix;

public class AddMagicFindPrefix extends AbstractPrefix {

  public AddMagicFindPrefix(int newLevel) {
    super(newLevel);
  }

  @Override
  public LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> getEffectLevelData() {
    LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> result = 
        new LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData>();
    
    result.put(EffectAddMagicFind.class, new MulMagicFindEffectData());
    
    return result;
  }
  
  @Override
  public int[] getDropValues() {
    return new int[]{
        1, 
        1, 
        10, 
        20, 
        30, 
        40, 
        50, 
        60, 
        70, 
        80, 
        90
        };
  }

  @Override
  public String[] getDisplayTexts() {
    return new String[]{
        "Tin", 
        "Brass", 
        "Bronze", 
        "Iron", 
        "Steel", 
        "Silver", 
        "Gold", 
        "Platinum", 
        "Mithril", 
        "Meteoric", 
        "Wierd"
        };
  }
  
  public class MulMagicFindEffectData extends FloatEffectLevelData {
    @Override
    public Class<? extends AbstractEffect> getEffectClass() {
      return EffectAddMagicFind.class;
    }
    
    @Override
    public Vector getNumericValueRangeForLevel(int level) {
      return getValueRangeLinear(new Vector(-0.4f, -0.21f), new Vector(0.2f), level);
    }
  }
  
}
