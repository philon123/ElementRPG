package com.philon.engine.forms;

import com.philon.engine.util.Vector;

public abstract class AbstractGUIElement {
  public Vector pos;
  public Vector size;
  public int img;
  
  public abstract void draw(Vector containerPos, Vector containerSize);
  
  public void handleMouseOver(Vector newMousePos) {
  }
  
  public void handleClickLeft(Vector clickedPixel) {
  }
  
  public void handleClickRight(Vector clickedPixel) {
  }
  
  public void update() {
  }
  
}
