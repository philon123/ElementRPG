package com.philon.rpg.forms;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.philon.engine.Data;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.mos.item.AbstractItem;

public abstract class AbstractEquipLabel extends AbstractItemGridLabel {
  public int equipSlotNr;

  @Override
  public void execDraw(SpriteBatch batch) {
    int tmpImage = getConfiguredBackground();
    if(tmpImage!=0) {
      batch.draw(Data.textures.get(tmpImage).frames[0], absPos.x, absPos.y, absSize.x, absSize.y);
    }

    AbstractItem it = getItem();
    if( !(it==null) ) {
      Vector itemCellPos = Vector.mulScalar( Vector.sub(getConfiguredGridSize(), it.getInvSize()), 0.5f );
      drawInvItem(batch, it, itemCellPos);
    }
  }

  @Override
  public void dropPickupToCell(Vector newPixel) {
    RpgGame.inst.getExclusiveUser().character.inv.addPickupToEquip(equipSlotNr);
  }

  @Override
  public AbstractItem getItemByCell(Vector newCell) {
    return getItem();
  }

  public AbstractItem getItem() {
    return RpgGame.inst.getExclusiveUser().character.inv.equip.getBySlot(equipSlotNr);
  }

  @Override
  protected List<AbstractItem> getItemsForDraw() {
    LinkedList<AbstractItem> result = new LinkedList<AbstractItem>();
    result.add(getItem());
    return result;
  }

}