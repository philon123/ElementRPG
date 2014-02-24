package com.philon.rpg.mo;

import com.philon.engine.FrameAnimation;
import com.philon.engine.PhilonGame;
import com.philon.rpg.ImageData;
import com.philon.rpg.RpgGame;
import com.philon.rpg.mo.state.AbstractMapObjState;

public abstract class ToggleMapObj extends UpdateMapObj implements Selectable {
	public int openCloseCooldown;

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
    return (int)PhilonGame.fps/3;
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
				if( currState==StateOpening.class ) {
					changeState( StateOpen.class );
				} else if( currState==StateClosing.class ) {
					changeState( StateClosed.class );
				}
			}
		}

	}

	public void toggle() {
		if( currState==StateClosed.class ) {
			changeState( StateOpening.class );
		} else if( currState==StateOpen.class ) {
			changeState( StateClosing.class );
		}
	}

	public void interactTrigger(UpdateMapObj objInteracting) {
	  toggle();
	}

	public class StateClosed extends AbstractMapObjState {

	  @Override
	  public void execOnChange() {
	    setAnimation(new FrameAnimation(ImageData.images[getToggleImage()]));
	    RpgGame.playSoundFX( getSouClosed() );
	  }

	  @Override
	  public boolean execUpdate() {
	    return true;
	  }

	}

	public class StateClosing extends AbstractMapObjState {

	  @Override
	  public void execOnChange() {
	    setAnimation(new FrameAnimation(ImageData.images[getToggleImage()], getToggleTime(), true));
	    RpgGame.playSoundFX( getSouClosing() );
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
	    setAnimation(new FrameAnimation(ImageData.images[getToggleImage()], 0, true));
	    RpgGame.playSoundFX( getSouOpened() );
	  }

	  @Override
	  public boolean execUpdate() {
	    return true;
	  }

	}

	public class StateOpening extends AbstractMapObjState {

	  @Override
	  public void execOnChange() {
	    setAnimation(new FrameAnimation(ImageData.images[getToggleImage()], getToggleTime(), false));
	    openCloseCooldown = getToggleTime();
	    RpgGame.playSoundFX( getSouOpening() );
	  }

	  @Override
	  public boolean execUpdate() {
	    return true;
	  }

	}

}
