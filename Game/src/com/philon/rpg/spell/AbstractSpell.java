package com.philon.rpg.spell;
import java.util.LinkedList;

import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.map.mo.BreakableMapObj;
import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.map.mo.StaticMapObj;
import com.philon.rpg.map.mo.UpdateMapObj.StateDying;
import com.philon.rpg.map.mo.UpdateMapObj.StateDyingParam;
import com.philon.rpg.map.mo.UpdateMapObj.StateMovingParam;
import com.philon.rpg.map.mo.UpdateMapObj.StateMovingStraight;
import com.philon.rpg.mos.door.AbstractDoor;
import com.philon.rpg.mos.shot.AbstractShot;
import com.philon.rpg.mos.wall.AbstractWall;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.util.RpgUtil;
import com.philon.rpg.util.TimerListObject;

/**
 * Every spell must have following method: <p>
 *   public static SpellDescriptor getDescriptor() {}
 */
public abstract class AbstractSpell {
  protected final SpellDescriptor descriptor;
	public int sLvl;
	public Vector pos;
	public StatsObj stats;
	public boolean isDying=false;

	protected CombatMapObj ownerMO;
	protected Vector tarPos;
	protected RpgMapObj target;
	protected LinkedList<AbstractShot> shots = new LinkedList<AbstractShot>();
	protected float lifeTimeCooldown;
	protected LinkedList<TimerListObject> hitObjects = new LinkedList<TimerListObject>();

	public AbstractSpell(CombatMapObj newOwnerMO, int newSLvl, Vector newTarPos, RpgMapObj newTarget) {
	  descriptor = getDescriptor(getClass());
	  sLvl = newSLvl;
	  target = newTarget;
	  lifeTimeCooldown = descriptor.getLifeTime();

	  ownerMO = newOwnerMO;
	  tarPos = newTarPos;
	  if( newTarget!=null ) {
	    tarPos = ((RpgMapObj)newTarget).pos.copy();
	  }

	  float newCastRange = 0.5f;
	  pos = newOwnerMO.pos.copy().addInst( Vector.sub( tarPos, newOwnerMO.pos ).normalizeInst().mulScalarInst(newCastRange) );

	  stats = descriptor.getDamageForLevel(sLvl);
	  RpgGame.inst.playSoundFX(descriptor.getSouCast());
	}

	public static SpellDescriptor getDescriptor(Class<? extends AbstractSpell> spellClass) {
	  try {
      return (SpellDescriptor) spellClass.getDeclaredMethod("getDescriptor").invoke(null);
    } catch (Exception e) {
      e.printStackTrace();
    }
	  return null;
	}

	public void update(float deltaTime) {
	  LinkedList<TimerListObject> forDelete = new LinkedList<TimerListObject>();
		for( TimerListObject tlo : hitObjects ) {
			tlo.timerValue -= deltaTime;
			if( tlo.timerValue<0 ) {
			  forDelete.add(tlo);
			}
		}
		hitObjects.removeAll(forDelete);

		if(lifeTimeCooldown>0) {
			lifeTimeCooldown -= deltaTime;
			if(lifeTimeCooldown<0) deleteObject();
		}
	}

	public void shotImpactTrigger( AbstractShot shotObj, RpgMapObj mo ) {
		for( TimerListObject tlo : hitObjects ) {
			if( tlo.mo == mo ) {
				return;
			}
		}
		hitObjects.addLast( new TimerListObject(mo, 0.25f) );

		if (mo instanceof StaticMapObj || mo instanceof AbstractWall || mo instanceof AbstractDoor) {
		  shotCollidedTrigger( shotObj, false );
		} else if (mo instanceof BreakableMapObj) {
      ((BreakableMapObj)mo).destroy();
      shotCollidedTrigger( shotObj, true );
		} else if (mo instanceof CombatMapObj) {
		  ownerMO.attack((CombatMapObj)mo, this);
		  shotCollidedTrigger( shotObj, true );
		}
	}

	private void shotCollidedTrigger(AbstractShot shotObj, boolean allowPassthrough ) {
		if( descriptor.getIsPassthrough()==false || allowPassthrough ) {
			shots.remove( shotObj );
			shotObj.changeState(StateDying.class, new StateDyingParam(null));
			if( shots.isEmpty() ) {
				deleteObject();
			}
		}
	}

	public void deleteObject() {
		for( AbstractShot s : shots ) {
			s.changeState(StateDying.class, new StateDyingParam(null));
		}
		isDying=true;
	}

	public void createSimpleShot(Class<? extends AbstractShot> clazz, Vector newShotPos) {
		AbstractShot sh = Util.instantiateClass(clazz);
		sh.ownerSpell = this;
		sh.setPosition(newShotPos);
		sh.tilesPerSecond = descriptor.getShotSpeed();
    sh.changeState(StateMovingStraight.class, new StateMovingParam(Vector.sub(tarPos, newShotPos)));
		shots.addLast(sh);
		RpgUtil.insertMapObj(sh);
	}

  public static abstract class SpellDescriptor {
    public abstract String getName();
    public abstract StatsObj getDamageForLevel(int level);
    public abstract int getManacostForLevel(int level);
    protected boolean getIsPassthrough() { return false; }
    protected abstract float getLifeTime(); //in seconds
    protected abstract float getShotSpeed();
    public abstract int getImgIcon();
    public abstract int getSouPrepare();
    public abstract int getSouCast();
  }

}
