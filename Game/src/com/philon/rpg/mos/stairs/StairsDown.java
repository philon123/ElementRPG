package com.philon.rpg.mos.stairs;

import com.philon.rpg.RpgGame;
import com.philon.rpg.map.mo.RpgMapObj;

public class StairsDown extends AbstractStairs {

  @Override
	public int getImage() {
    return 336;
  }

  @Override
  public void interactTrigger(RpgMapObj objInteracting) {
    RpgGame.inst.changeLevel( RpgGame.inst.currLevel+1 );
  }

}
