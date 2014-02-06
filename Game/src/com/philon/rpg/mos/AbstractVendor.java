package com.philon.rpg.mos;

import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.forms.AbstractVendorForm;
import com.philon.rpg.mo.Selectable;
import com.philon.rpg.mo.UpdateMapObj;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.player.inventory.AbstractItemGrid;

public abstract class AbstractVendor extends UpdateMapObj implements Selectable {
  public ItemGrid itemGrid;
  public AbstractVendorForm form;
  
  public AbstractVendor() {
    super();
    
    itemGrid = new ItemGrid();
    addItems();
  }
  
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
  public void interactTrigger(UpdateMapObj objInteracting) {
    form.toggle();
  }
  
  public void addItems() {
    AbstractItem newItem = null;
    do {
      newItem = createItem();
    } while (itemGrid.addAuto(newItem));
  }
  
  public abstract AbstractItem createItem();
  
  public void buyItem(AbstractItem newItem) {
    RpgGame.inst.localPlayer.inv.pickupGold((int)newItem.dropValue);
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
