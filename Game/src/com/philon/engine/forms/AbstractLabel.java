package com.philon.engine.forms;

import com.badlogic.gdx.graphics.Color;
import com.philon.engine.util.Vector;
import com.philon.rpg.ImageData;
import com.philon.rpg.RpgGame;

public abstract class AbstractLabel extends AbstractGUIElement {
  public int ID;
  public String displayText;
  
  public AbstractLabel() {
  }
  
  @Override
  public void draw(Vector containerPos, Vector containerSize) {
    Vector tmpPos = Vector.mul( pos, containerSize ).addInst( containerPos );
    Vector tmpSize = size;
    RpgGame.inst.gGraphics.drawTextureRect( ImageData.images[img].frames[0], tmpPos, tmpSize, Color.WHITE );
    RpgGame.inst.gGraphics.drawText( RpgGame.inst.gGraphics.font, getDisplayText(), tmpPos.addInst(new Vector(5)) );
  }
  
  public String getDisplayText() {
    return displayText;
  }
  
}