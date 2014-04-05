package com.philon.engine.input;

import com.badlogic.gdx.controllers.PovDirection;
import com.philon.engine.event.InputEvent;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;

/*
 * It seems there are different versions of gamepads with different ID Strings.
 * Therefore its IMO a better bet to check for:
 * if (controller.getName().toLowerCase().contains("xbox") &&
                 controller.getName().contains("360"))
 */
public class XBox360Controller extends Controller {
  public static final int BUTTON_X = 2;
  public static final int BUTTON_Y = 3;
  public static final int BUTTON_A = 0;
  public static final int BUTTON_B = 1;
  public static final int BUTTON_BACK = 6;
  public static final int BUTTON_START = 7;
  public static final PovDirection BUTTON_DPAD_UP = PovDirection.north;
  public static final PovDirection BUTTON_DPAD_DOWN = PovDirection.south;
  public static final PovDirection BUTTON_DPAD_RIGHT = PovDirection.east;
  public static final PovDirection BUTTON_DPAD_LEFT = PovDirection.west;
  public static final int BUTTON_LB = 4;
  public static final int BUTTON_L3 = 8;
  public static final int BUTTON_RB = 5;
  public static final int BUTTON_R3 = 9;
  public static final int AXIS_LEFT_X = 1; //-1 is left | +1 is right
  public static final int AXIS_LEFT_Y = 0; //-1 is up | +1 is down
  public static final int AXIS_LEFT_TRIGGER = 4; //value 0 to 1f
  public static final int AXIS_RIGHT_X = 3; //-1 is left | +1 is right
  public static final int AXIS_RIGHT_Y = 2; //-1 is up | +1 is down
  public static final int AXIS_RIGHT_TRIGGER = 4; //value 0 to -1f

  com.badlogic.gdx.controllers.Controller gdxController;

  public XBox360Controller(com.badlogic.gdx.controllers.Controller newController) {
    gdxController = newController;
  }

  public class EmulatedMouseCursor extends MouseCursor {
    @Override
    public InputEvent<?> poll() {
      Vector delta = XBox360Controller.this.getElementByClass(JoystickLeft.class).pos;
      delta.mulScalarInst(0.025f);
      Vector result = Vector.add(pos, delta);
      result.applyBoundsInst(new Vector(), new Vector(RpgGame.inst.xyRatio, 1));
      return setPos(result);
    }
  }

  public class EmulatedMouseButton1 extends MouseButton1 {
    @Override
    public InputEvent<?> poll() {
      return setIsActive(gdxController.getButton(BUTTON_A));
    }
  }

  public class EmulatedMouseButton2 extends MouseButton2 {
    @Override
    public InputEvent<?> poll() {
      return setIsActive(gdxController.getButton(BUTTON_B));
    }
  }

  public class RealStartButton extends StartButton {
    @Override
    public InputEvent<?> poll() {
      return setIsActive(gdxController.getButton(BUTTON_BACK));
    }
  }

  public class RealSelectButton extends SelectButton {
    @Override
    public InputEvent<?> poll() {
      return setIsActive(gdxController.getButton(BUTTON_START));
    }
  }

  public class RealJoystickLeft extends JoystickLeft {
    @Override
    public InputEvent<?> poll() {
      Vector rawPos = new Vector(gdxController.getAxis(AXIS_LEFT_X), gdxController.getAxis(AXIS_LEFT_Y));
      if(!Vector.absolute(rawPos).isEitherLarger(new Vector(0.2f))) rawPos = new Vector();
      return setPos(rawPos);
    }
  }

  public class RealJoystickRight extends JoystickRight {
    @Override
    public InputEvent<?> poll() {
      Vector rawPos = new Vector(gdxController.getAxis(AXIS_RIGHT_X), gdxController.getAxis(AXIS_RIGHT_Y));
      if(!Vector.absolute(rawPos).isEitherLarger(new Vector(0.2f))) rawPos = new Vector();
      return setPos(rawPos);
    }
  }

  public class RealButton1 extends Button1 {
    @Override
    public InputEvent<?> poll() {
      return setIsActive(gdxController.getButton(BUTTON_A));
    }
  }

  public class RealButton2 extends Button2 {
    @Override
    public InputEvent<?> poll() {
      return setIsActive(gdxController.getButton(BUTTON_B));
    }
  }

  public class RealButton3 extends Button3 {
    @Override
    public InputEvent<?> poll() {
      return setIsActive(gdxController.getButton(BUTTON_X));
    }
  }
}
