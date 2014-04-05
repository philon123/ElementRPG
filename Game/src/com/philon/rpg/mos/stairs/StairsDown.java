package com.philon.rpg.mos.stairs;

import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.util.RpgUtil;

public class StairsDown extends AbstractStairs {

  @Override
	public int getImage() {
    return 336;
  }

  @Override
  public void interactTrigger(RpgMapObj objInteracting) {
    RpgUtil.changeLevel(1);
  }

}
