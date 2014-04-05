package com.philon.engine.input;


public abstract class Controller extends AbstractController {

  public interface MouseControllerElement { //class-level marker for mouse elements
  }

  public abstract class MouseCursor extends Joystick implements MouseControllerElement {
  }

  public abstract class MouseButton1 extends Button implements MouseControllerElement {
  }

  public abstract class MouseButton2 extends Button implements MouseControllerElement {
  }

  public abstract class StartButton extends Button {
  }

  public abstract class SelectButton extends Button {
  }

  public abstract class JoystickLeft extends Joystick {
  }

  public abstract class JoystickRight extends Joystick {
  }

  public abstract class Button1 extends Button {
  }

  public abstract class Button2 extends Button {
  }

  public abstract class Button3 extends Button {
  }
}
