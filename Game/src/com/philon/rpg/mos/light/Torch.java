package com.philon.rpg.mos.light;

import com.philon.engine.util.Vector;
import com.philon.rpg.mo.UpdateMapObj;

public class Torch extends UpdateMapObj {

	public Torch() {
	  isCollObj=false;
		setLuminance(0.5f);
	}
	
	@Override
	public int getImgIdle() {
	  return 339;
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

  @Override
  public Vector getCollRect() {
    return new Vector();
  }

}
