package com.philon.rpg.mos.stairs;

import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.util.RpgUtil;

public class StairsUp extends AbstractStairs {

  public int getImage() {
    return 335;
  }

  @Override
  public void interactTrigger(RpgMapObj objInteracting) {
    RpgUtil.changeLevel(-1);
  }

}
