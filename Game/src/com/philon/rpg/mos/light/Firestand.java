package com.philon.rpg.mos.light;

import com.philon.engine.util.Vector;
import com.philon.rpg.map.mo.UpdateMapObj;

public class Firestand extends UpdateMapObj {

	public Firestand() {
	  super();

		setLuminance(0.33f);
		isSelectable = false;
	}

  @Override
  public Vector getCollRect() {
    return new Vector(0.3f);
  }

  @Override
  public int getImgIdle() {
    return 340;
  }

  @Override
  public float getTilesPerSecond() {
    return 0;
  }

  @Override
  public int getImgMoving() {
    return 0;
  }

  @Override
  public int getImgDying() {
    return 0;
  }

  @Override
  public int getSouDie() {
    return 0;
  }

}
