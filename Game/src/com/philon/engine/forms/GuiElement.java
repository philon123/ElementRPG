package com.philon.engine.forms;

import java.util.TreeMap;

import com.philon.engine.util.Util;
import com.philon.engine.util.Util.OrderComperator;
import com.philon.engine.util.Vector;

public abstract class GuiElement {
  public abstract Vector getPosition();
  public abstract Vector getSize();
  public abstract int getImage();
  public TreeMap<Class<? extends GuiElement>, GuiElement> elements;

  public GuiElement() {
    loadElements();
  }

  @SuppressWarnings("unchecked")
  private void loadElements() {
    new OrderComperator();
    elements = new TreeMap<Class<? extends GuiElement>, GuiElement>(new OrderComperator());

    for (Class<? extends GuiElement> currentClass : Util.getClassHierarchy(getClass(), GuiElement.class) ) {
      for (Class<?> newClass : currentClass.getDeclaredClasses()) {
        if ( !GuiElement.class.isAssignableFrom(newClass) ) continue;
        elements.put(
            (Class<? extends GuiElement>)newClass,
            (GuiElement)Util.instantiateClass(newClass, new Object[]{(Object)this})
        );
      }
    }
  }

  @SuppressWarnings("unchecked")
  public <T> T getElementByClass(Class<? extends T> clazz) {
    return (T) (elements.get(clazz));
  }

  public void draw(Vector absPos, Vector absSize) {
    for(GuiElement currEle : elements.values()) {
      Vector newPos = Vector.add( absPos, Vector.mul(absSize, currEle.getPosition()) );
      Vector newSize = Vector.mul( absSize, currEle.getSize() );
      currEle.draw( newPos, newSize );
    }
  }

  public GuiElement getElement(Vector relPos) {
    if(elements.isEmpty()) return this; //this is bottom most element
    for(GuiElement currEle : elements.values()) { //search for hit element and if found -> delegate
      if(currEle.getPosition().isAllSOE(relPos) && Vector.add(currEle.getSize(), currEle.getPosition()).isAllLOE(relPos)) {
        return currEle.getElement( Vector.div(Vector.sub(relPos, currEle.getPosition()), currEle.getSize()) );
      }
    }
    return this; //no suitable elements. this is bottom most element
  }

}
