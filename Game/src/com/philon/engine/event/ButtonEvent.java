package com.philon.engine.event;

import com.philon.engine.input.AbstractController.Button;

public class ButtonEvent extends InputEvent<Button> {
  public static final int ACTION_DOWN = 1;
  public static final int ACTION_UP = 0;
  public static final int ACTION_PRESSED = 2;
  public ButtonEvent(Button newSource, int newOpCode) {
    super(newSource, newOpCode);
  }
}