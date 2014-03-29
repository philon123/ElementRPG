package com.philon.engine;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.philon.engine.forms.AbstractForm;
import com.philon.engine.forms.AbstractGUIElement;
import com.philon.engine.user.UserAction;
import com.philon.engine.util.Vector;

public class Input {
  public Vector realMousePos;
  public boolean m1downReal;
  public boolean m2downReal;
  public boolean m1actionPerformed;
  public boolean m2actionPerformed;

  public int leftForm = -1;
  public int rightForm = -1;
  public AbstractGUIElement currPressedElement = null;
  public AbstractGUIElement currHoveredOverElement = null;

  public boolean isSelectingLeft;

  public LinkedList<Integer> keysWatched;
  public LinkedHashMap<Integer, Boolean> keysIsPressed = new LinkedHashMap<Integer, Boolean>();
  public LinkedHashMap<Integer, Boolean> keysIsReleased = new LinkedHashMap<Integer, Boolean>();

  public Input() {
    //hotkeys
    keysWatched = new LinkedList<Integer>();
  }

  public LinkedList<UserAction> getUserInput() {
    LinkedList<UserAction> result = new LinkedList<UserAction>();

    //hotkeys
    for(int currKey : keysWatched) {
      Boolean tmp = keysIsPressed.get(currKey);
      boolean wasKeyPressed = tmp==null ? false : tmp;
      boolean isKeyPressed = Gdx.input.isKeyPressed(currKey);

      if (wasKeyPressed && !isKeyPressed) {
        keysIsReleased.put(currKey, true);
      } else {
        keysIsReleased.put(currKey, false);
      }
      keysIsPressed.put(currKey, isKeyPressed);
    }

    return result;
  }

  public void handleUserInput() {
    //hotkeys
    for(int currKey : keysWatched) {
      Boolean tmp;
      tmp = keysIsPressed.get(currKey);
      boolean wasKeyPressed = tmp==null ? false : tmp;
      boolean isKeyPressed = Gdx.input.isKeyPressed(currKey);

      if (wasKeyPressed && !isKeyPressed) {
        keysIsReleased.put(currKey, true);
      } else {
        keysIsReleased.put(currKey, false);
      }
      keysIsPressed.put(currKey, isKeyPressed);
    }

    //mouse
    realMousePos = new Vector(Gdx.input.getX(), Gdx.input.getY());
    m1downReal=Gdx.input.isButtonPressed(Buttons.LEFT);
    m2downReal=Gdx.input.isButtonPressed(Buttons.RIGHT);
    if (m1actionPerformed && ! m1downReal) m1actionPerformed=false;
    if (m2actionPerformed && ! m2downReal) m2actionPerformed=false;

    boolean isMouseOverForm=false;
    for (int i=0; i<PhilonGame.gForms.activeForms.size(); i++) {
      AbstractForm currForm = PhilonGame.gForms.activeForms.get(i);
      if (realMousePos.isAllLOE(currForm.pos) && realMousePos.isAllSOE(Vector.add(currForm.pos, currForm.size))) {
        handleMouseOverForm( currForm, realMousePos.copy().subInst(currForm.pos) );
        isMouseOverForm=true;
        break;
      }
    }

    if(!isMouseOverForm) handleMouseOverGame( realMousePos, m1downReal, m2downReal );
  }

  public void handleMouseOverGame( Vector newMousePos, boolean isM1down, boolean isM2down ) {
  }

  public void handleMouseOverForm( AbstractForm currForm, Vector newMousePos ) {
    Vector execActions = currForm.handleMouse(
        newMousePos,
        m1downReal && !m1actionPerformed,
        m2downReal && !m2actionPerformed);

    if (execActions.x==1) m1actionPerformed=true;
    if (execActions.y==1) m2actionPerformed=true;
  }

  //----------


}
