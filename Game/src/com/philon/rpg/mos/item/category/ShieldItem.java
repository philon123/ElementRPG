package com.philon.rpg.mos.item.category;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.stat.effect.EffectsObj;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddArmor;

public abstract class ShieldItem extends AbstractItem {
  
  @Override
  public int getSouDrop() {
    return 43;
  }
  
  @Override
  public int getSouFlip() {
    return 31;
  }
  
  @Override
  public int getImgMap() {
    return 320;
  }
  
  @Override
  public EffectsObj getBaseEffects() {
    EffectsObj result = super.getBaseEffects();
    result.addOrCreateEffect( EffectAddArmor.class, getBaseArmor().getRandomIntValue() );
    return result;
  }
  
  public abstract Vector getBaseArmor();
}
