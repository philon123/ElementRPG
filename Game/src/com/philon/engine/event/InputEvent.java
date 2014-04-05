package com.philon.engine.event;

import com.philon.engine.input.AbstractController.ControllerElement;

public abstract class InputEvent<T extends ControllerElement> {
  public int opCode;
  public T source;

  public InputEvent(T newSource, int newOpCode) {
    opCode = newOpCode;
    source = newSource;
  }
}