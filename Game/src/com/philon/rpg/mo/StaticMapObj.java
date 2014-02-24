package com.philon.rpg.mo;

import com.philon.engine.FrameAnimation;
import com.philon.rpg.ImageData;

public abstract class StaticMapObj extends RpgMapObj {

  public StaticMapObj() {
    super();

    setAnimation(new FrameAnimation(ImageData.images[getImage()]));
  }

  public abstract int getImage();

}
