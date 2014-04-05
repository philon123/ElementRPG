package com.philon.engine.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import javax.management.RuntimeErrorException;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.philon.engine.Data;
import com.philon.engine.event.InputEvent;
import com.philon.engine.event.InputListener;
import com.philon.engine.input.AbstractController.ControllerElement;
import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;

public abstract class GuiElement {
  public static final int ALIGN_NONE         = 0;
  public static final int ALIGN_CENTER       = 1;
  public static final int ALIGN_TOP_LEFT     = 2;
  public static final int ALIGN_TOP_RIGHT    = 3;
  public static final int ALIGN_BOTTOM_LEFT  = 4;
  public static final int ALIGN_BOTTOM_RIGHT = 5;
  public static final int ALIGN_TOP          = 6;
  public static final int ALIGN_BOTTOM       = 7;
  public static final int ALIGN_LEFT         = 8;
  public static final int ALIGN_RIGHT        = 9;

  private TreeMap<Integer, ArrayList<GuiElement>> orderToElementsMap /** higher order is tested for selection first. */
      = new TreeMap<Integer, ArrayList<GuiElement>>();
  private HashMap<Class<? extends ControllerElement>, InputListener<? extends ControllerElement>> elementToListenerMap
      = new HashMap<Class<? extends ControllerElement>, InputListener<? extends ControllerElement>>();

  protected GuiElement parent = null;

  public Vector absPos, absSize;
  public float xyRatio;

  public GuiElement() {
    loadElements();
  }

  /**
   * Strechable: Implement getConfiguredSize(). This area is proportional to the container.
   * <p/>
   * Not Strechable: Implement getConfiguredXYRatio() and getConfiguredScale() to define bounds with a fixed width to height ratio.
   * The scale represents the relative size of the y-axis (can be changed by overriding isScaleByX())
   */
  protected abstract boolean isStrechable();

  protected Vector getConfiguredSize() {
    throw new Error(getClass().getSimpleName() + " has to implement getConfiguredSize()!");
  }
  protected float getConfiguredXYRatio() {
    throw new Error(getClass().getSimpleName() + " has to implement getConfiguredXYRatio()!");
  }
  protected float getConfiguredScale() {
    throw new Error(getClass().getSimpleName() + " has to implement getConfiguredScale()!");
  }
  protected boolean isScaleByX() {
    return false;
  }
  /** Default: relative to upper left corner, but configuring alignment will add coordinates inversely to the alignment*/
  protected Vector getConfiguredPosition() {
    return new Vector();
  }
  protected int getConfiguredAlignment() {
    return ALIGN_NONE;
  }
  protected int getConfiguredBackground() {
    return 0;
  }

  protected void execDraw(SpriteBatch batch) {
    int tmpImage = getConfiguredBackground();
    if(tmpImage!=0) {
      drawRelative(batch, Data.textures.get(tmpImage).frames[0], new Vector(), new Vector(1));
    }
  }
  protected void execDrawOverChildren(SpriteBatch batch) {
  }
  protected boolean getConfiguredIsSelectable() {
    return true;
  }
  public void execSelected() {
  }
  public void execDeselected() {
  }
  public void execCursorEnter() {
  }
  public void execCursorExit() {
  }
  protected void execRemoved() {
  }

  private void loadElements() {
    for (Class<? extends GuiElement> currentClass : Util.getClassHierarchy(getClass(), GuiElement.class) ) {
      for (Class<?> newClass : currentClass.getDeclaredClasses()) {
        if(!GuiElement.class.isAssignableFrom(newClass)) continue;
        Class<? extends GuiElement> newClassCasted = newClass.asSubclass(GuiElement.class);
        insertElementInit(Util.instantiateClass(newClassCasted, this));
      }
    }
  }

  protected void insertElementInit(GuiElement newElement) { //doesn't set the container transform, as it might not be set yet
    int newOrder = Util.getOrderForClass(newElement.getClass());
    ArrayList<GuiElement> currList = orderToElementsMap.get(newOrder);
    if(currList==null) {
      orderToElementsMap.put(newOrder, new ArrayList<GuiElement>());
      currList = orderToElementsMap.get(newOrder);
    }
    currList.add(newElement);
  }

  public void insertElement(GuiElement newElement) {
    insertElementInit(newElement);
    newElement.setParent(this);
  }

  public void removeElement(GuiElement newElement) {
    if(newElement==null) return;
    int newOrder = Util.getOrderForClass(newElement.getClass());
    ArrayList<GuiElement> orderList = orderToElementsMap.get(newOrder);
    if(orderList.remove(newElement)) {
      newElement.execRemoved();
      newElement.setParent(null);
    }
    if(orderList.isEmpty()) {
      orderToElementsMap.remove(newOrder);
    }
  }

  public void removeElementByClass(Class<? extends GuiElement> newElement) {
    removeElement(getElementByClass(newElement));
  }

  /**
   * @returns The FIRST(!) occurance of an element of this class in the children, null if none exist.
   */
  @SuppressWarnings("unchecked")
  public <T extends GuiElement> T getElementByClass(Class<T> clazz) {
    Iterator<GuiElement> childrenInOrder = getChildrenInOrder(true);
    while(childrenInOrder.hasNext()) {
      GuiElement currEle = childrenInOrder.next();
      if( clazz.isAssignableFrom(currEle.getClass()) ) {
        return (T) currEle;
      }
    }
    return null;
  }

  public Iterator<GuiElement> getChildrenInOrder(final boolean isAscending) {
    return new Iterator<GuiElement>() {
      Iterator<ArrayList<GuiElement>> listItr;
      Iterator<GuiElement> eleItr = null;
      {
        if(isAscending) {
          listItr = orderToElementsMap.values().iterator();
        } else {
          listItr = orderToElementsMap.descendingMap().values().iterator();
        }
      }

      @Override
      public boolean hasNext() {
        do {
          if(eleItr==null || !eleItr.hasNext()) {
            if(!listItr.hasNext()) return false;
            eleItr = listItr.next().iterator();
          } else {
            return true;
          }
        } while(true);
      }
      @Override
      public GuiElement next() {
        return eleItr.next();
      }
      @Override
      public void remove() {
        eleItr.remove();
      }
    };
  }

  //when mouse is clicked, activate clicked element and handle event
  //events are delegated to activatedElement (recursive). if this doesn't handle it, pass the event back up the hierarchy
  protected <T extends ControllerElement> void addInputListener(Class<T> forElement, InputListener<? super T> newListener) {
    if(elementToListenerMap.containsKey(forElement)) throw new RuntimeErrorException(
        new Error( getClass().getSimpleName() + " tried to overwrite input listener for " + forElement.getSimpleName()) );
    elementToListenerMap.put(forElement, newListener);
  }

  protected void removeInputListener(Class<? extends ControllerElement> forElement) {
    if(!elementToListenerMap.containsKey(forElement)) throw new RuntimeErrorException(
        new Error(
            getClass().getSimpleName() + " tried to remove input listener for " +
            forElement.getSimpleName() + " at " + ", but it does not exist!")
        );
    elementToListenerMap.remove(forElement);
  }

  /**
   * params may assume a square coordinate system. ( (1, 1) will be the full size )
   */
  protected void drawRelative(SpriteBatch batch, TextureRegion image, Vector newPos, Vector newSize) {
    Vector newAbsPos = Vector.mul(newPos, absSize).addInst(absPos);
    Vector newAbsSize = Vector.mul(newSize, absSize);
    batch.draw(image, newAbsPos.x, newAbsPos.y, newAbsSize.x, newAbsSize.y);
  }

  /**
   * params may assume a uniform coordinate system. ( (1, 1) will be a square )
   */
  protected void drawNormalized(SpriteBatch batch, TextureRegion image, Vector newPos, Vector newSize) {
    Vector newAbsPos = Vector.mul(new Vector(newPos.x/xyRatio, newPos.y), absSize).addInst(absPos);
    batch.draw(image, newAbsPos.x, newAbsPos.y, newSize.x, newSize.y);
  }

  protected Vector getRelPosByAbsPos(Vector newAbsPos) {
    return Vector.sub(newAbsPos, absPos).divInst(absSize);
  }

  protected boolean isRectVisible( Vector newPos, Vector newSize ) {
    if( newPos.isAllSmaller(new Vector(xyRatio, 1)) && Vector.add(newPos, newSize).isAllLarger(new Vector()) ) {
      return true;
    }
    return false;
  }

  /*
   * #############################
   * ############################# HIERARCHY METHODS
   * #############################
   */

  public void setParent(GuiElement newParent) {
    parent = newParent;
    if(parent!=null) setContainerTransform(parent.absPos, parent.absSize);
  }

  public void setContainerTransform(Vector containerAbsPos, Vector containerAbsSize) {
    Vector relPos, relSize; //transform relative to container

    if(isStrechable()) {
      relSize = getConfiguredSize();
    } else {
      float newScale = getConfiguredScale();
      if(isScaleByX()) {
        relSize = new Vector(newScale, newScale/getConfiguredXYRatio());
      } else {
        float containerXYRatio = containerAbsSize.x / containerAbsSize.y;
        relSize = new Vector((getConfiguredXYRatio() * newScale) / containerXYRatio, newScale);
      }
    }

    relPos = new Vector();
    Vector confPos = getConfiguredPosition();
    switch (getConfiguredAlignment()) {
      case ALIGN_NONE :
        relPos = confPos;
        break;
      case ALIGN_CENTER :
        relPos = new Vector(0.5f).subInst( Vector.mul(relSize, new Vector(0.5f)) );
        break;
      case ALIGN_TOP :
        relPos.y = 0 - confPos.y;
        break;
      case ALIGN_BOTTOM :
        relPos.y = 1-relSize.y - confPos.y;
        break;
      case ALIGN_LEFT :
        relPos = confPos;
        break;
      case ALIGN_RIGHT :
        relPos.x = 1-relSize.x - confPos.x;
        break;
      case ALIGN_TOP_LEFT :
        relPos = confPos;
        break;
      case ALIGN_TOP_RIGHT :
        relPos = new Vector(1-relSize.x - confPos.x, confPos.y);
        break;
      case ALIGN_BOTTOM_LEFT :
        relPos = new Vector(confPos.x, 1-relSize.y - confPos.y);
        break;
      case ALIGN_BOTTOM_RIGHT :
        relPos = new Vector(1-relSize.x - confPos.x, 1-relSize.y - confPos.y);
        break;
    }

    absPos = Vector.add( containerAbsPos, Vector.mul(containerAbsSize, relPos) );
    absSize = Vector.mul( containerAbsSize, relSize );
    xyRatio = absSize.x / absSize.y;

    Iterator<GuiElement> childrenInOrder = getChildrenInOrder(true);
    while(childrenInOrder.hasNext()) {
      GuiElement currEle = childrenInOrder.next();
      currEle.setParent(this);
    }
  }

  public GuiElement getElementByAbsPos(Vector newAbsPos) {
    Iterator<GuiElement> childrenInOrder = getChildrenInOrder(false);
    while(childrenInOrder.hasNext()) {
    GuiElement currEle = childrenInOrder.next();
    if(currEle.getConfiguredIsSelectable()) {
        if(currEle.absPos.isAllSOE(newAbsPos) && Vector.add(currEle.absSize, currEle.absPos).isAllLOE(newAbsPos)) {
          return currEle.getElementByAbsPos( newAbsPos );
        }
      }
    }
    return this; //no suitable elements. this is bottom most element
  }

  /**
   * Will delegate to selectedChild, if that doesn't exist or doesn't handle event, this will try to handle the event.
   * @Returns true if the event was handled, false if not.
   */
  public boolean handleEvent(InputEvent<? extends ControllerElement> newEvent) {
    if(!elementToListenerMap.isEmpty()) {
      Iterator<Class<? extends ControllerElement>> descIter
          = Util.getClassHierarchy(newEvent.source.getClass(), ControllerElement.class).descendingIterator();
      while(descIter.hasNext()) {
        InputListener<? extends ControllerElement> inputListener = elementToListenerMap.get(descIter.next());
        if(inputListener==null) continue;
        if(inputListener.handleEvent(newEvent)) return true;
      }
    }
    if(parent!=null) return parent.handleEvent(newEvent);
    return false;
  }

  public void draw(SpriteBatch batch) {
    execDraw(batch);

    Iterator<GuiElement> childrenInOrder = getChildrenInOrder(true);
    while(childrenInOrder.hasNext()) {
      GuiElement currEle = childrenInOrder.next();
      currEle.draw(batch);
    }
    execDrawOverChildren(batch);
  }

}
