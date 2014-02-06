package com.philon.rpg.stat.presuf.presufs;

import java.util.LinkedHashMap;

import com.philon.engine.util.Vector;
import com.philon.rpg.stat.effect.EffectsObj.AbstractEffect;
import com.philon.rpg.stat.effect.EffectsObj.EffectMulArmor;
import com.philon.rpg.stat.presuf.AbstractPrefix;

public class MulArmorPrefix extends AbstractPrefix {

  public MulArmorPrefix(int newLevel) {
    super(newLevel);
  }

  @Override
  public LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> getEffectLevelData() {
    LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> result = 
        new LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData>();
    
    result.put(EffectMulArmor.class, new MulArmorEffectData());
    
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
        90, 
        100
        };
  }

  @Override
  public String[] getDisplayTexts() {
    return new String[]{
        "Vulnerable", 
        "Rusted", 
        "Fine", 
        "String", 
        "Grand", 
        "Valiant", 
        "Glorious", 
        "Blessed", 
        "Saintly", 
        "Awesome", 
        "Holy", 
        "Godly"
        };
  }
  

  
  public class MulArmorEffectData extends FloatEffectLevelData {
    @Override
    public Class<? extends AbstractEffect> getEffectClass() {
      return EffectMulArmor.class;
    }
    
    @Override
    public Vector getNumericValueRangeForLevel(int level) {
      return getValueRangeLinear(new Vector(-0.4f, -0.21f), new Vector(0.2f), level);
    }
  }
  
}
