package com.philon.engine.event;

import com.philon.engine.input.AbstractController.Joystick;

public class JoystickEvent extends InputEvent<Joystick> {
  public static final int ACTION_CHANGED = 1;
  public JoystickEvent(Joystick newSource, int newOpCode) {
    super(newSource, newOpCode);
  }
}