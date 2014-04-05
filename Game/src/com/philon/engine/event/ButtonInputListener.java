package com.philon.engine.event;

import com.philon.engine.input.AbstractController.Button;

public abstract class ButtonInputListener extends InputListener<Button> {
  @Override
  public boolean execHandleEvent(InputEvent<Button> newEvent) {
    switch(newEvent.opCode) {
      case ButtonEvent.ACTION_DOWN :
        return execDown();
      case ButtonEvent.ACTION_UP :
        return execUp();
      case ButtonEvent.ACTION_PRESSED :
        return execPressed();
    }
    return false;
  }
  protected boolean execDown() {
    return true;
  }
  protected boolean execUp() {
    return true;
  }
  protected boolean execPressed() {
    return true;
  }
}