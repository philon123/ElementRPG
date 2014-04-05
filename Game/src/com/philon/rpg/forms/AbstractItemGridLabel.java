package com.philon.rpg.forms;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.philon.engine.Data;
import com.philon.engine.event.ButtonInputListener;
import com.philon.engine.event.JoystickInputListener;
import com.philon.engine.forms.GuiElement;
import com.philon.engine.input.AbstractController.Joystick;
import com.philon.engine.input.Controller.MouseButton1;
import com.philon.engine.input.Controller.MouseButton2;
import com.philon.engine.input.Controller.MouseCursor;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.map.mo.CombatMapObj.StateCasting;
import com.philon.rpg.map.mo.UpdateMapObj;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.item.category.ConsumableItem;
import com.philon.rpg.mos.player.AbstractChar;
import com.philon.rpg.spell.SpellData;

/**
 * Needs to implement getConfiguredScale()
 */
public abstract class AbstractItemGridLabel extends GuiElement {
  protected Vector mouseOverCell = null;
  protected AbstractItem hoveredOverItem = null;

  protected ItemPopupForm popupForm = new ItemPopupForm();

  public AbstractItemGridLabel() {

    addInputListener(MouseCursor.class, new JoystickInputListener() {
      @Override
      protected boolean execChanged(Joystick source) {
        mouseOverCell = null;
        Vector offsetRemoved = Vector.sub(source.pos, absPos);
        if(offsetRemoved.isEitherSmaller(new Vector()) || offsetRemoved.isEitherLarger(absSize)) {
          mouseOverCell = null;
        } else {
          Vector configuredGridSize = getConfiguredGridSize();
          mouseOverCell = offsetRemoved.divInst(absSize).mulInst(configuredGridSize).floorAllInst();
          if(mouseOverCell.x == configuredGridSize.x) mouseOverCell.x = configuredGridSize.x-1;
          if(mouseOverCell.y == configuredGridSize.y) mouseOverCell.y = configuredGridSize.y-1;
        }

        hoveredOverItem = mouseOverCell==null ? null : getItemByCell(mouseOverCell);

        if(hoveredOverItem==null) {
          if(isPopupOpen()) {
            RpgGame.inst.guiHierarchy.removeElement(popupForm);
          }
        } else {
          if(popupForm.getItem()!=hoveredOverItem) {
            popupForm.setItem(hoveredOverItem);
          }
          if(!isPopupOpen()) {
            RpgGame.inst.guiHierarchy.insertElement(popupForm);
          }
          popupForm.absPos = source.pos.copy();
        }

        return true;
      }
    });

    addInputListener(MouseButton1.class, new ButtonInputListener() {
      @Override
      protected boolean execDown() {
        if(mouseOverCell==null) return false;
        AbstractChar character = RpgGame.inst.getExclusiveUser().character;
        if( character.inv.pickedUpItem!=null ) {
          dropPickupToCell(mouseOverCell);
        } else if( hoveredOverItem!=null ) {
          if( character.preparedSpell == SpellData.IDENTIFY ) {
            character.castPreparedSpell( null, hoveredOverItem );
            popupForm.setItem(hoveredOverItem); //refresh form
          } else {
            handleClickItemLeft(hoveredOverItem);
          }
        }
        return true;
      }
    });

    addInputListener(MouseButton2.class, new ButtonInputListener() {
      @Override
      protected boolean execDown() {
        AbstractItem itemAtMouse = getItemByCell(mouseOverCell);
        UpdateMapObj character = RpgGame.inst.getExclusiveUser().character;
        if( character.currState instanceof StateCasting ) {
          character.changeState( CombatMapObj.StateIdle.class );
        } else {
          if( itemAtMouse!=null ) {
            handleClickItemRight(itemAtMouse);
          }
        }
        return true;
      }
    });
  }

  private boolean isPopupOpen() {
    return RpgGame.inst.guiHierarchy.getElementByClass(ItemPopupForm.class)!=null;
  }

  @Override
  protected boolean isStrechable() {
    return false;
  }
  @Override
  protected float getConfiguredXYRatio() {
    Vector gridSize = getConfiguredGridSize();
    return gridSize.x/gridSize.y;
  }
  @Override
  public void execCursorExit() {
    if(isPopupOpen()) {
      RpgGame.inst.guiHierarchy.removeElement(popupForm);
    }
  }

  protected abstract Vector getConfiguredGridSize();
  protected abstract AbstractItem getItemByCell(Vector newCell);
  protected abstract List<AbstractItem> getItemsForDraw();
  protected abstract void dropPickupToCell(Vector newCell);

  @Override
  protected void execDraw(SpriteBatch batch) {
    super.execDraw(batch);

    drawGrid(batch);
    for( AbstractItem tmpItem : getItemsForDraw() ) {
      if (tmpItem!=null) drawInvItem( batch, tmpItem, tmpItem.pos );
    }
  }

  protected void handleClickItemLeft(AbstractItem clickedItem) {
    RpgGame.inst.getExclusiveUser().character.inv.pickupItem( clickedItem );
  }

  protected void handleClickItemRight(AbstractItem clickedItem) {
    if( clickedItem instanceof ConsumableItem ) {
      RpgGame.inst.getExclusiveUser().character.consumeItem( (ConsumableItem)clickedItem );
    }
  }

  protected void drawInvItem( SpriteBatch batch, AbstractItem it, Vector cellPos ) {
    Vector itemRelPos = Vector.div( cellPos, getConfiguredGridSize() );
    Vector itemRelSize = Vector.div( it.getInvSize(), getConfiguredGridSize() );
    Vector itemAbsPos = Vector.add( absPos, Vector.mul(absSize, itemRelPos) );
    Vector itemAbsSize = Vector.mul(absSize, itemRelSize);

    Color tmpColor;
    if( it.reqMetFlag ) {
      if( it.isIdentified ) {
        tmpColor = new Color(0.06f, 0.6f, 0.06f, 1);
      } else {
        tmpColor = new Color( 0.06f, 0.06f, 0.6f, 1 );
      }
    } else {
      tmpColor = new Color( 0.6f, 0.06f, 0.06f, 1 );
    }

    batch.setColor(tmpColor);
    batch.draw(Data.textures.get(230).frames[0],
        itemAbsPos.x, itemAbsPos.y, itemAbsSize.x, itemAbsSize.y);
    batch.setColor(Color.WHITE);
    batch.draw(Data.textures.get(it.getImgInv()).frames[0],
        itemAbsPos.x, itemAbsPos.y, itemAbsSize.x, itemAbsSize.y);
  }

  protected void drawGrid(SpriteBatch batch) {
//    Vector gridSize = getConfiguredGridSize();
//    Vector cellSize = new Vector(absSize.y/gridSize.y);
//    for( int x = 0; x <= gridSize.x; x++ ) {
//      RpgGame.inst.graphics.drawLine( batch,
//          Vector.add( absPos, new Vector(x*cellSize.x, 0) ),
//          Vector.add( absPos, new Vector(x*cellSize.x, gridSize.y*cellSize.y) )
//      );
//    }
//    for( int y = 0; y <= gridSize.y; y++ ) {
//      RpgGame.inst.graphics.drawLine( batch,
//          Vector.add( absPos, new Vector(0, y*cellSize.y) ),
//          Vector.add( absPos, new Vector(gridSize.x*cellSize.x, y*cellSize.y) )
//      );
//    }
  }

}
