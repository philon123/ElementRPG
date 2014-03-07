package com.philon.rpg.mos.stairs;

import com.philon.rpg.RpgGame;
import com.philon.rpg.map.mo.RpgMapObj;

public class StairsUp extends AbstractStairs {

  public int getImage() {
    return 335;
  }

  @Override
  public void interactTrigger(RpgMapObj objInteracting) {
    RpgGame.inst.changeLevel( RpgGame.inst.currLevel-1 );
  }

}
