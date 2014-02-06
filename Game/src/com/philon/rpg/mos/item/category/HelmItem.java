package com.philon.rpg.mos.item.category;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.stat.effect.EffectsObj;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddArmor;

public abstract class HelmItem extends AbstractItem {
  
  @Override
  public Vector getInvSize() {
    return new Vector(2, 2);
  }
  
  @Override
  public int getSouDrop() {
    return 37;
  }

  @Override
  public int getSouFlip() {
    return 24;
  }

  @Override
  public int getImgMap() {
    return 316;
  }
  
  @Override
  public EffectsObj getBaseEffects() {
    EffectsObj result = super.getBaseEffects();
    result.addOrCreateEffect( EffectAddArmor.class, getBaseArmor().getRandomIntValue() );
    return result;
  }
  
  public abstract Vector getBaseArmor();
}
