package com.philon.rpg.mos.chest;

import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.map.mo.ToggleMapObj;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.item.ItemData;

public abstract class AbstractChest extends ToggleMapObj {

	public AbstractChest() {
	  super();

	  replaceState(StateOpening.class, StateChestOpening.class);
	}

	public abstract int getDropValue();

	public class StateChestOpening extends StateOpening {
    @Override
    public void execOnChange() {
      super.execOnChange();

      if( !hasBeenToggled ) {
        AbstractItem it = ItemData.createRandomItem( getDropValue() );
        Vector newItemPos = RpgGame.inst.gMap.getNextFreeTile(Vector.add(pos, direction), false, false, true, true);
        if( newItemPos!=null ) {
          it.setPosition(newItemPos);
          it.changeState( AbstractItem.StateMap.class );
        }
      }
    }
  }
}
