package com.philon.engine.event;

import com.philon.engine.input.AbstractController.ControllerElement;

public abstract class InputListener<T extends ControllerElement> {
  /**
   * Return true if the event was handled, false if not. If you return false, the event will be delegated to parent
   * This is useful for overlapping elements,
   * i.e. using round elements where bounding boxes overlap but not the actual elements
   */
  public boolean handleEvent(InputEvent<?> newEvent) {
    return execHandleEvent(castEvent(newEvent));
  }

  @SuppressWarnings("unchecked")
  public <U extends ControllerElement> InputEvent<T> castEvent(InputEvent<U> newEvent) {
    return (InputEvent<T>) newEvent;
  }

  protected abstract boolean execHandleEvent(InputEvent<T> newEvent);
}