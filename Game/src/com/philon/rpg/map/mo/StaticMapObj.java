package com.philon.rpg.map.mo;

import com.philon.engine.Data;
import com.philon.engine.FrameAnimation;

public abstract class StaticMapObj extends RpgMapObj {

  public StaticMapObj() {
    super();

    setAnimation(new FrameAnimation(Data.textures.get(getImage())));
  }

  public abstract int getImage();

}
