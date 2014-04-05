package com.philon.rpg.mos;

import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.forms.AbstractVendorForm;
import com.philon.rpg.forms.InventoryForm;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.map.mo.UpdateMapObj;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.player.inventory.AbstractItemGrid;

public abstract class AbstractVendor extends UpdateMapObj {
  public ItemGrid itemGrid;

  public AbstractVendor() {
    super();

    itemGrid = new ItemGrid();
    addItems();
  }

  protected abstract Class<? extends AbstractVendorForm> getFormClass();

  @Override
  public int getImgIdle() {
    return 55;
  }

  @Override
  public Vector getCollRect() {
    return new Vector(0.3f);
  }

  @Override
  public int getImgDying() {
    return 0;
  }

  @Override
  public int getSouDie() {
    return 0;
  }

  @Override
  public float getTilesPerSecond() {
    return 0;
  }

  @Override
  public int getImgMoving() {
    return 0;
  }

  @Override
  public void interactTrigger(RpgMapObj objInteracting) {
    AbstractVendorForm newForm = Util.instantiateClass(getFormClass());
    newForm.setVendor(this);
    RpgGame.inst.startExclusiveSession(RpgGame.inst.activeUser, newForm, new InventoryForm());
  }

  public void addItems() {
    AbstractItem newItem = null;
    do {
      newItem = createItem();
    } while (itemGrid.addAuto(newItem));
  }

  public abstract AbstractItem createItem();

  public void buyItem(AbstractItem newItem) {
    RpgGame.inst.getExclusiveUser().character.inv.pickupGold((int)newItem.getDropValue());
    itemGrid.addAuto(newItem);
  }

  public AbstractItem getItemByCell(Vector cell) {
    return itemGrid.getItemByCell(cell);
  }

  public class ItemGrid extends AbstractItemGrid {
    @Override
    public Vector getGridSize() {
      return new Vector(10, 10);
    }
  }

}
