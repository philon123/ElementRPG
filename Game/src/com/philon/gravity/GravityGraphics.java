package com.philon.gravity;

import java.util.ArrayList;

import com.philon.engine.Graphics;
import com.philon.engine.PhilonGame;
import com.philon.engine.util.Vector;

public class GravityGraphics extends Graphics {
  float currScale = 1;
  Vector currOffset = new Vector();

  public GravityGraphics() {
  }

  public Vector getScreenPosByMapPos(Vector newMapPos) {
    return applyOffset(applyScale(newMapPos));
  }

  public Vector getMapPosByScreenPos(Vector newScreenPos) {
    return deapplyScale(deapplyOffset(newScreenPos));
  }

  public Vector getScreenSizeByMapSize(Vector newMapSize) {
    return applyScale(newMapSize);
  }

  public Vector applyOffset(Vector withoutOffset) {
    return Vector.add(withoutOffset, currOffset);
  }

  public Vector deapplyOffset(Vector withOffset) {
    return Vector.sub(withOffset, currOffset);
  }

  public Vector applyScale(Vector forScaling) {
    return Vector.mulScalar(forScaling, currScale);
  }

  public Vector deapplyScale(Vector forDescaling) {
    return Vector.divScalar(forDescaling, currScale);
  }

  public Vector getScreenSize() {
    return new Vector(PhilonGame.ratioXY, 1);
  }

  public void updateScaling() {
    Vector currRectSize = getRectSize(GravityScreen.currMap.massObjects).addInst(new Vector(0.1f));
    Vector reqScaling = getScreenSize().copy().divInst(currRectSize);
    float smallerValue = reqScaling.x < reqScaling.y ? reqScaling.x : reqScaling.y;
    if (smallerValue>1f) smallerValue=1f; //minimum scale, stops rediculus zooming
    currScale = smallerValue;

    Vector newOffset = Vector.mulScalar(getMidpoint(GravityScreen.currMap.massObjects), -currScale);
    currOffset = currOffset.addInst(newOffset).divScalarInst(2); //smooth camera
  }

  public Vector getMidpoint(ArrayList<MassObject> objects) {
    Vector min = new Vector(Float.POSITIVE_INFINITY);
    Vector max = new Vector(Float.NEGATIVE_INFINITY);
    for (MassObject currObject : objects) {
      if (currObject.getPosition().x < min.x) min.x = currObject.getPosition().x;
      if (currObject.getPosition().y < min.y) min.y = currObject.getPosition().y;
      if (currObject.getPosition().x > max.x) max.x = currObject.getPosition().x;
      if (currObject.getPosition().y > max.y) max.y = currObject.getPosition().y;
    }
    return new Vector((max.x + min.x)/2f, (max.y + min.y)/2f);
  }

  public Vector getRectSize(ArrayList<MassObject> objects) {
    Vector min = new Vector(Float.POSITIVE_INFINITY);
    Vector max = new Vector(Float.NEGATIVE_INFINITY);
    for (MassObject currObject : objects) {
      if (currObject.getPosition().x < min.x) min.x = currObject.getPosition().x;
      if (currObject.getPosition().y < min.y) min.y = currObject.getPosition().y;
      if (currObject.getPosition().x > max.x) max.x = currObject.getPosition().x;
      if (currObject.getPosition().y > max.y) max.y = currObject.getPosition().y;
    }
    return new Vector(max.x - min.x, max.y - min.y);
  }

}
