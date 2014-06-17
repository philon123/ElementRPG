package com.philon.rpg.map.mo;

import com.philon.engine.PhilonGame;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.map.mo.state.StateParam;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.item.AbstractItem.StateMap;
import com.philon.rpg.mos.item.ItemData;
import com.philon.rpg.util.RpgUtil;

public abstract class BreakableMapObj extends ToggleMapObj {
  public int breakTimer;

  public BreakableMapObj() {
    isSelectable = false;
  }

  @Override
  public void toggle() {
    if( currState instanceof StateClosed ) {
      changeState(StateOpening.class, new StateParam());
      hasBeenToggled = true;
    }
  }

  public abstract int getImgBreak();
  public abstract int getSouBreak();
  public abstract int getDropValue();

  @Override
  public int getSouOpening() {
    return getSouBreak();
  }

  @Override
  public int getToggleImage() {
    return getImgBreak();
  }

  @Override
  public int getToggleTime() {
    return getAnimDur();
  }

  @Override
  public float getTilesPerSecond() {
    return 0;
  }

  @Override
  public int getImgIdle() {
    return 0;
  }

  @Override
  public int getImgMoving() {
    return 0;
  }

  @Override
  public int getImgDying() {
    return 0;
  }

  @Override
  public int getSouDie() {
    return 0;
  }

  public int getAnimDur() {
    return (int) PhilonGame.inst.fps/2;
  }

  public void destroy() {
    changeState(StateBreaking.class, new StateParam());
    isCollObj = false;
    isSelectable = false;
  }

  public class StateBreaking extends StateOpening {
    public StateBreaking(StateParam param) {
      super(param);
    }
    @Override
    public void execOnChange() {
      super.execOnChange();

      breakTimer = getAnimDur();

      AbstractItem it = ItemData.createRandomItem( getDropValue() );
      Vector newItemPos = RpgUtil.getNextFreeTile(pos, false, false, true, true);
      if( newItemPos!=null ) {
        it.setPosition(newItemPos);
        it.changeState(StateMap.class, new StateParam());
      }

      RpgGame.inst.playSoundFX(getSouBreak());
    }

    @Override
    public boolean execUpdate() {
      breakTimer -= 1;
      if (breakTimer==0) {
        changeState(StateOpen.class, new StateParam());
      }
      return true;
    }
  }

  public class StateBroken extends StateOpen {
    public StateBroken(StateParam param) {
      super(param);
    }
    @Override
    public void execOnChange() {
      super.execOnChange();

      isCollObj=false;
      isSelectable = false;
    }
  }

}
