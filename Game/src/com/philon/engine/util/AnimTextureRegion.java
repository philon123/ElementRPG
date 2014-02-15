package com.philon.engine.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimTextureRegion {
	public Texture texture;
	public Vector frameSize;
	public int numFrames;
	public TextureRegion[] frames;
	public Vector dimensions;

	public AnimTextureRegion( Texture newTexture, Vector newFrameSize ) {
	  texture = newTexture;
	  if (newFrameSize.isAllEqual(new Vector())) {
	    frameSize = new Vector( texture.getWidth(), texture.getHeight());
	    frames = new TextureRegion[]{new TextureRegion(texture)};
	    dimensions = new Vector(1);
	  } else {
	    frameSize = newFrameSize.copy();
	    split(newFrameSize);
	  }
	  numFrames = frames.length;
	}

	public void split(Vector newFrameSize) {
	  Vector pixSize = new Vector(texture.getWidth(), texture.getHeight());
	  dimensions = pixSize.copy().divInst(newFrameSize.copy().floorAllInst()).floorAllInst();

    TextureRegion[] tiles = new TextureRegion[(int) (dimensions.x*dimensions.y)];
    for (int row = 0; row < dimensions.y; row++) {
      for (int col = 0; col < dimensions.x; col++) {
        tiles[(row*((int)dimensions.x))+col] = new TextureRegion(texture, (int)(col*newFrameSize.x), (int)(row*newFrameSize.y), (int)(newFrameSize.x), (int)(newFrameSize.y));
      }
    }

    frames = tiles;
  }

}
