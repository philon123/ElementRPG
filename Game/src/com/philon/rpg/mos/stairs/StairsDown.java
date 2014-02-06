package com.philon.rpg.mos.stairs;

import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.mo.Selectable;
import com.philon.rpg.mo.StaticMapObj;
import com.philon.rpg.mo.UpdateMapObj;

public class StairsDown extends StaticMapObj implements Selectable {
  
  @Override
	public int getImage() {
    return 336;
  }

  @Override
	public Vector getCollRect() {
	  return new Vector(1, 1);
	}
	
  @Override
  public void interactTrigger(UpdateMapObj objInteracting) {
    RpgGame.inst.changeLevel( RpgGame.inst.currLevel+1 );
  }

}
