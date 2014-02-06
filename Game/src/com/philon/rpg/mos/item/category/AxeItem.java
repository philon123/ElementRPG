package com.philon.rpg.mos.item.category;

import com.philon.rpg.spell.SpellData;


public abstract class AxeItem extends WeaponItem {
  
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
    return 309;
  }
  
  @Override
  public int getDefaultSpellType() {
    return SpellData.MELEE;
  }
  
}
