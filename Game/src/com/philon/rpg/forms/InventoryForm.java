package com.philon.rpg.forms;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.philon.engine.PhilonGame;
import com.philon.engine.forms.AbstractButton;
import com.philon.engine.forms.AbstractForm;
import com.philon.engine.forms.AbstractLabel;
import com.philon.engine.util.Vector;
import com.philon.rpg.ImageData;
import com.philon.rpg.RpgGame;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.player.inventory.Inventory.Equip;

public class InventoryForm extends AbstractForm {

  @Override
  public Vector getPosByScreenSize(Vector newScreenSize) {
    return new Vector(newScreenSize.x-size.x, 0);
  }

  @Override
  public Vector getSizeByScreenSize(Vector newScreenSize) {
    return new Vector(700, 700);
  }

  @Override
  public void draw() {
    super.draw();

    //draw popup
    if( !(RpgGame.inst.localPlayer.inv.hoveredOverItem==null) ) {
      drawInvItemPopup( RpgGame.inst.localPlayer.inv.hoveredOverItem, RpgGame.inst.gInput.realMousePos.copy() );
    }
  }

  public static void drawInvItemPopup( AbstractItem it, Vector newPixel ) {
//    Vector itemSize = Vector.mul( ItemData.invSize[it.id], FormData.invCellSize );
    Vector pixSize = new Vector( 300, 300 );
    String text = it.getDisplayTextBody();

    if (newPixel.x+pixSize.x > PhilonGame.screenPixSize.x) newPixel.x = PhilonGame.screenPixSize.x-pixSize.x;
    if (newPixel.y+pixSize.y >  PhilonGame.screenPixSize.y) newPixel.y =  PhilonGame.screenPixSize.y-pixSize.y;

    RpgGame.inst.gGraphics.drawTextureRect( ImageData.images[ImageData.IMG_FORM_BACK_POPUP].frames[0], newPixel, pixSize, Color.WHITE );
    RpgGame.inst.gGraphics.drawTextMultiline( RpgGame.inst.gGraphics.font, text, newPixel.addInst(new Vector(10)) );
  }

  public WeaponLabel getWeaponLabel() {
    return (WeaponLabel) getElementByClass(WeaponLabel.class);
  }

  public ShieldLabel getShieldLabel() {
    return (ShieldLabel) getElementByClass(ShieldLabel.class);
  }

  public class BackgroundLabel extends AbstractLabel {

    public BackgroundLabel() {
      ID = 1;
      pos = new Vector(0.00f, 0.00f) ;
      size = new Vector(700.00f, 700.00f) ;
      img = 228;
      displayText = "";
    }

  }

  public class HelmLabel extends AbstractEquipLabel {

    public HelmLabel() {
      ID = 31;
      pos = new Vector(0.40f, 0.05f) ;
      size = new Vector(125.00f, 125.00f) ;
      img = 290;
      displayText = "";

      equipSlotNr = Equip.INV_HELM;
    }

  }

  public class WeaponLabel extends AbstractEquipLabel {

    public WeaponLabel() {
      ID = 32;
      pos = new Vector(0.05f, 0.26f) ;
      size = new Vector(125.00f, 188.00f) ;
      img = 294;
      displayText = "";

      equipSlotNr = Equip.INV_WEAPON;
    }

    @Override
    public void handleClickLeft(Vector clickedPixel) {
      super.handleClickLeft(clickedPixel);
    }

  }

  public class ArmorLabel extends AbstractEquipLabel {

    public ArmorLabel() {
      ID = 33;
      pos = new Vector(0.40f, 0.26f) ;
      size = new Vector(125.00f, 188.00f) ;
      img = 295;
      displayText = "";

      equipSlotNr = Equip.INV_ARMOR;
    }

  }

  public class ShieldLabel extends AbstractEquipLabel {

    public ShieldLabel() {
      ID = 34;
      pos = new Vector(0.75f, 0.26f) ;
      size = new Vector(125.00f, 188.00f) ;
      img = 293;
      displayText = "";

      equipSlotNr = Equip.INV_SHIELD;
    }

  }

  public class AmuletLabel extends AbstractEquipLabel {

    public AmuletLabel() {
      ID = 35;
      pos = new Vector(0.61f, 0.14f) ;
      size = new Vector(63.00f, 63.00f) ;
      img = 289;
      displayText = "";

      equipSlotNr = Equip.INV_AMULETT;
    }

  }

  public class Ring1Label extends AbstractEquipLabel {

    public Ring1Label() {
      ID = 36;
      pos = new Vector(0.29f, 0.44f) ;
      size = new Vector(63.00f, 63.00f) ;
      img = 291;
      displayText = "";

      equipSlotNr = Equip.INV_RING1;
    }

  }

  public class Ring2Label extends AbstractEquipLabel {

    public Ring2Label() {
      ID = 37;
      pos = new Vector(0.61f, 0.44f) ;
      size = new Vector(63.00f, 63.00f) ;
      img = 292;
      displayText = "";

      equipSlotNr = Equip.INV_RING2;
    }

  }

  public class GoldLabel extends AbstractLabel {

    public GoldLabel() {
      ID = 39;
      pos = new Vector(0.05f, 0.54f) ;
      size = new Vector(70.00f, 35.00f) ;
      img = 244;
      displayText = "Gold:";
    }

  }

  public class GoldAmountLabel extends AbstractLabel {

    public GoldAmountLabel() {
      ID = 40;
      pos = new Vector(0.16f, 0.54f) ;
      size = new Vector(70.00f, 35.00f) ;
      img = 244;
      displayText = "";
    }

    @Override
    public String getDisplayText() {
      return "" + RpgGame.inst.localPlayer.inv.currGold;
    }

  }

  public class InvSpaceLabel extends AbstractItemGridLabel {

    public InvSpaceLabel() {
      ID = 38;
      pos = new Vector(0.05f, 0.60f) ;
      size = new Vector(630.00f, 245.00f) ;
      img = 296;
      displayText = "";

      gridSize = new Vector( 10, 4 );
      cellSize = Vector.div(size, gridSize);
    }

    @Override
    public AbstractItem getItemByCell(Vector newCell) {
      return RpgGame.inst.localPlayer.inv.invGrid.getItemByCell(newCell);
    }

    @Override
    public List<AbstractItem> getItemsForDraw() {
      return RpgGame.inst.localPlayer.inv.invGrid.itemList;
    }

    @Override
    public void dropPickupToCell(Vector newCell) {
      AbstractItem result = RpgGame.inst.localPlayer.inv.invGrid.add(RpgGame.inst.localPlayer.inv.pickedUpItem, newCell, true);
      if(result==null) {
        RpgGame.inst.localPlayer.inv.pickedUpItem = null;
      } else if(result!=RpgGame.inst.localPlayer.inv.pickedUpItem) {
        RpgGame.inst.localPlayer.inv.pickedUpItem = result;
      }
    }

  }

  public class CloseButton extends AbstractButton {

    public CloseButton() {
      ID = 1;
      pos = new Vector(0.01f, 0.01f) ;
      size = new Vector(35.00f, 35.00f) ;
      img = 242;
      imgPressed = 243;
    }

    @Override
    public void execAction() {
      PhilonGame.gForms.removeForm( InventoryForm.this );
    }

  }

}
