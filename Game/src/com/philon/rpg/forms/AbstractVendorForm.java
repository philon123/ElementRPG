package com.philon.rpg.forms;

import java.util.List;

import com.philon.engine.PhilonGame;
import com.philon.engine.forms.AbstractButton;
import com.philon.engine.forms.AbstractForm;
import com.philon.engine.forms.AbstractLabel;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.mos.AbstractVendor;
import com.philon.rpg.mos.item.AbstractItem;

public abstract class AbstractVendorForm extends AbstractForm {
  public AbstractVendor currVendor;

  @Override
  public Vector getPosByScreenSize(Vector newScreenSize) {
    return new Vector();
  }

  @Override
  public Vector getSizeByScreenSize(Vector newScreenSize) {
    return new Vector(700, 700);
  }

  public void setVendor(AbstractVendor newVendor) {
    currVendor = newVendor;
  }

  public class VendorGridLabel extends AbstractItemGridLabel {

    public VendorGridLabel() {
      pos = new Vector(0.05f, 0.05f) ;
      size = new Vector(630, 630) ;
      img = 242;
      displayText="";

      gridSize = new Vector(10, 10);
      cellSize = Vector.div(size, gridSize);
    }

    @Override
    public AbstractItem getItemByCell(Vector newCell) {
      return currVendor.itemGrid.getItemByCell(newCell);
    }

    @Override
    public List<AbstractItem> getItemsForDraw() {
      return currVendor.itemGrid.itemList;
    }

    public boolean dropPickupToCell(Vector newCell) {
      currVendor.buyItem(RpgGame.inst.localPlayer.inv.pickedUpItem);
      RpgGame.inst.localPlayer.inv.pickedUpItem = null;
      return true;
    }

    @Override
    public void handleClickItemLeft(AbstractItem clickedItem) {
      //don't do anyting
    }

    @Override
    public void handleClickItemRight(AbstractItem clickedItem) {
      if (RpgGame.inst.localPlayer.inv.currGold>=clickedItem.dropValue) {
        RpgGame.inst.localPlayer.inv.currGold -= clickedItem.dropValue;
        if (RpgGame.inst.localPlayer.inv.pickedUpItem==null) {
          currVendor.itemGrid.remove(clickedItem);
          RpgGame.inst.localPlayer.inv.pickupItem(clickedItem);
        }
        }
    }

  }

  public class BackgroundLabel extends AbstractLabel {
    public BackgroundLabel() {
      pos = new Vector(0.00f, 0.00f) ;
      size = new Vector(700.00f, 700.00f) ;
      img = 229;
      displayText = "";
    }
  }

  public class CloseButton extends AbstractButton {

    public CloseButton() {
      pos = new Vector(0.94f, 0.01f) ;
      size = new Vector(35.00f, 35.00f) ;
      img = 242;
      imgPressed = 243;
    }

    @Override
    public void execAction() {
      PhilonGame.gForms.removeForm( AbstractVendorForm.this );
    }

  }

}
