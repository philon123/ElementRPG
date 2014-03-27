package com.philon.rpg.mos.door;

import com.philon.rpg.map.mo.ToggleMapObj;

public abstract class AbstractDoor extends ToggleMapObj {

	public class StateDoorOpen extends StateOpen {
    @Override
    public void execOnChange() {
      super.execOnChange();

      isCollObj = false;
      dirty = true;
    }
  }

	public class StateDoorClosed extends StateClosed {
    @Override
    public void execOnChange() {
      super.execOnChange();

      isCollObj = true;
      dirty = true;
    }
  }

}
