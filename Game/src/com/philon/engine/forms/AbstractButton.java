package com.philon.engine.forms;

import com.philon.engine.event.ButtonInputListener;
import com.philon.engine.event.InputEvent;
import com.philon.engine.event.JoystickInputListener;
import com.philon.engine.input.AbstractController.Joystick;
import com.philon.engine.input.Controller.MouseButton1;
import com.philon.engine.input.Controller.MouseCursor;

public abstract class AbstractButton extends GuiElement {
  public boolean isPressed = false;

  public AbstractButton() {
    addInputListener(MouseButton1.class, new ButtonInputListener(){
      @Override
      protected boolean execDown() {
        isPressed = true;
        return true;
      }
      @Override
      protected boolean execUp() {
        if(isPressed) {
          execAction();
          return true;
        }
        return false;
      }
    });
    addInputListener(MouseCursor.class, new JoystickInputListener() {
      @Override
      protected boolean execHandleEvent(InputEvent<Joystick> newEvent) {
        if(!isPressed) return false;

        if( newEvent.source.pos.isAllLOE(absPos) && newEvent.source.pos.isAllSOE(absPos.copy().addInst(absSize)) ) {
          isPressed = true;
        } else {
          isPressed = false;
        }
        return super.execHandleEvent(newEvent);
      }
    });
  }

  @Override
  protected boolean isStrechable() {
    return false;
  }
  @Override
  protected int getConfiguredBackground() {
    return isPressed ? getConfiguredImgPressed() : getConfiguredBackground();
  }

  protected abstract int getConfiguredImgPressed();
  protected abstract void execAction();

}