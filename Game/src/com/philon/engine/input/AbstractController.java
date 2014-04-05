package com.philon.engine.input;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.philon.engine.event.ButtonEvent;
import com.philon.engine.event.InputEvent;
import com.philon.engine.event.JoystickEvent;
import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;

public class AbstractController {
  protected LinkedHashMap<Class<? extends ControllerElement>, ControllerElement> controllerMap
    = new LinkedHashMap<Class<? extends ControllerElement>, ControllerElement>();

  public AbstractController() {
    controllerMap = Util.createInnerClasses( AbstractController.class, getClass(), ControllerElement.class, new Object[]{this} );
  }

  public LinkedList<InputEvent<?>> poll() {
    LinkedList<InputEvent<?>> result = new LinkedList<InputEvent<?>>();

    for(ControllerElement currElement : controllerMap.values()) {
      InputEvent<?> newEvt = currElement.poll();
      if(newEvt!=null) result.add(newEvt);
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  public <T> T getElementByClass(Class<T> clazz) {
    return (T) controllerMap.get(clazz);
  }

  public abstract class ControllerElement {
    public ControllerElement() {
    }

    public abstract InputEvent<?> poll();

    protected abstract InputEvent<?> createEvent(int newOP);

    public abstract Class<? extends InputEvent<?>> getEventClass();
  }


  public abstract class Button extends ControllerElement {
    public boolean isActive = false;

    protected ButtonEvent setIsActive(boolean newIsActive) {
      if(!isActive && !newIsActive) return null; //nothing happened

      if(isActive && newIsActive) { //still pressed
        return createEvent(ButtonEvent.ACTION_PRESSED);
      }

      if(isActive) {
        isActive = false;
        return createEvent(ButtonEvent.ACTION_UP);
      }

      isActive = true;
      return createEvent(ButtonEvent.ACTION_DOWN);
    }

    @Override
    public ButtonEvent createEvent(int newOP) {
      return new ButtonEvent(this, newOP);
    }

    @Override
    public Class<? extends InputEvent<Button>> getEventClass() {
      return ButtonEvent.class;
    }
  }

  public abstract class Joystick extends ControllerElement {
    public Vector pos = new Vector(); //current joystick direction

    protected JoystickEvent setPos(Vector newPos) {
      if(pos.isAllEqual(newPos) && pos.equals(new Vector())) {
        return null;
      } else {
        pos = newPos;
        return createEvent(JoystickEvent.ACTION_CHANGED);
      }
    }

    @Override
    public JoystickEvent createEvent(int newOP) {
      return new JoystickEvent(this, newOP);
    }

    @Override
    public Class<? extends InputEvent<Joystick>> getEventClass() {
      return JoystickEvent.class;
    }
  }
}
