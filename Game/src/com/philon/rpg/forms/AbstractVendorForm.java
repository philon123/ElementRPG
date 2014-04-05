package com.philon.rpg.forms;

import java.util.List;

import com.philon.engine.forms.GuiElement;
import com.philon.engine.util.Util.Order;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.RpgUser;
import com.philon.rpg.mos.AbstractVendor;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.player.inventory.Inventory;

@Order(10)
public abstract class AbstractVendorForm extends GuiElement {
  public AbstractVendor currVendor;

  @Override
  protected boolean isStrechable() {
    return false;
  }
  @Override
  protected float getConfiguredXYRatio() {
    return 1;
  }
  @Override
  protected int getConfiguredAlignment() {
    return GuiElement.ALIGN_TOP_LEFT;
  }
  @Override
  protected float getConfiguredScale() {
    return 0.9f;
  }
  @Override
  protected int getConfiguredBackground() {
    return 229;
  }

  public void setVendor(AbstractVendor newVendor) {
    currVendor = newVendor;
  }

  public class VendorGridLabel extends AbstractItemGridLabel {
    @Override
    protected float getConfiguredScale() {
      return 0.9f;
    }
    @Override
    protected Vector getConfiguredGridSize() {
      return new Vector(10, 10);
    }
    @Override
    protected int getConfiguredBackground() {
      return 242;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.05f);
    }
    @Override
    public AbstractItem getItemByCell(Vector newCell) {
      return currVendor.itemGrid.getItemByCell(newCell);
    }
    @Override
    public List<AbstractItem> getItemsForDraw() {
      return currVendor.itemGrid.itemList;
    }
    public void dropPickupToCell(Vector newCell) {
      RpgUser exclusiveUser = RpgGame.inst.getExclusiveUser();
      currVendor.buyItem(exclusiveUser.character.inv.pickedUpItem);
      exclusiveUser.character.inv.pickedUpItem = null;
    }
    @Override
    public void handleClickItemLeft(AbstractItem clickedItem) {
      //don't do anyting
    }
    @Override
    public void handleClickItemRight(AbstractItem clickedItem) {
      Inventory inv = RpgGame.inst.getExclusiveUser().character.inv;

      if (inv.currGold>=clickedItem.getDropValue()) {
        inv.currGold -= clickedItem.getDropValue();
        if (inv.pickedUpItem==null) {
          currVendor.itemGrid.remove(clickedItem);
          inv.pickupItem(clickedItem);
        }
      }
    }
  }

}
