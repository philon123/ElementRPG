package com.philon.engine.forms;

import com.badlogic.gdx.graphics.Color;
import com.philon.engine.util.Vector;
import com.philon.rpg.ImageData;
import com.philon.rpg.RpgGame;

public abstract class AbstractButton extends AbstractGUIElement {
  public int ID;
  public int imgPressed;
    
  public AbstractButton() {
  }
  
  @Override
  public void draw(Vector containerPos, Vector containerSize) {
    Vector tmpPos = Vector.mul( pos, containerSize ).addInst( containerPos );
    Vector tmpSize = size;
    int tmpImage = (this==RpgGame.inst.gInput.currPressedElement) ? imgPressed : img;
    RpgGame.inst.gGraphics.drawTextureRect( ImageData.images[tmpImage].frames[0], tmpPos, tmpSize, Color.WHITE );
  }
  
  @Override
  public void handleClickLeft(Vector clickedPixel) {
    execAction();
  }
  
  public abstract void execAction();
    
}