package com.philon.gravity;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.philon.engine.Graphics;
import com.philon.engine.util.Vector;

public class GravityGraphics extends Graphics {
  float currScale = 1;
  Vector currPixOffset = new Vector();

  public Vector getPixPosByMapPos(Vector newMapPos) {
    return getPixSizeByMapSize(newMapPos).subInst(currPixOffset);
  }

  public Vector getPixSizeByMapSize(Vector newMapSize) {
    return newMapSize.copy().mulScalarInst(currScale);
  }

  public Vector getMapPosByPixPos(Vector newPixPos) {
    return null; //TODO input translation
  }

  public void updateScaling() {
    Vector currRectSize = getRectSize(GravityGame.currMap.massObjects).mulScalarInst(1.2f);
    Vector reqScaling = GravityGame.getScreenPixSize().copy().divInst(currRectSize);
    float smallerValue = reqScaling.x < reqScaling.y ? reqScaling.x : reqScaling.y;
    if (smallerValue>1) smallerValue=1;
    currScale = smallerValue;

    Vector centeredPixel = getPixSizeByMapSize(getMidpoint(GravityGame.currMap.massObjects));
    Vector newOffset = centeredPixel.subInst(GravityGame.getScreenPixSize().copy().mulScalarInst(0.5f));
    currPixOffset = currPixOffset.addInst(newOffset).divScalarInst(2); //smooth camera
  }

  public void drawAll() {
    for (MassObject currObject : GravityGame.currMap.massObjects) {
      Vector imgSize = getPixSizeByMapSize(new Vector(currObject.radius*2));
      Vector newPos = getPixPosByMapPos(currObject.pos);
      if (currObject.radiance>0) { //draw glow first
        Vector glowSize = Vector.mulScalar(imgSize, 1+currObject.radiance).mulScalarInst(2);
        Color newGlowColor = new Color(0.9f, 0.15f, 0.15f, 1f);
        drawTextureCentered(GravityGame.data.textures.get(Data.IMG_GLOW).frames[0], newPos, glowSize, newGlowColor );
      }
      drawTextureCentered(currObject.animation.image.frames[0], newPos, imgSize, Color.WHITE); //draw object

    }
  }

  public Vector getMidpoint(ArrayList<MassObject> objects) {
    Vector min = new Vector(Float.POSITIVE_INFINITY);
    Vector max = new Vector(Float.NEGATIVE_INFINITY);
    for (MassObject currObject : objects) {
      if (currObject.pos.x < min.x) min.x = currObject.pos.x;
      if (currObject.pos.y < min.y) min.y = currObject.pos.y;
      if (currObject.pos.x > max.x) max.x = currObject.pos.x;
      if (currObject.pos.y > max.y) max.y = currObject.pos.y;
    }
    return new Vector((max.x + min.x)/2f, (max.y + min.y)/2f);
  }

  public Vector getRectSize(ArrayList<MassObject> objects) {
    Vector min = new Vector(Float.POSITIVE_INFINITY);
    Vector max = new Vector(Float.NEGATIVE_INFINITY);
    for (MassObject currObject : objects) {
      if (currObject.pos.x < min.x) min.x = currObject.pos.x;
      if (currObject.pos.y < min.y) min.y = currObject.pos.y;
      if (currObject.pos.x > max.x) max.x = currObject.pos.x;
      if (currObject.pos.y > max.y) max.y = currObject.pos.y;
    }
    return new Vector(max.x - min.x, max.y - min.y);
  }

  public void drawTextureCentered(TextureRegion texture, Vector newPos, Vector newSize, Color tint) {
    drawTextureRect(
        texture,
        Vector.sub(newPos, newSize.copy().mulScalarInst(0.5f)),
        newSize,
        tint
        );
  }
}
