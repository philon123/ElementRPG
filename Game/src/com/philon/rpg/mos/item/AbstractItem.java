package com.philon.rpg.mos.item;

import com.philon.engine.FrameAnimation;
import com.philon.engine.util.Vector;
import com.philon.rpg.ImageData;
import com.philon.rpg.RpgGame;
import com.philon.rpg.mo.Selectable;
import com.philon.rpg.mo.UpdateMapObj;
import com.philon.rpg.mo.state.AbstractMapObjState;
import com.philon.rpg.mos.player.AbstractChar;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.effect.EffectsObj;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddDurability;
import com.philon.rpg.stat.presuf.AbstractPrefix;
import com.philon.rpg.stat.presuf.AbstractSuffix;
import com.philon.rpg.stat.presuf.PrefixSuffixData;

public abstract class AbstractItem extends UpdateMapObj implements Selectable {
	public int iEffType;

	public String displayText;
	public Vector invSize;
	public float dropValue;
	public boolean isIdentified = true;
	public boolean reqMetFlag = false; //used by inventory and drawForm()

	public EffectsObj baseEffects;
	public EffectsObj effects ;
	public StatsObj stats;
	public AbstractPrefix prefix;
	public AbstractSuffix suffix;
	public StatsObj requirements;

	public int souDrop;
	public int souFlip;
	public int imgMap;
	public int imgInv;

	public AbstractItem() {
	  super();

	  addState( StateIdle.class );
    addState( StatePickedUp.class );
    addState( StateMap.class );
    addState( StateInv.class );

    displayText = getItemName();
    dropValue   = getDropValue();
    baseEffects = getBaseEffects();
    requirements = getRequirements();
    invSize = getInvSize();
    souDrop = getSouDrop();
    souFlip = getSouFlip();
    imgMap = getImgMap();
    imgInv = getImgInv();

    updateEffects();
	}

	public abstract String getItemName();
	public abstract int getDropValue();
  public abstract int getBaseDurability();
	public abstract Vector getInvSize();
	public abstract int getSouDrop();
	public abstract int getSouFlip();
	public abstract int getImgMap();
	public abstract int getImgInv();

	public EffectsObj getBaseEffects() {
    EffectsObj result = new EffectsObj();
    result.addOrCreateEffect( EffectAddDurability.class, getBaseDurability() );
    return result;
  }

	@Override
	public boolean getIsAutoInsert() {
	  return false;
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
  public float getTilesPerSecond() {
    return 0;
  }

  @Override
  public Vector getCollRect() {
    return new Vector();
  }


	public StatsObj getRequirements() {
    return new StatsObj();
  }

	public ItemSaveData save() {
		return new ItemSaveData(this);
	}

	public static AbstractItem load( ItemSaveData isd, AbstractChar newOwnerPlayer ) {
		AbstractItem it = ItemData.createItem(isd.itemClass);

		it.iEffType = isd.iEffType;
		if (isd.prefix!=null) it.prefix=(AbstractPrefix) PrefixSuffixData.load(isd.prefix);
		if (isd.suffix!=null) it.suffix=(AbstractSuffix) PrefixSuffixData.load(isd.suffix);

		it.updateEffects();

		if( !isd.isIdentified ) {
			it.deidentify();
		}

		it.changeState( StatePickedUp.class );
		it.pos = isd.pos.copy();

		return it;
	}

	//----------

	public void updateEffects() {
		effects = new EffectsObj();
		effects.addToSelf( baseEffects );
		if (isIdentified) {
  		if( prefix!=null ) {
  			effects.addToSelf( prefix.effects );
  		}
  		if( suffix!=null ) {
  			effects.addToSelf( suffix.effects );
  		}
		}
		updateStats();
	}

	public void updateStats() {
	  stats = effects.getStats();
	}

	public void deidentify() {
		isIdentified=false;
		updateEffects();
	}

	public void identify() {
		isIdentified=true;
		updateEffects();
	}

	public String getDisplayTextTitle() {
		String dt = "";

		if( isIdentified ) {
			if( prefix!=null ) {
				dt += prefix.getDisplayTet() + " ";
			}
			dt += displayText;
			if( suffix!=null ) {
				dt += " " + suffix.getDisplayTet();
			}
		} else {
			dt += displayText;
		}

		return dt;
	}

	public String getDisplayTextBody() {
		String dt = "";

		dt += getDisplayTextTitle() + "\r\n";
		dt += "\r\n";

		dt += effects.getDisplayTextBody();
//		if( !(this instanceof ConsumableItem || this instanceof RingItem || this instanceof AmuletItem) ) {
//			dt += "Durability: " + (Integer)stats.getStatValue(StatDurability.class) + "\r\n";
//		}

		if( !isIdentified ) {
			dt += "Not Identified" + "\r\n";
		}

		dt += "\r\n";
		dt += requirements.getReqDisplayTextBody();

		return dt;
	}

	@Override
	public void interactTrigger(UpdateMapObj objInteracting) {

  }

	public class StatePickedUp extends AbstractMapObjState {
	  @Override
	  public void execOnChange() {
	    pos = new Vector( -1 );
	    setAnimation(new FrameAnimation(ImageData.images[imgInv]));

	    if( currState==AbstractItem.StateMap.class ) {
	      deleteObject();
	      currState = StatePickedUp.class;
	    }
	  }

    @Override
    public boolean execUpdate() {
      return true;
    }
	}

	//----------

	public class StateMap extends AbstractMapObjState {
    @Override
    public void execOnChange() {
      turnToDirection( new Vector(0, 1) );
      collRect = new Vector(0.5f);
      updateOccTiles();
      setAnimation(new FrameAnimation(ImageData.images[imgMap]));

      if (souFlip>0) RpgGame.playSoundFX( souFlip );
      RpgGame.inst.dynamicMapObjs.addLast(AbstractItem.this);
      updateRenderMapKey();
    }

    @Override
    public boolean execUpdate() {
      return true;
    }
  }

  //----------

	public class StateInv extends AbstractMapObjState {
    @Override
    public void execOnChange() {
    }

    @Override
    public boolean execUpdate() {
      return true;
    }
  }

  //----------

}
