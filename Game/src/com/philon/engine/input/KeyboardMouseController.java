package com.philon.engine.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.philon.engine.PhilonGame;
import com.philon.engine.event.ButtonEvent;
import com.philon.engine.event.JoystickEvent;
import com.philon.engine.util.Vector;

public class KeyboardMouseController extends Controller {

  public class RealMouseCursor extends MouseCursor {
    @Override
    public JoystickEvent poll() {
      Vector screenPixSize = PhilonGame.inst.screenPixSize;
      Vector newPos = new Vector(Gdx.input.getX(), Gdx.input.getY()).divInst(new Vector(screenPixSize.y));
      return setPos(newPos); //newPos is <= (xyRatio, 1)
    }
  }

  public class RealMouseButton1 extends MouseButton1 {
    public int keyFor1 = Buttons.LEFT;

    @Override
    public ButtonEvent poll() {
      return setIsActive(Gdx.input.isButtonPressed(keyFor1));
    }
  }

  public class RealMouseButton2 extends MouseButton2 {
    public int keyFor2 = Buttons.RIGHT;

    @Override
    public ButtonEvent poll() {
      return setIsActive(Gdx.input.isButtonPressed(keyFor2));
    }
  }

  public class KeyboardStartButton extends StartButton {
    public int keyForStart = Keys.ENTER;

    @Override
    public ButtonEvent poll() {
      return setIsActive(Gdx.input.isKeyPressed(keyForStart));
    }
  }

  public class KeyboardSelectButton extends SelectButton {
    public int keyForSelect = Keys.E;

    @Override
    public ButtonEvent poll() {
      return setIsActive(Gdx.input.isKeyPressed(keyForSelect));
    }
  }

  public class WASDJoystick extends JoystickLeft {
    public int keyForUp = Keys.W;
    public int keyForDown = Keys.S;
    public int keyForLeft = Keys.A;
    public int keyForRigt = Keys.D;

    @Override
    public JoystickEvent poll() {
      Vector newDirection = new Vector();
      if( Gdx.input.isKeyPressed(keyForUp) ) {
        newDirection.y += -1;
      }
      if( Gdx.input.isKeyPressed(keyForDown) ) {
        newDirection.y += +1;
      }
      if( Gdx.input.isKeyPressed(keyForLeft) ) {
         newDirection.x += -1;
      }
      if( Gdx.input.isKeyPressed(keyForRigt) ) {
         newDirection.x += +1;
      }
      return setPos(newDirection.normalizeInst());
    }
  }

  public class MouseJoystick extends JoystickRight {
    @Override
    public JoystickEvent poll() { //returns normalized offset of cursor to screen center
//      if(!getElementByClass(RealMouseButton1.class).isActive) return setPos(new Vector());
//
//      Vector cursorPos = KeyboardMouseController.this.getElementByClass(RealMouseCursor.class).pos;
//      Vector charPos = RpgUtil.getScreenPosByTilePos( RpgGame.inst.getActiveUser().character.pos );
//
//      return setPos( Vector.sub(cursorPos, charPos).normalizeInst() );
      return setPos(new Vector());
    }
  }

  public class Button1Emulated extends Button1 {
    public int keyFor1 = Buttons.LEFT;

    @Override
    public ButtonEvent poll() {
      return setIsActive(Gdx.input.isButtonPressed(keyFor1));
    }
  }

  public class Button2Emulated extends Button2 {
    public int keyFor2 = Buttons.RIGHT;

    @Override
    public ButtonEvent poll() {
      return setIsActive(Gdx.input.isButtonPressed(keyFor2));
    }
  }

  public class ShiftKey extends Button3 {
    public int keyFor3 = Keys.SHIFT_LEFT;

    @Override
    public ButtonEvent poll() {
      return setIsActive(Gdx.input.isKeyPressed(keyFor3));
    }
  }

}
