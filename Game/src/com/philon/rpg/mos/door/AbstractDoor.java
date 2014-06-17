package com.philon.rpg.mos.door;

import com.philon.rpg.map.mo.ToggleMapObj;
import com.philon.rpg.map.mo.state.StateParam;

public abstract class AbstractDoor extends ToggleMapObj {

	public class StateDoorOpen extends StateOpen {
    public StateDoorOpen(StateParam param) {
      super(param);
    }
    @Override
    public void execOnChange() {
      super.execOnChange();

      isCollObj = false;
    }
  }

	public class StateDoorClosed extends StateClosed {
    public StateDoorClosed(StateParam param) {
      super(param);
    }
    @Override
    public void execOnChange() {
      super.execOnChange();

      isCollObj = true;
    }
  }

}
