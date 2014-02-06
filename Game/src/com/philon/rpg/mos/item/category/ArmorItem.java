package com.philon.rpg.mos.item.category;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.stat.effect.EffectsObj;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddArmor;

public abstract class ArmorItem extends AbstractItem {
  public abstract Vector getBaseArmor();
  
  @Override
  public EffectsObj getBaseEffects() {
    EffectsObj result = super.getBaseEffects();
    result.addOrCreateEffect( EffectAddArmor.class, getBaseArmor().getRandomIntValue() );
    return result;
  }
}
