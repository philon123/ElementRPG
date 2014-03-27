package com.philon.rpg.mos.item;

import com.philon.engine.FrameAnimation;
import com.philon.engine.util.Vector;
import com.philon.rpg.ImageData;
import com.philon.rpg.RpgGame;
import com.philon.rpg.SoundData;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.map.mo.UpdateMapObj;
import com.philon.rpg.map.mo.state.AbstractMapObjState;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.effect.EffectsObj;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddDurability;
import com.philon.rpg.stat.effect.EffectsObjSaveData;
import com.philon.rpg.stat.presuf.AbstractPrefix;
import com.philon.rpg.stat.presuf.AbstractSuffix;
import com.philon.rpg.stat.presuf.PrefixSuffixData;
import com.philon.rpg.stat.presuf.PrefixSuffixSaveData;

public abstract class AbstractItem extends UpdateMapObj {
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

    displayText = getItemName();
    dropValue   = getDropValue();
    baseEffects = getBaseEffects();
    requirements = getRequirements();
    invSize = getInvSize();
    souDrop = getSouDrop();
    souFlip = getSouFlip();
    imgMap = getImgMap();
    imgInv = getImgInv();

    isCollObj = false;

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

	public class StatePickedUp extends AbstractMapObjState {
	  @Override
	  public void execOnChange() {
	    if( currState instanceof StateMap ) {
	      deleteObject();
	    }

	    setAnimation(new FrameAnimation(ImageData.images[imgInv]));
	    RpgGame.playSoundFX(SoundData.SOU_PICKUP);
	  }

    @Override
    public boolean execUpdate() {
      return true;
    }
	}

	public class StateMap extends AbstractMapObjState {
    @Override
    public void execOnChange() {
      setAnimation(new FrameAnimation(ImageData.images[imgMap]));
      turnToDirection( new Vector(0, 1) );
      collRect = new Vector(0.5f);
      setPosition(pos);
      RpgGame.inst.gMap.insertMapObj(AbstractItem.this);

      RpgGame.playSoundFX( getSouFlip() );
    }

    @Override
    public boolean execUpdate() {
      return true;
    }
  }

	public class StateInv extends AbstractMapObjState {
    @Override
    public void execOnChange() {
    }

    @Override
    public boolean execUpdate() {
      return true;
    }
  }

	public ItemSaveData save() {
    return new ItemSaveData(this);
  }

  public static class ItemSaveData extends RpgMapObjSaveData {
    public boolean isIdentified = false;
    public int iEffType = ItemData.EFFTYPE_NORMAL;

    public EffectsObjSaveData baseEffects = new EffectsObjSaveData();
    public PrefixSuffixSaveData prefix;
    public PrefixSuffixSaveData suffix;

    public ItemSaveData( Class<? extends RpgMapObj> newObjClass ) {
      super(newObjClass, new Vector(), new Vector());
    }

    public ItemSaveData(AbstractItem obj) {
      this( obj.getClass() );

      isIdentified = obj.isIdentified;
      iEffType = obj.iEffType;
      baseEffects = obj.baseEffects.save();
      if (obj.prefix!=null) prefix = obj.prefix.save();
      if (obj.suffix!=null) suffix = obj.suffix.save();
    }

    @Override
    public RpgMapObj load() {
      AbstractItem result = (AbstractItem)super.load();

      result.iEffType = iEffType;
      if (prefix!=null) result.prefix=(AbstractPrefix) PrefixSuffixData.load(prefix);
      if (suffix!=null) result.suffix=(AbstractSuffix) PrefixSuffixData.load(suffix);

      if( !isIdentified ) {
        result.deidentify();
      }

      result.updateEffects();

      result.changeState( StatePickedUp.class );
      result.pos = pos.copy();

      return result;
    }
  }

}
