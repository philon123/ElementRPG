package com.philon.rpg.forms;

import java.util.List;

import com.philon.engine.event.ButtonInputListener;
import com.philon.engine.forms.AbstractTextBox;
import com.philon.engine.forms.GuiElement;
import com.philon.engine.input.Controller.MouseButton1;
import com.philon.engine.util.Util.Order;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.player.AbstractChar;
import com.philon.rpg.mos.player.inventory.Inventory.Equip;

@Order(10)
public class InventoryForm extends GuiElement {

  public InventoryForm() {
    addInputListener(MouseButton1.class, new ButtonInputListener() {
      @Override
      protected boolean execDown() {
        return true;
      }
    });
  }

  @Override
  protected boolean isStrechable() {
    return false;
  }
  @Override
  protected float getConfiguredXYRatio() {
    return 1;
  }
  @Override
  protected float getConfiguredScale() {
    return 0.75f;
  }
  @Override
  protected int getConfiguredAlignment() {
    return GuiElement.ALIGN_TOP_RIGHT;
  }
  @Override
  protected int getConfiguredBackground() {
    return 228;
  }

//don't need a close button anymore
//  public class CloseButton extends AbstractButton {
//    @Override
//    protected int getConfiguredBackground() {
//      return 242;
//    }
//    @Override
//    protected int getConfiguredImgPressed() {
//      return 243;
//    }
//    @Override
//    protected float getConfiguredXYRatio() {
//      return 1;
//    }
//    @Override
//    protected float getConfiguredScale() {
//      return 0.05f;
//    }
//    @Override
//    protected Vector getConfiguredPosition() {
//      return new Vector(0.01f, 0.01f) ;
//    }
//    @Override
//    public void execAction() {
//      RpgGame.inst.guiHierarchy.removeElementByClass(InventoryForm.class);
//    }
//  }

  public class EquipGroupBox extends GuiElement {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 8/6.5f; //(6+(4*0.5f)) / (5 + (3*0.5f)); //gridtiles + (gaps * 0.5)
    }
    @Override
    protected float getConfiguredScale() {
      return 0.54f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.29f, 0.05f);
    }

    public class HelmLabel extends AbstractEquipLabel {
      public HelmLabel() {
        equipSlotNr = Equip.INV_HELM;
      }
      @Override
      protected Vector getConfiguredPosition() {
        return new Vector(3f/8f, 0.5f/6.5f);
      }
      @Override
      protected float getConfiguredScale() {
        return 2f/6.5f;
      }
      @Override
      protected int getConfiguredBackground() {
        return 290;
      }
      @Override
      protected Vector getConfiguredGridSize() {
        return new Vector(2);
      }
    }

    public class WeaponLabel extends AbstractEquipLabel {
      public WeaponLabel() {
        equipSlotNr = Equip.INV_WEAPON;
      }
      @Override
      protected Vector getConfiguredPosition() {
        return new Vector(0.5f/8f, 3f/6.5f) ;
      }
      @Override
      protected float getConfiguredScale() {
        return 3f/6.5f;
      }
      @Override
      protected int getConfiguredBackground() {
        return 294;
      }
      @Override
      protected Vector getConfiguredGridSize() {
        return new Vector(2, 3);
      }
    }

    public class ArmorLabel extends AbstractEquipLabel {
      public ArmorLabel() {
        equipSlotNr = Equip.INV_ARMOR;
      }
      @Override
      protected Vector getConfiguredPosition() {
        return new Vector(3f/8f, 3f/6.5f) ;
      }
      @Override
      protected float getConfiguredScale() {
        return 3f/6.5f;
      }
      @Override
      protected int getConfiguredBackground() {
        return 295;
      }
      @Override
      protected Vector getConfiguredGridSize() {
        return new Vector(2, 3);
      }
    }

    public class ShieldLabel extends AbstractEquipLabel {
      public ShieldLabel() {
        equipSlotNr = Equip.INV_SHIELD;
      }
      @Override
      protected Vector getConfiguredPosition() {
        return new Vector(5.5f/8f, 3f/6.5f) ;
      }
      @Override
      protected float getConfiguredScale() {
        return 3f/6.5f;
      }
      @Override
      protected int getConfiguredBackground() {
        return 293;
      }
      @Override
      protected Vector getConfiguredGridSize() {
        return new Vector(2, 3);
      }
    }

    public class AmuletLabel extends AbstractEquipLabel {
      public AmuletLabel() {
        equipSlotNr = Equip.INV_AMULETT;
      }
      @Override
      protected Vector getConfiguredPosition() {
        return new Vector(5.5f/8f, 1.5f/6.5f) ;
      }
      @Override
      protected float getConfiguredScale() {
        return 1f/6.5f;
      }
      @Override
      protected int getConfiguredBackground() {
        return 289;
      }
      @Override
      protected Vector getConfiguredGridSize() {
        return new Vector(1);
      }
    }

    public class Ring1Label extends AbstractEquipLabel {
      public Ring1Label() {
        equipSlotNr = Equip.INV_RING1;
      }
      @Override
      protected Vector getConfiguredPosition() {
        return new Vector(0.5f/8f, 0.5f/6.5f) ;
      }
      @Override
      protected float getConfiguredScale() {
        return 1f/6.5f;
      }
      @Override
      protected int getConfiguredBackground() {
        return 291;
      }
      @Override
      protected Vector getConfiguredGridSize() {
        return new Vector(1);
      }
    }

    public class Ring2Label extends AbstractEquipLabel {
      public Ring2Label() {
        equipSlotNr = Equip.INV_RING2;
      }
      @Override
      protected Vector getConfiguredPosition() {
        return new Vector(1.5f/8f, 1.5f/6.5f) ;
      }
      @Override
      protected float getConfiguredScale() {
        return 1f/6.5f;
      }
      @Override
      protected int getConfiguredBackground() {
        return 292;
      }
      @Override
      protected Vector getConfiguredGridSize() {
        return new Vector(1);
      }
    }

  }

  public class GoldLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 3;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.05f, 0.05f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "Gold:";
    }
  }

  public class GoldAmountLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 3;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.05f, 0.125f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "" + RpgGame.inst.getExclusiveUser().character.inv.currGold;
    }
  }

  public class InvSpaceLabel extends AbstractItemGridLabel {
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.05f, 0.59f);
    }
    @Override
    protected float getConfiguredScale() {
      return 0.36f;
    }
    @Override
    protected int getConfiguredBackground() {
      return 296;
    }
    @Override
    public AbstractItem getItemByCell(Vector newCell) {
      return RpgGame.inst.getExclusiveUser().character.inv.invGrid.getItemByCell(newCell);
    }
    @Override
    public List<AbstractItem> getItemsForDraw() {
      return RpgGame.inst.getExclusiveUser().character.inv.invGrid.itemList;
    }
    @Override
    public void dropPickupToCell(Vector newCell) {
      AbstractChar character = RpgGame.inst.getExclusiveUser().character;
      AbstractItem result = character.inv.invGrid.add(character.inv.pickedUpItem, newCell, true);
      if(result==character.inv.pickedUpItem) return;
      character.inv.pickedUpItem = null;
      if(result!=null) {
        character.inv.pickupItem(result);
      }
    }
    @Override
    protected Vector getConfiguredGridSize() {
      return new Vector(10, 4);
    }
  }

}
