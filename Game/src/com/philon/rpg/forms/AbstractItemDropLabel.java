package com.philon.rpg.forms;

import com.badlogic.gdx.graphics.Color;
import com.philon.engine.forms.AbstractLabel;
import com.philon.engine.util.Vector;
import com.philon.rpg.ImageData;
import com.philon.rpg.RpgGame;
import com.philon.rpg.mo.CombatMapObj;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.item.category.ConsumableItem;
import com.philon.rpg.spell.SpellData;

public abstract class AbstractItemDropLabel extends AbstractLabel {
  
  public abstract boolean dropPickupToPixel(Vector newPixel);

  public abstract AbstractItem getItemByPixel(Vector newPixel);
  
  @Override
  public void handleMouseOver(Vector newMousePos) {
    AbstractItem itemAtMouse = getItemByPixel(newMousePos);
    RpgGame.inst.localPlayer.inv.hoveredOverItem = itemAtMouse;
  }
  
  @Override
  public void handleClickLeft(Vector clickedPixel) {
    AbstractItem itemAtMouse = getItemByPixel(clickedPixel);
    if( RpgGame.inst.localPlayer.inv.pickedUpItem!=null ) {
      dropPickupToPixel(clickedPixel);
    } else if( itemAtMouse!=null ) {
      if( RpgGame.inst.localPlayer.preparedSpell == SpellData.IDENTIFY ) {
        RpgGame.inst.localPlayer.castPreparedSpell( null, itemAtMouse );
      } else {
        handleClickItemLeft(itemAtMouse);
      }
    }
  }
  
  public void handleClickItemLeft(AbstractItem clickedItem) {
    RpgGame.inst.localPlayer.inv.pickupItem( clickedItem );
  }
  
  @Override
  public void handleClickRight(Vector clickedPixel) {
    AbstractItem itemAtMouse = getItemByPixel(clickedPixel);
    if( RpgGame.inst.localPlayer.currState==CombatMapObj.StateCasting.class ) {
      RpgGame.inst.localPlayer.changeState( CombatMapObj.StateIdle.class );
    } else {
      if( itemAtMouse!=null ) {
        handleClickItemRight(itemAtMouse);
      }
    }
  }
  
  public void handleClickItemRight(AbstractItem clickedItem) {
    if( clickedItem instanceof ConsumableItem ) {
      RpgGame.inst.localPlayer.consumeItem( (ConsumableItem)clickedItem );
    }
  }
  
  public Vector getCellSize() {
    return new Vector(64);
  }
  
  public void drawInvItem( AbstractItem it, Vector newPixel ) {
    Vector cellSize = getCellSize();
    Vector pixSize = Vector.mul( it.invSize, cellSize );

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
    RpgGame.inst.gGraphics.drawTextureRect( ImageData.images[ImageData.IMG_FORM_BACK_SLOT].frames[0], newPixel, pixSize, tmpColor );
    RpgGame.inst.gGraphics.drawTextureRect( ImageData.images[it.imgInv].frames[0], newPixel, pixSize, Color.WHITE );
  }
  
}
