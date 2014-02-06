package com.philon.rpg.spell;
import java.util.LinkedList;

import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.mo.AbstractMapObj;
import com.philon.rpg.mo.BreakableMapObj;
import com.philon.rpg.mo.CombatMapObj;
import com.philon.rpg.mo.Selectable;
import com.philon.rpg.mo.StaticMapObj;
import com.philon.rpg.mos.door.AbstractDoor;
import com.philon.rpg.mos.shot.AbstractShot;
import com.philon.rpg.mos.shot.ShotData;
import com.philon.rpg.mos.wall.AbstractWall;
import com.philon.rpg.stat.StatsObj;
import com.philon.rpg.util.TimerListObject;

public abstract class AbstractSpell {
	public int id;
	public int sLvl;
	public CombatMapObj ownerMO;
	public Vector pos;

	public StatsObj stats;
	public Vector tarPos;
	public Selectable target;
	public boolean isDying=false;
	public boolean passthrough=false;
	public LinkedList<AbstractShot> shots = new LinkedList<AbstractShot>();
	public int lifeTime;
	public LinkedList<TimerListObject> hitObjects = new LinkedList<TimerListObject>();

	//----------
	
	public void init( CombatMapObj newOwnerMO, int newSType, int newSLvl, Vector newTarPos, Selectable newTarget ) {
	  id            = newSType;
	  sLvl          = newSLvl;
	  target        = newTarget;
	  lifeTime      = SpellData.lifeTime[id];
	  passthrough   = SpellData.passthrough[id];
	  
	  ownerMO = newOwnerMO;
	  if( newTarget!=null ) {
	    tarPos = ((AbstractMapObj)newTarget).pos.copy();
	  } else {
	    tarPos = newTarPos;
	  }
	  
	  float newCastRange = 0.5f;
	  pos = newOwnerMO.pos.copy().addInst( Vector.sub( tarPos, newOwnerMO.pos ).normalizeInst().mulScalarInst(newCastRange) );

	  stats = getBaseStats();
	  RpgGame.playSoundFX( SpellData.souCast[id] );
	}
	
	public StatsObj getBaseStats() {
	  return SpellData.stats[id][sLvl];
	}

	//----------
	
	public void update() {
	  LinkedList<TimerListObject> forDelete = new LinkedList<TimerListObject>();
		for( TimerListObject tlo : hitObjects ) {
			tlo.timerValue -= 1;
			if( tlo.timerValue==0 ) {
			  forDelete.add(tlo);
			}
		}
		hitObjects.removeAll(forDelete);

		if( lifeTime>0 ) {
			lifeTime -= 1;
			if (lifeTime==0) isDying=true;
		}

		if (isDying) deleteObject();
	}

	//----------

	public void shotImpactTrigger( AbstractShot shotObj, AbstractMapObj mo ) {
		for( TimerListObject tlo : hitObjects ) {
			if( tlo.mo == mo ) {
				return;
			}
		}
		hitObjects.addLast( new TimerListObject(mo, (int)(RpgGame.fps/3)) );
		
		if (mo instanceof StaticMapObj || mo instanceof AbstractWall || mo instanceof AbstractDoor) {
		  shotCollidedTrigger( shotObj, true );
		} else if (mo instanceof BreakableMapObj) {
      ((BreakableMapObj)mo).destroy();
      shotCollidedTrigger( shotObj, true );
		} else if (mo instanceof CombatMapObj) {
		  ownerMO.attack((CombatMapObj)mo, this);
		  shotCollidedTrigger( shotObj, false );
		}
		
		
	}

	//----------

	public void shotCollidedTrigger( AbstractShot shotObj, boolean denyPassthrough ) {
		if( passthrough==false || denyPassthrough ) {
			shots.remove( shotObj );
			shotObj.changeState( AbstractShot.StateDying.class );
			if( shots.isEmpty() ) {
				deleteObject();
			}
		}
	}

	//----------

	public void deleteObject() {
		for( AbstractShot s : shots ) {
			s.changeState( AbstractShot.StateDying.class );
		}
		isDying=true;
	}

	//----------

	public void createSimpleShot( Class<? extends AbstractShot> clazz, Vector newShotPos ) {
		float newShotSpeed=SpellData.speed[id];
		AbstractShot sh = ShotData.createShot(clazz);
		sh.ownerSpell = this;
		sh.setPosition(newShotPos);
		sh.tilesPerSecond = newShotSpeed;
		sh.setTarget( target, tarPos );
		shots.addLast(sh);
	}
	
	//----------

}