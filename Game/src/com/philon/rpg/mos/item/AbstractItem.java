package com.philon.rpg.mos.item;

import com.philon.engine.Data;
import com.philon.engine.FrameAnimation;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgData;
import com.philon.rpg.RpgGame;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.map.mo.UpdateMapObj;
import com.philon.rpg.map.mo.state.MapObjState;
import com.philon.rpg.map.mo.state.StateParam;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.stat.effect.EffectsObj;
import com.philon.rpg.stat.presuf.AbstractPrefix;
import com.philon.rpg.stat.presuf.AbstractPrefixSuffix.PrefixSuffixSaveData;
import com.philon.rpg.stat.presuf.AbstractSuffix;
import com.philon.rpg.util.RpgUtil;

public abstract class AbstractItem extends UpdateMapObj {
	public int iEffType;

	public boolean isIdentified = true;
	public boolean reqMetFlag = false; //used by inventory and drawForm()

	public EffectsObj effects ;
	public StatsObj stats;
	public AbstractPrefix prefix;
	public AbstractSuffix suffix;
	public StatsObj requirements;


	public AbstractItem() {
	  super();

    requirements = getRequirements();

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
		effects.addToSelf( getBaseEffects() );
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
			dt += getItemName();
			if( suffix!=null ) {
				dt += " " + suffix.getDisplayTet();
			}
		} else {
			dt += getItemName();
		}

		return dt;
	}

	public String getDisplayTextBody() {
		String dt = "";

		dt += getDisplayTextTitle() + "\r\n";
		dt += "\r\n";

		dt += effects.getDisplayTextBody();

		if( !isIdentified ) {
			dt += "Not Identified" + "\r\n";
		}

		dt += "\r\n";
		dt += requirements.getReqDisplayTextBody();

		return dt;
	}

	public class StatePickedUp extends MapObjState<StateParam> {
	  public StatePickedUp(StateParam param) {
      super(param);
    }
    @Override
	  public void execOnChange() {
	    if( currState instanceof StateMap ) {
	      RpgUtil.removeMapObj(AbstractItem.this);
	    }

      isCollObj = false;
	    setAnimation(new FrameAnimation(Data.textures.get(getImgInv())));
	    RpgGame.inst.playSoundFX(RpgData.SOU_PICKUP);
	  }
    @Override
    public boolean execUpdate(float deltaTime) {
      return true;
    }
	}

	public class StateMap extends MapObjState<StateParam> {
    public StateMap(StateParam param) {
      super(param);
    }
    @Override
    public void execOnChange() {
      setAnimation(new FrameAnimation(Data.textures.get(getImgMap())));
      turnToDirection( new Vector(0, 1) );
      collRect = new Vector(0.5f);
      setPosition(pos);
      isCollObj = true;
      RpgUtil.insertMapObj(AbstractItem.this);
      RpgGame.inst.playSoundFX( getSouFlip() );
    }
    @Override
    public boolean execUpdate(float deltaTime) {
      return true;
    }
  }

	public class StateInv extends MapObjState<StateParam> {
    public StateInv(StateParam param) {
      super(param);
    }
    @Override
    public void execOnChange() {
      RpgGame.inst.playSoundFX(getSouDrop());
    }
    @Override
    public boolean execUpdate(float deltaTime) {
      return true;
    }
  }

	public ItemSaveData save() {
    return new ItemSaveData(this);
  }

  public static class ItemSaveData extends RpgMapObjSaveData {
    public boolean isIdentified = false;
    public int iEffType = ItemData.EFFTYPE_NORMAL;

    public PrefixSuffixSaveData prefix;
    public PrefixSuffixSaveData suffix;

    public ItemSaveData( Class<? extends RpgMapObj> newObjClass ) {
      super(newObjClass, new Vector(), new Vector());
    }

    public ItemSaveData(AbstractItem obj) {
      this( obj.getClass() );

      isIdentified = obj.isIdentified;
      iEffType = obj.iEffType;
      if (obj.prefix!=null) prefix = obj.prefix.save();
      if (obj.suffix!=null) suffix = obj.suffix.save();
    }

    @Override
    public RpgMapObj load() {
      AbstractItem result = (AbstractItem)super.load();

      result.iEffType = iEffType;
      if (prefix!=null) result.prefix=(AbstractPrefix) prefix.load();
      if (suffix!=null) result.suffix=(AbstractSuffix) suffix.load();

      if( !isIdentified ) {
        result.deidentify();
      }

      result.updateEffects();

      result.changeState(StatePickedUp.class, new StateParam());
      result.pos = pos.copy();

      return result;
    }
  }

}
