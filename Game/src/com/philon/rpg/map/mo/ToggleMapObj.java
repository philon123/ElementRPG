package com.philon.rpg.map.mo;

import com.philon.engine.Data;
import com.philon.engine.FrameAnimation;
import com.philon.engine.PhilonGame;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.map.mo.state.AbstractMapObjState;

public abstract class ToggleMapObj extends UpdateMapObj {
	public int openCloseCooldown;
	public boolean hasBeenToggled = false;

	public abstract int getToggleImage();

  @Override
  public int getImgIdle() {
    return getToggleImage();
  }

  @Override
  public Class<? extends AbstractMapObjState> getDefaultState() {
    return StateClosed.class;
  }

  public int getToggleTime() {
    return (int)PhilonGame.inst.fps/3;
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
  public float getTilesPerSecond() {
    return 0;
  }

  public int getSouOpening() {
    return 0;
  }

  public int getSouOpened() {
    return 0;
  }

  public int getSouClosing() {
    return 0;
  }

  public int getSouClosed() {
    return 0;
  }

	public void updateCooldowns() {
		super.updateCooldowns();

		//open/close
		if( openCloseCooldown>0 ) {
			openCloseCooldown -= 1;
			if( openCloseCooldown==0 ) {
				if( currState instanceof StateOpening ) {
					changeState( StateOpen.class );
				} else if( currState instanceof StateClosing ) {
					changeState( StateClosed.class );
				}
			}
		}

	}

	public void toggle() {
		if( currState instanceof StateClosed ) {
			changeState( StateOpening.class );
		} else if( currState instanceof StateOpen ) {
			changeState( StateClosing.class );
		}
		hasBeenToggled = true;
	}

	@Override
	public void interactTrigger(RpgMapObj objInteracting) {
	  super.interactTrigger(objInteracting);

	  toggle();
	}

	public class StateClosed extends AbstractMapObjState {

	  @Override
	  public void execOnChange() {
	    setAnimation(new FrameAnimation(Data.textures.get(getToggleImage())));
	    RpgGame.inst.playSoundFX( getSouClosed() );
	  }

	  @Override
	  public boolean execUpdate() {
	    return true;
	  }
	}

	public class StateClosing extends AbstractMapObjState {

	  @Override
	  public void execOnChange() {
	    setAnimation(new FrameAnimation(Data.textures.get(getToggleImage()), getToggleTime(), true));
	    RpgGame.inst.playSoundFX( getSouClosing() );
	    openCloseCooldown = getToggleTime();
	  }

	  @Override
	  public boolean execUpdate() {
	    return true;
	  }
	}

	public class StateOpen extends AbstractMapObjState {

	  @Override
	  public void execOnChange() {
	    setAnimation(new FrameAnimation(Data.textures.get(getToggleImage()), 0, true));
	    RpgGame.inst.playSoundFX( getSouOpened() );
	  }

	  @Override
	  public boolean execUpdate() {
	    return true;
	  }
	}

	public class StateOpening extends AbstractMapObjState {

	  @Override
	  public void execOnChange() {
	    setAnimation(new FrameAnimation(Data.textures.get(getToggleImage()), getToggleTime(), false));
	    openCloseCooldown = getToggleTime();
	    RpgGame.inst.playSoundFX( getSouOpening() );
	  }

	  @Override
	  public boolean execUpdate() {
	    return true;
	  }
	}

	@Override
	public ToggleMOSaveData save() {
    return new ToggleMOSaveData(this);
  }

  public static class ToggleMOSaveData extends RpgMapObjSaveData {
    public Class<? extends AbstractMapObjState> state;
    public boolean hasBeenToggled;

    public ToggleMOSaveData(Class<? extends ToggleMapObj> newObjClass, Vector newPos, Vector newDirection, Class<? extends AbstractMapObjState> newStateClass, boolean newHasBeenToggled) {
      super(newObjClass, newPos, newDirection);

      hasBeenToggled = newHasBeenToggled;
      state = newStateClass;
      if(StateOpening.class.isAssignableFrom(state)) state = StateOpen.class;
      if(StateClosing.class.isAssignableFrom(state)) state = StateClosed.class;

    }

    public ToggleMOSaveData(ToggleMapObj obj) {
      this( obj.getClass(), obj.pos, obj.direction, obj.currState.getClass(), obj.hasBeenToggled );
    }

    @Override
    public RpgMapObj load() {
      ToggleMapObj result = (ToggleMapObj)super.load();

      result.hasBeenToggled = hasBeenToggled;
      result.changeState(state);

      return result;
    }
  }
}
