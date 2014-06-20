package com.philon.rpg.map.mo;

import com.philon.engine.Data;
import com.philon.engine.FrameAnimation;
import com.philon.engine.PhilonGame;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.map.mo.state.MapObjState;
import com.philon.rpg.map.mo.state.StateParam;

public abstract class ToggleMapObj extends UpdateMapObj {
	public boolean hasBeenToggled = false;

	public ToggleMapObj() {
	  super();

	  changeState(StateClosed.class, new StateParam());
	}

	public abstract int getToggleImage();

  @Override
  public int getImgIdle() {
    return getToggleImage();
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

	public void toggle() {
		if( currState instanceof StateClosed ) {
			changeState(StateOpening.class, new StateParam());
		} else if( currState instanceof StateOpen ) {
			changeState(StateClosing.class, new StateParam());
		}
		hasBeenToggled = true;
	}

	@Override
	public void interactTrigger(RpgMapObj objInteracting) {
	  super.interactTrigger(objInteracting);

	  toggle();
	}

	public class StateClosed extends MapObjState<StateParam> {
	  public StateClosed(StateParam param) {
      super(param);
    }
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

	public class StateClosing extends MapObjState<StateParam> {
    private int closeCooldown;
    public StateClosing(StateParam param) {
      super(param);
    }
	  @Override
	  public void execOnChange() {
	    setAnimation(new FrameAnimation(Data.textures.get(getToggleImage()), getToggleTime(), true));
	    RpgGame.inst.playSoundFX( getSouClosing() );
	    closeCooldown = getToggleTime();
	  }
	  @Override
	  public boolean execUpdate() {
	    if(closeCooldown>0) {
	      closeCooldown--;
	    } else {
	      changeState(StateClosed.class, new StateParam());
	    }
	    return true;
	  }
	}

	public class StateOpen extends MapObjState<StateParam> {
	  public StateOpen(StateParam param) {
      super(param);
    }
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

	public class StateOpening extends MapObjState<StateParam> {
	  private int openCooldown;
    public StateOpening(StateParam param) {
      super(param);
    }
	  @Override
	  public void execOnChange() {
	    setAnimation(new FrameAnimation(Data.textures.get(getToggleImage()), getToggleTime(), false));
	    openCooldown = getToggleTime();
	    RpgGame.inst.playSoundFX( getSouOpening() );
	  }
	  @Override
	  public boolean execUpdate() {
      if(openCooldown>0) {
        openCooldown--;
      } else {
        changeState(StateOpen.class, new StateParam());
      }
	    return true;
	  }
	}

	@Override
	public ToggleMOSaveData save() {
    return new ToggleMOSaveData(this);
  }

  public static class ToggleMOSaveData extends RpgMapObjSaveData {
    public Class<? extends MapObjState<?>> state;
    public boolean hasBeenToggled;

    public ToggleMOSaveData(Class<? extends ToggleMapObj> newObjClass, Vector newPos, Vector newDirection, Class<? extends MapObjState<?>> newStateClass, boolean newHasBeenToggled) {
      super(newObjClass, newPos, newDirection);

      hasBeenToggled = newHasBeenToggled;
      state = newStateClass;
      if(StateOpening.class.isAssignableFrom(state)) state = StateOpen.class;
      if(StateClosing.class.isAssignableFrom(state)) state = StateClosed.class;

    }

    @SuppressWarnings("unchecked")
    public ToggleMOSaveData(ToggleMapObj obj) {
      this( obj.getClass(), obj.pos, obj.orientation, (Class<? extends MapObjState<?>>)obj.currState.getClass(), obj.hasBeenToggled );
    }

    @Override
    public RpgMapObj load() {
      ToggleMapObj result = (ToggleMapObj)super.load();

      result.hasBeenToggled = hasBeenToggled;
      result.changeState(state, new StateParam());

      return result;
    }
  }
}
