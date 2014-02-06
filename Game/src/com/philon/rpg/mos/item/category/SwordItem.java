package com.philon.rpg.mos.item.category;

import com.philon.rpg.spell.SpellData;


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
  public int getDefaultSpellType() {
    return SpellData.MELEE;
  }
  
}
