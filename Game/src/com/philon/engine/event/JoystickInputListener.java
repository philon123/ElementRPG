package com.philon.engine.event;

import com.philon.engine.input.AbstractController.Joystick;

public abstract class JoystickInputListener extends InputListener<Joystick> {
  @Override
  protected boolean execHandleEvent(InputEvent<Joystick> newEvent) {
    switch(newEvent.opCode) {
      case JoystickEvent.ACTION_CHANGED:
        return execChanged(newEvent.source);
    }
    return false;
  }
  protected boolean execChanged(Joystick source) {
    return true;
  }
}