package com.philon.engine.forms;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.philon.engine.PhilonGame;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;

public abstract class AbstractForm {
  public int ID;

  public Vector pos;
  public Vector size;

  public AbstractGUIElement backgroundLabel;
  public LinkedHashMap<Class<?>, AbstractGUIElement> elements;

  public boolean isActive=false;

  /**
   * updates <b>size</b> first, then <b>pos</b>
   * @param newScreenSize
   */
  public AbstractForm() {
    loadGUIElements();
    screenSizeChangedTrigger(PhilonGame.screenPixSize);
  }

  public abstract Vector getPosByScreenSize(Vector newScreenSize);

  public abstract Vector getSizeByScreenSize(Vector newScreenSize);

  public void screenSizeChangedTrigger(Vector newScreenSize) {
    size = getSizeByScreenSize(newScreenSize);
    pos = getPosByScreenSize(newScreenSize);
  }

  public void activate() {
    if (isActive) return;
    PhilonGame.gForms.addForm(this);
    isActive = true;

  }

  public void deactivate() {
    if (!isActive) return;
    PhilonGame.gForms.removeForm(this);
    isActive = false;
  }

  public void toggle() {
    if (isActive) {
      deactivate();
    } else {
      activate();
    }
  }

  public void loadGUIElements() {
    try {
      elements = new LinkedHashMap<Class<?>, AbstractGUIElement>();
      Class<?> currClass = getClass();
      do {
        for (Class<?> tmpClass : currClass.getDeclaredClasses()) {
          if (AbstractGUIElement.class.isAssignableFrom(tmpClass)) {
            AbstractGUIElement newElement = (AbstractGUIElement) tmpClass.getDeclaredConstructor(new Class<?>[]{currClass}).newInstance(this);
            if (newElement.getClass().getSimpleName().equals("BackgroundLabel")) {
              backgroundLabel = newElement;
            } else {
              elements.put(tmpClass, newElement);
            }
          }
        }
        currClass = currClass.getSuperclass();
      } while(currClass!=AbstractForm.class);

    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.getTargetException().printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    }
  }

  public void draw() {
    backgroundLabel.draw(pos, size);
    for (AbstractGUIElement currElement : elements.values()) {
      currElement.draw(pos, size);
    }
  }

  public Vector handleMouse( Vector newMousePos, boolean isM1action, boolean isM2action ) {
    AbstractGUIElement currElement = getElementByPixel(newMousePos);
    RpgGame.inst.localPlayer.inv.hoveredOverItem = null;
    RpgGame.inst.gInput.currHoveredOverElement = currElement;
    Vector result = new Vector();
    if (currElement==null) {
      RpgGame.inst.gInput.currPressedElement = null;
      return result;
    }

    Vector relMousePixel = Vector.sub(newMousePos, getElementPixPos(currElement));
    if (currElement==RpgGame.inst.gInput.currPressedElement && !RpgGame.inst.gInput.m1downReal) {
      currElement.handleClickLeft(relMousePixel);
      RpgGame.inst.gInput.currPressedElement = null;
      result.x = 1;
    } else if (isM1action) {
      RpgGame.inst.gInput.currPressedElement = currElement;
    } else {
      RpgGame.inst.gInput.currPressedElement = null;
      currElement.handleMouseOver(relMousePixel);
    }
    if (isM2action) {
      currElement.handleClickRight(relMousePixel);
      result.y = 1;
    }

    return result;
  }

  public AbstractGUIElement getElementByPixel( Vector newPixel ) {
    Vector minPixel;
    Vector maxPixel;

    for (AbstractGUIElement currElement : elements.values()) {
      minPixel = getElementPixPos(currElement);
      maxPixel = Vector.add( minPixel, currElement.size );

      if( newPixel.isAllLarger( minPixel ) && newPixel.isAllSmaller( maxPixel ) ) {
        return currElement;
      }
    }

    return null;
  }

  public Vector getElementPixPos(AbstractGUIElement newElement) {
    return Vector.mul(newElement.pos, size);
  }

  public void update() {
    for (AbstractGUIElement currElement : elements.values()) {
      currElement.update();
    }
  }

  @SuppressWarnings("unchecked")
  public <T> ArrayList<T> getElementsOfType(Class<T> forClass) {
    ArrayList<T> result = new ArrayList<T>();
    for (AbstractGUIElement currElement : elements.values()) {
      if (currElement.getClass().isAssignableFrom(forClass)) {
        result.add((T) currElement);
      }
    }
    return result;
  }

  public AbstractGUIElement getElementByClass(Class<? extends AbstractGUIElement> forClass) {
    return elements.get(forClass);
  }

}
