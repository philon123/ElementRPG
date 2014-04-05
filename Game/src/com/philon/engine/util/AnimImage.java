package com.philon.engine.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimImage {
  public int imgIndex;
	public Texture texture;
	public Vector frameSize;
	public float xyRatio;
	public int numFrames;
	public TextureRegion[] frames;
	public Vector dimensions;
	public float scaleByX;

	public AnimImage( int newImgIndex, Texture newTexture, Vector newFrameSize, float newScaleByX ) throws IllegalArgumentException {
	  imgIndex = newImgIndex;
	  Vector newTextureSize = new Vector(newTexture.getWidth(), newTexture.getHeight());
	  if (newFrameSize.isAllEqual(new Vector())) {
	    newFrameSize = newTextureSize;
	  }
	  Vector newTileSize = Vector.div(newTextureSize, newFrameSize);
	  if( ((int)newTileSize.x)-newTileSize.x!=0 && ((int)newTileSize.y)-newTileSize.y!=0 ) {
	    throw new IllegalArgumentException("FrameSize " + newTileSize.toStringIntRounded() +
	        " doesn't divide the texture size " + newTextureSize.toStringIntRounded() + " cleanly. ");
	  }
	  texture = newTexture;
	  frameSize = newFrameSize.copy();
	  xyRatio = frameSize.x / frameSize.y;
	  frames = split(newFrameSize);
	  numFrames = frames.length;
	  scaleByX = newScaleByX;
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

}
