package com.philon.rpg.mos.door;

import com.philon.engine.util.Vector;
import com.philon.rpg.mo.ToggleMapObj;

public abstract class AbstractDoor extends ToggleMapObj {

	public AbstractDoor() {
	  super();
	  
	  replaceState(StateOpen.class, StateDoorOpen.class);
		replaceState(StateClosed.class, StateDoorClosed.class);
		
		turnToDirection( Math.random()<0.5 ? new Vector(1, 0) : new Vector(0, 1) );
	}

	public class StateDoorOpen extends StateOpen {
    @Override
    public void execOnChange() {
      super.execOnChange();

      isCollObj=false;
      updateOccTiles();
    }
  }

	public class StateDoorClosed extends StateClosed {
    @Override
    public void execOnChange() {
      super.execOnChange();

      isCollObj=true;
      updateOccTiles();
    }
  }

}
