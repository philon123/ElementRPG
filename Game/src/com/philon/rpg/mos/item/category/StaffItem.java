package com.philon.rpg.mos.item.category;

import com.philon.rpg.spell.SpellData;


public abstract class StaffItem extends WeaponItem {
  
  @Override
  public int getSouDrop() {
    return 44;
  }

  @Override
  public int getSouFlip() {
    return 32;
  }

  @Override
  public int getImgMap() {
    return 321;
  }

  @Override
  public boolean isTwoHanded() {
    return true;
  }
  
  @Override
  public int getDefaultSpellType() {
    return SpellData.MELEE;
  }
  
}
