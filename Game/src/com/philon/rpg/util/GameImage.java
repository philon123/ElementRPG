package com.philon.rpg.util;

import com.badlogic.gdx.graphics.Texture;
import com.philon.engine.util.AnimTextureRegion;
import com.philon.engine.util.Vector;
import com.philon.rpg.ImageData;

public class GameImage extends AnimTextureRegion {
  public Vector tileSize;
  
  public GameImage( Texture newTexture, Vector newFrameSize ) {
    super(newTexture, newFrameSize);
    
    float ratio = frameSize.y / frameSize.x;
    tileSize = ImageData.defaultImgSize.copy().mulInst(new Vector(1, ratio));
  }
  
}
