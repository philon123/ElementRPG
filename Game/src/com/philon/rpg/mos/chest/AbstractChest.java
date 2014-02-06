package com.philon.rpg.mos.chest;

import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.mo.ToggleMapObj;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.item.ItemData;

public abstract class AbstractChest extends ToggleMapObj {
	public int dropValue = 100;
	public boolean isEmpty=false;

	public AbstractChest() {
	  super();
	  
	  replaceState(StateOpen.class, StateChestOpen.class);
		turnToDirection( Math.random()<0.5 ? new Vector(1, 0) : new Vector(0, 1) );
	}
	
	public class StateChestOpen extends StateOpen {
    
    @Override
    public void execOnChange() {
      super.execOnChange();

      if( !isEmpty ) {
        AbstractItem it = ItemData.createRandomItem( dropValue );
        Vector newItemPos = RpgGame.inst.gMap.getNextFreeTile(Vector.add(pos, direction), false, false, true, true);
        if( newItemPos!=null ) {
          it.setPosition(newItemPos);
          it.changeState( AbstractItem.StateMap.class );
        }
        isEmpty=true;
      }
    }
    
  }

}
