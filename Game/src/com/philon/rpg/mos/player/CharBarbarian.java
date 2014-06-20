package com.philon.rpg.mos.player;

import com.philon.rpg.mos.item.ItemData;
import com.philon.rpg.mos.item.items.ItemShortSword;
import com.philon.rpg.mos.player.inventory.Inventory.Equip;
import com.philon.rpg.stat.effect.EffectsObj;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddAttackRate;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddCastRate;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddDexterity;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddLifePerVit;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddMagic;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddManaPerMag;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddStrength;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddVitality;

public class CharBarbarian extends AbstractChar {

  public CharBarbarian() {
    super();

    inv.pickupItem( ItemData.createItem(ItemShortSword.class) );
    inv.addPickupToEquip( Equip.INV_WEAPON );
  }

  @Override
  public String getCharText() {
    return "Barbarian";
  }

  @Override
  public EffectsObj getBaseEffects() {
    EffectsObj result = new EffectsObj();
    result.addOrCreateEffect( EffectAddStrength.class, 25 );
    result.addOrCreateEffect( EffectAddDexterity.class, 15 );
    result.addOrCreateEffect( EffectAddVitality.class, 20 );
    result.addOrCreateEffect( EffectAddMagic.class, 0 );
    result.addOrCreateEffect( EffectAddLifePerVit.class, 4f );
    result.addOrCreateEffect( EffectAddManaPerMag.class, 0f );
    result.addOrCreateEffect( EffectAddAttackRate.class, 3f );
    result.addOrCreateEffect( EffectAddCastRate.class, 3f );
    return result;
  }

  @Override
  public int getImgIdle() {
    return 300;
  }

  @Override
  public int getImgMoving() {
    return 302;
  }

  @Override
  public int getImgCasting() {
    return 300;
  }

  @Override
  public int getImgHit() {
    return 326;
  }

  @Override
  public int getImgDying() {
    return 303;
  }

  @Override
  public int getSouAttack() {
    return 0;
  }

  @Override
  public int getSouHit() {
    return 8;
  }

  @Override
  public int getSouDie() {
    return 9;
  }

  @Override
  public int getSouNoMana() {
    return 49;
  }

}
