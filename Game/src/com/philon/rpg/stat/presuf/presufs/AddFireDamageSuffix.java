package com.philon.rpg.stat.presuf.presufs;

import java.util.LinkedHashMap;

import com.philon.engine.util.Vector;
import com.philon.rpg.stat.effect.EffectsObj.AbstractEffect;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddFireDamage;
import com.philon.rpg.stat.presuf.AbstractSuffix;

public class AddFireDamageSuffix extends AbstractSuffix {

  public AddFireDamageSuffix(int newLevel) {
    super(newLevel);
  }

  @Override
  public LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> getEffectLevelData() {
    LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData> result = 
        new LinkedHashMap<Class<? extends AbstractEffect>, AbstractEffectLevelData>();
    
    result.put(EffectAddFireDamage.class, new AddFireDamageEffectData());
    
    return result;
  }
  
  @Override
  public int[] getDropValues() {
    return new int[]{
        40, 
        50, 
        60, 
        70
        };
  }

  @Override
  public String[] getDisplayTexts() {
    return new String[]{
        "of the Flame", 
        "of Burning", 
        "of Fire", 
        "of Inferno" 
        };
  }
  
  public class AddFireDamageEffectData extends VectorEffectLevelData {
    @Override
    public Class<? extends AbstractEffect> getEffectClass() {
      return EffectAddFireDamage.class;
    }
    
    @Override
    public Vector[] getVectorValueRangeForLevel(int level) {
      return getVectorValueRangeLinear(
          new Vector[]{new Vector(0,2), new Vector(3,5)}, 
          new Vector[]{new Vector(3), new Vector(4)}, 
          level
          );
    }
  }
  
}
