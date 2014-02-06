package com.philon.rpg.mos.item.category;

import com.philon.rpg.spell.SpellData;


public abstract class BowItem extends WeaponItem {
  
  @Override
  public int getSouDrop() {
    return 36;
  }

  @Override
  public int getSouFlip() {
    return 25;
  }

  @Override
  public int getImgMap() {
    return 311;
  }
  
  @Override
  public boolean isTwoHanded() {
    return true;
  }
  
  @Override
  public int getDefaultSpellType() {
    return SpellData.ARROW;
  }
  
}