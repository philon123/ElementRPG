package com.philon.rpg.mos.enemy;

import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.mo.GameMapObj;
import com.philon.rpg.mo.CombatMapObj;
import com.philon.rpg.mo.Selectable;
import com.philon.rpg.mo.UpdateMapObj;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.item.ItemData;
import com.philon.rpg.mos.player.AbstractChar;
import com.philon.rpg.spell.AbstractSpell;
import com.philon.rpg.stat.StatsObj.StatHealth;
import com.philon.rpg.stat.StatsObj.StatM1Stype;
import com.philon.rpg.stat.StatsObj.StatMaxHealth;

public abstract class AbstractEnemy extends CombatMapObj implements Selectable {
	public int dropValue;
	public int worthXP;
	public float maxPullDist;
	
	public abstract String getName();
	public abstract int getDropValue();
	public abstract int getWorthXP();
	public abstract float getMaxPullDist();
	
	public AbstractEnemy() {
    super();
    
    dropValue = getDropValue();
    worthXP = getWorthXP();
    maxPullDist = getMaxPullDist();
  }
	
	@Override
	public int getSouFootstep() {
	  return 0;
	}

  @Override
  public Vector getCollRect() {
    return new Vector(0.3f);
  }
  
	@Override
	public boolean getCanSeeGO( GameMapObj newTarget ) {
    Vector tile1=pos.copy().roundAllInst();
    Vector tile2=newTarget.pos.copy().roundAllInst();
    float aiDist = RpgGame.inst.gUtil.sqrMap[ (int) (Math.pow(tile2.x-tile1.x, 2)+Math.pow(tile2.y-tile1.y, 2)) ];
    if (aiDist>maxPullDist) return false;

    return RpgGame.inst.gMap.tilesInSight( tile1, tile2 );
  }

	//----------

	public void update() {
	  if (!(currState==StateHit.class || currState==StateDying.class)) {
  		if( getCanSeeGO(RpgGame.inst.localPlayer) ) {
  			setTarget(RpgGame.inst.localPlayer);
  			currSelectedSpell = (Integer)stats.getStatValue(StatM1Stype.class);
  			changeState(StateAttacking.class);
  		} else {
  			currTarget = null;
  			if (currTargetPos!=null) {
  			  changeState(StateMovingTarget.class); //TODO use astar
  			}
  		}
	  }
	  
		super.update();
	}

	@Override
	public void attack(CombatMapObj mo, AbstractSpell spell) {
	  if (mo instanceof AbstractEnemy) return;
	  
	  super.attack(mo, spell);
	}

	@Override
	public void deathTrigger(CombatMapObj killedBy) {
		AbstractItem it = ItemData.createRandomItem( dropValue );
		Vector newItemPos = RpgGame.inst.gMap.getNextFreeTile(pos, false, false, true, true);
		if( newItemPos!=null ) {
			it.setPosition(newItemPos);
			it.changeState( AbstractItem.StateMap.class );
		}
		
		if (killedBy instanceof AbstractChar) {
      ((AbstractChar)killedBy).addXP(getWorthXP());
    }

		super.deathTrigger(killedBy);
	}

	//----------
	
	public void interactTrigger(UpdateMapObj objInteracting) {
    
  }

  //----------

	public String getDisplayText() {
		String dt = getName() + "\n";
		dt += " Health" + (Integer)stats.getStatValue(StatHealth.class) + "/" + (Integer)stats.getStatValue(StatMaxHealth.class);
		return dt;
	}

	//----------
	
}
