package com.philon.engine.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimTextureRegion {
	public Texture texture;
	public Vector frameSize;
	public int numFrames;
	public TextureRegion[] frames;
	
	public AnimTextureRegion( Texture newTexture, Vector newFrameSize ) {
	  texture = newTexture;
	  if (newFrameSize.isAllEqual(new Vector())) {
	    frameSize = new Vector( texture.getWidth(), texture.getHeight());
	    frames = new TextureRegion[]{new TextureRegion(texture)};
	  } else {
	    frameSize = newFrameSize.copy();
	    frames = split(newTexture, newFrameSize);
	  }
	  numFrames = frames.length;
	}
	
	public TextureRegion[] split (Texture newTexture, Vector newFrameSize) {
    int width = newTexture.getWidth();
    int height = newTexture.getHeight();

    int rows = height / (int)newFrameSize.y;
    int cols = width / (int)newFrameSize.x;

    TextureRegion[] tiles = new TextureRegion[rows*cols];
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        tiles[(row*cols)+col] = new TextureRegion(texture, (int)(col*newFrameSize.x), (int)(row*newFrameSize.y), (int)(newFrameSize.x), (int)(newFrameSize.y));
      }
    }

    return tiles;
  }
	
}
