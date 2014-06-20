package com.philon.rpg.mos.chest;

import com.philon.engine.util.Vector;
import com.philon.rpg.map.mo.ToggleMapObj;
import com.philon.rpg.map.mo.state.StateParam;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.item.ItemData;
import com.philon.rpg.util.RpgUtil;

public abstract class AbstractChest extends ToggleMapObj {

	public abstract int getDropValue();

	public class StateChestOpening extends StateOpening {
    public StateChestOpening(StateParam param) {
      super(param);
    }
    @Override
    public void execOnChange() {
      super.execOnChange();

      if( !hasBeenToggled ) {
        AbstractItem it = ItemData.createRandomItem( getDropValue() );
        Vector newItemPos = RpgUtil.getNextFreeTile(Vector.add(pos, orientation), false, false, true, true);
        if( newItemPos!=null ) {
          it.setPosition(newItemPos);
          it.changeState(AbstractItem.StateMap.class, new StateParam());
        }
      }
    }
  }
}
