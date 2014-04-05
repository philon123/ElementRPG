package com.philon.rpg.mos.player;

import com.philon.rpg.mos.item.ItemData;
import com.philon.rpg.mos.item.items.ItemShortBow;
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

public class CharAmazon extends AbstractChar {

  public CharAmazon() {
    super();

    inv.pickupItem( ItemData.createItem(ItemShortBow.class) );
    inv.addPickupToEquip( Equip.INV_WEAPON );
  }

  @Override
  public String getCharText() {
    return "Amazon";
  }

  @Override
  public EffectsObj getBaseEffects() {
    EffectsObj result = new EffectsObj();
    result.addOrCreateEffect( EffectAddStrength.class, 15 );
    result.addOrCreateEffect( EffectAddDexterity.class, 25 );
    result.addOrCreateEffect( EffectAddVitality.class, 15 );
    result.addOrCreateEffect( EffectAddMagic.class, 5 );
    result.addOrCreateEffect( EffectAddLifePerVit.class, 3f );
    result.addOrCreateEffect( EffectAddManaPerMag.class, 2f );
    result.addOrCreateEffect( EffectAddAttackRate.class, 3f );
    result.addOrCreateEffect( EffectAddCastRate.class, 3f );
    return result;
  }

  @Override
  public int getNumSkills() {
    return 10;
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
    return 10;
  }

  @Override
  public int getSouDie() {
    return 11;
  }

  @Override
  public int getSouNoMana() {
    return 47;
  }

}
