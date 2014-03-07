package com.philon.rpg.mos.stairs;

import com.philon.engine.util.Vector;
import com.philon.rpg.map.mo.StaticMapObj;

public abstract class AbstractStairs extends StaticMapObj {

  @Override
  public Vector getCollRect() {
    return new Vector(1, 1);
  }

  public Vector getSpawnOffset() {
    return new Vector(1);
  }

}

