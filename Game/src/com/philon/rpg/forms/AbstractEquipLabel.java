package com.philon.rpg.forms;

import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.mos.item.AbstractItem;

public abstract class AbstractEquipLabel extends AbstractItemDropLabel {
  public int equipSlotNr;

  @Override
  public void draw(Vector containerPos, Vector containerSize) {
    super.draw(containerPos, containerSize);

    AbstractItem currItem = RpgGame.inst.localPlayer.inv.equip.getBySlot(equipSlotNr);
    if( !(currItem==null) ) {
      Vector tmpPos = Vector.mul( pos, containerSize ).addInst(containerPos);
      tmpPos.addInst( Vector.sub( size, currItem.invSize.copy().mulInst(getCellSize()) ).mulScalarInst(0.5f) );
      drawInvItem( currItem, tmpPos );
    }
  }

  @Override
  public void dropPickupToPixel(Vector newPixel) {
    RpgGame.inst.localPlayer.inv.addPickupToEquip(equipSlotNr);
  }

  @Override
  public AbstractItem getItemByPixel(Vector newPixel) {
    return getItem();
  }

  public AbstractItem getItem() {
    return RpgGame.inst.localPlayer.inv.equip.getBySlot(equipSlotNr);
  }

}