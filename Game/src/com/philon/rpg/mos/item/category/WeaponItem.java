package com.philon.rpg.mos.item.category;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.stat.effect.EffectsObj;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddNormalDamage;
import com.philon.rpg.stat.effect.EffectsObj.EffectSetDefaultSpell;

public abstract class WeaponItem extends AbstractItem {
  public abstract boolean isTwoHanded();
  public abstract Vector getBaseDamage();
  public abstract int getDefaultSpellType();
  
  @Override
  public EffectsObj getBaseEffects() {
    EffectsObj result = super.getBaseEffects();
    result.addOrCreateEffect( EffectAddNormalDamage.class, getBaseDamage() );
    result.addOrCreateEffect( EffectSetDefaultSpell.class, getDefaultSpellType() );
    return result;
  }
}
