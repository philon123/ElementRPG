package com.philon.engine.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimImage {
  public int imgIndex;
  private Texture texture;
  private Vector frameSize;
  private float xyRatio;
  private TextureRegion[] frames;
  private Vector dimensions;
  private float defaultScale;
  private Vector originRelative;

  public AnimImage(int newImgIndex, Texture newTexture, Vector newFrameSize, float newScale, Vector newOrigin) {
    imgIndex = newImgIndex;
    Vector newTextureSize = new Vector(newTexture.getWidth(), newTexture.getHeight());
    if (newFrameSize.isAllEqual(new Vector())) {
      newFrameSize = newTextureSize;
    }
    Vector newTileSize = Vector.div(newTextureSize, newFrameSize);
    if( ((int)newTileSize.x)-newTileSize.x!=0f && ((int)newTileSize.y)-newTileSize.y!=0f ) {
      throw new IllegalArgumentException("FrameSize " + newTileSize.toStringIntRounded() +
          " doesn't divide the texture size " + newTextureSize.toStringIntRounded() + " cleanly. ");
    }
    texture = newTexture;
    frameSize = newFrameSize.copy();
    xyRatio = frameSize.x / frameSize.y;
    frames = split(newFrameSize);
    defaultScale = newScale;
    originRelative = Vector.div(newOrigin, newFrameSize);
  }

  public int getFramesInAnimation() {
    return (int)dimensions.x;
  }

  public int getDirectionsInAnimation() {
    return (int)dimensions.y;
  }

  public Vector getFrameSize() {
    return frameSize.copy();
  }

  private TextureRegion[] split(Vector newFrameSize) {
    Vector pixSize = new Vector(texture.getWidth(), texture.getHeight());
    dimensions = pixSize.copy().divInst(newFrameSize.copy().floorAllInst()).floorAllInst();

    TextureRegion[] tiles = new TextureRegion[(int) (dimensions.x*dimensions.y)];
    for (int row = 0; row < dimensions.y; row++) {
      for (int col = 0; col < dimensions.x; col++) {
        tiles[(row*((int)dimensions.x))+col] = createTextureRegion(texture, Vector.mul(newFrameSize, new Vector(col, row)), newFrameSize);
      }
    }

    return tiles;
  }

  private TextureRegion createTextureRegion(Texture newTex, Vector newPos, Vector newSize) {
    TextureRegion newTR = new TextureRegion(newTex, (int)newPos.x, (int)newPos.y, (int)newSize.x, (int)newSize.y);
    newTR.flip(false, true);
    return newTR;
  }

  /**
   * forces given position for topleft corner and streches image if necessary to fit size.
   */
  public void draw(Batch batch, Vector newPos, Vector newSize, int newFrame) {
    batch.draw(frames[newFrame], newPos.x, newPos.y, newSize.x, newSize.y);
  }

  /**
   * origin of image will be drawn at the given position. image default scale is applied.
   */
  public void draw(Batch batch, Vector newPos, float newScale, int newFrame) {
    Vector newSize = new Vector(1, 1f/xyRatio).mulScalarInst(defaultScale*newScale);
    Vector originOffset = Vector.mul(newSize, originRelative);
    draw(batch, Vector.sub(newPos, originOffset), newSize, newFrame);
  }

}
