package com.philon.rpg.mos.item.category;

import com.philon.rpg.spell.AbstractSpell;
import com.philon.rpg.spell.SpellMelee;


public abstract class ClubItem extends WeaponItem {

  @Override
  public int getSouDrop() {
    return 34;
  }

  @Override
  public int getSouFlip() {
    return 22;
  }

  @Override
  public int getImgMap() {
    return 312;
  }

  @Override
  public Class<? extends AbstractSpell> getDefaultSpellType() {
    return SpellMelee.class;
  }

}
