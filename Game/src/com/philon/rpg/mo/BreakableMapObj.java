package com.philon.rpg.mo;

import com.philon.engine.FrameAnimation;
import com.philon.engine.util.Vector;
import com.philon.rpg.ImageData;
import com.philon.rpg.RpgGame;
import com.philon.rpg.mo.state.AbstractMapObjState;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.item.ItemData;

public abstract class BreakableMapObj extends UpdateMapObj implements Selectable {
  public int breakTimer;

  public abstract int getImgBreak();
  public abstract int getSouBreak();
  public abstract int getDropValue();

  public int getAnimDur() {
    return (int) RpgGame.fps/2;
  }

  @Override
  public Class<? extends AbstractMapObjState> getDefaultState() {
    return IntactState.class;
  }

  public void destroy() {
    changeState(BreakingState.class);
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

  @Override
  public void interactTrigger(UpdateMapObj objInteracting) {

  }

  public class IntactState extends AbstractMapObjState {
    @Override
    public void execOnChange() {
      setAnimation(new FrameAnimation(ImageData.images[getImgBreak()]));
    }

    @Override
    public boolean execUpdate() {
      return true;
    }
  }

  public class BreakingState extends AbstractMapObjState {
    @Override
    public void execOnChange() {
      setAnimation(new FrameAnimation(ImageData.images[getImgBreak()], getAnimDur(), false));
      breakTimer = getAnimDur();
      isCollObj=false;

      AbstractItem it = ItemData.createRandomItem( getDropValue() );
      Vector newItemPos = RpgGame.inst.gMap.getNextFreeTile(pos, false, false, true, true);
      if( newItemPos!=null ) {
        it.setPosition(newItemPos);
        it.changeState( AbstractItem.StateMap.class );
      }

      RpgGame.playSoundFX(getSouBreak());
    }

    @Override
    public boolean execUpdate() {
      breakTimer -= 1;
      if (breakTimer==0) {
        changeState(BrokenState.class);
      }
      return true;
    }
  }

  public class BrokenState extends AbstractMapObjState {
    @Override
    public void execOnChange() {
      setAnimation(new FrameAnimation(ImageData.images[getImgBreak()], 0, true));
    }

    @Override
    public boolean execUpdate() {
      return true;
    }
  }

}
