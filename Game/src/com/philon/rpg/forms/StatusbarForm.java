package com.philon.rpg.forms;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.philon.engine.forms.AbstractButton;
import com.philon.engine.forms.GuiElement;
import com.philon.engine.util.Util.Order;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.player.AbstractChar;
import com.philon.rpg.spell.AbstractSpell;
import com.philon.rpg.stat.StatsObj.StatM1Stype;
import com.philon.rpg.stat.StatsObj.StatM2Stype;

@Order(10)
public class StatusbarForm extends GuiElement {

  @Override
  protected boolean isStrechable() {
    return false;
  }
  @Override
  protected boolean isScaleByX() {
    return true;
  }
  @Override
  protected float getConfiguredScale() {
    return 1;
  }
  @Override
  protected float getConfiguredXYRatio() {
    return 20;
  }
  @Override
  protected int getConfiguredAlignment() {
    return GuiElement.ALIGN_BOTTOM_LEFT;
  }
  @Override
  protected int getConfiguredBackground() {
    return 239;
  }

  @Override
  public void execDraw(SpriteBatch batch) {
//  if( fID==FormData.STATUSBAR ) { //TODO health orbs
//  float orbFill;
//  //draw health orb
//  if( Game.inst.localPlayer.stats.values[StatData.HEALTH]==0 ) {
//    orbFill=0;
//  } else {
//    orbFill = Game.inst.localPlayer.stats.values[StatData.HEALTH] / Game.inst.localPlayer.stats.values[StatData.MAXHEALTH];
//  }
//  currTemp = 42; //orb health label
//  tmpPos = Vector.mul( LabelData.pos[currTemp], size ).addInst( new Vector(30*1.5f, 8*1.5f).addInst( pos ) );
//  tmpSize = new Vector( 80*1.5f, 80*1.5f );
//  setColor(255, 60, 60);
//  drawOval( tmpPos.x, tmpPos.y, tmpSize.x, tmpSize.y );
//  setColor(255, 255, 255);
//  drawRect( tmpPos.x, tmpPos.y, tmpSize.x, tmpSize.y*(1-orbFill) );
//
//  //draw mana orb
//  if( Game.inst.localPlayer.stats.values[StatData.MANA]==0 ) {
//    orbFill=0;
//  } else {
//    orbFill = Game.inst.localPlayer.stats.values[StatData.MANA] / Game.inst.localPlayer.stats.values[StatData.MAXMANA];
//  }
//  currTemp = 43; //orb mana label
//  tmpPos = Vector.mul( LabelData.pos[currTemp], size ).addInst( new Vector(4*1.5f, 12*1.5f).addInst( pos ) );
//  tmpSize = new Vector( 82*1.5f, 80*1.5f );
//  setColor(60, 60, 255);
//  drawOval( tmpPos.x, tmpPos.y, tmpSize.x, tmpSize.y );
//  setColor(255, 255, 255);
//  drawRect( tmpPos.x, tmpPos.y, tmpSize.x, tmpSize.y*(1-orbFill) );
//}

    super.execDraw(batch);

  }

  public class OrbHealthLabel extends GuiElement {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 174f/150f;
    }
    @Override
    protected float getConfiguredScale() {
      return 2;
    }
    @Override
    protected int getConfiguredAlignment() {
      return GuiElement.ALIGN_BOTTOM_LEFT;
    }
    @Override
    protected int getConfiguredBackground() {
      return 237;
    }
  }

  public class OrbManaLabel extends GuiElement {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 174f/150f;
    }
    @Override
    protected float getConfiguredScale() {
      return 2;
    }
    @Override
    protected int getConfiguredAlignment() {
      return GuiElement.ALIGN_BOTTOM_RIGHT;
    }
    @Override
    protected int getConfiguredBackground() {
      return 238;
    }
  }

  public class CenteredGroupBox extends GuiElement {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 12;
    }
    @Override
    protected float getConfiguredScale() {
      return 1;
    }
    @Override
    protected int getConfiguredAlignment() {
      return GuiElement.ALIGN_CENTER;
    }

    public class Spell1Button extends AbstractButton {
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
        return 1f;
      }
      @Override
      protected int getConfiguredAlignment() {
        return GuiElement.ALIGN_TOP_LEFT;
      }
      @Override
      @SuppressWarnings("unchecked")
      protected int getConfiguredBackground() {
        Class<? extends AbstractSpell> tmpSpell = (Class<? extends AbstractSpell>)RpgGame.inst.getExclusiveUser().character.stats.getStatValue(StatM1Stype.class);
        return AbstractSpell.getDescriptor(tmpSpell).getImgIcon();
      }
      @Override
      protected int getConfiguredImgPressed() {
        return getConfiguredBackground();
      }
      @Override
      public void execAction() {
        SpellSelectForm existingForm = RpgGame.inst.guiHierarchy.getElementByClass(SpellSelectForm.class);
        if(existingForm==null) {
          SpellSelectForm ssf = new SpellSelectForm();
          ssf.setIsSelectingForLeft(true);
          RpgGame.inst.guiHierarchy.insertElement(ssf);
        } else {
          RpgGame.inst.guiHierarchy.removeElementByClass(SpellSelectForm.class);
        }
      }
    }

    public class Spell2Button extends AbstractButton {
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
        return 1f;
      }
      @Override
      protected int getConfiguredAlignment() {
        return GuiElement.ALIGN_TOP_RIGHT;
      }
      @Override
      @SuppressWarnings("unchecked")
      protected int getConfiguredBackground() {
        Class<? extends AbstractSpell> tmpSpell = (Class<? extends AbstractSpell>)RpgGame.inst.getExclusiveUser().character.stats.getStatValue(StatM2Stype.class);
        return AbstractSpell.getDescriptor(tmpSpell).getImgIcon();
      }
      @Override
      protected int getConfiguredImgPressed() {
        return getConfiguredBackground();
      }
      @Override
      public void execAction() {
        SpellSelectForm existingForm = RpgGame.inst.guiHierarchy.getElementByClass(SpellSelectForm.class);
        if(existingForm==null) {
          SpellSelectForm ssf = new SpellSelectForm();
          ssf.setIsSelectingForLeft(false);
          RpgGame.inst.guiHierarchy.insertElement(ssf);
        } else {
          RpgGame.inst.guiHierarchy.removeElementByClass(SpellSelectForm.class);
        }
      }
    }

    public class BeltGridLabel extends AbstractItemGridLabel {
      @Override
      protected float getConfiguredScale() {
        return 1;
      }
      @Override
      protected Vector getConfiguredGridSize() {
        return new Vector(10, 1);
      }
      @Override
      protected Vector getConfiguredPosition() {
        return new Vector(1/12f, 0);
      }
      @Override
      protected int getConfiguredBackground() {
        return 297;
      }
      @Override
      public void dropPickupToCell(Vector newCell) {
        AbstractChar character = RpgGame.inst.getExclusiveUser().character;
        AbstractItem result = character.inv.beltGrid.add(character.inv.pickedUpItem, newCell, true);
        if(result==null) {
          character.inv.pickedUpItem = null;
        } else if(result!=character.inv.pickedUpItem) {
          character.inv.pickedUpItem = result;
        }
      }
      @Override
      public AbstractItem getItemByCell(Vector newCell) {
        return RpgGame.inst.getExclusiveUser().character.inv.beltGrid.getItemByCell(newCell);
      }
      @Override
      public List<AbstractItem> getItemsForDraw() {
        return RpgGame.inst.getExclusiveUser().character.inv.beltGrid.itemList;
      }
    }
  }

}
