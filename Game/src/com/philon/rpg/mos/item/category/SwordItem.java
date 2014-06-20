package com.philon.rpg.mos.item.category;

import com.philon.rpg.spell.AbstractSpell;
import com.philon.rpg.spell.SpellMelee;


public abstract class SwordItem extends WeaponItem {

  @Override
  public int getSouDrop() {
    return 45;
  }

  @Override
  public int getSouFlip() {
    return 33;
  }

  @Override
  public int getImgMap() {
    return 322;
  }

  @Override
  public Class<? extends AbstractSpell> getDefaultSpellType() {
    return SpellMelee.class;
  }

}
