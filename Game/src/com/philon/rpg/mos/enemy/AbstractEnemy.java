package com.philon.rpg.mos.enemy;

import com.philon.engine.PhilonGame;
import com.philon.engine.input.User;
import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.RpgUser;
import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.map.mo.UpdateMapObj;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.item.ItemData;
import com.philon.rpg.mos.player.AbstractChar;
import com.philon.rpg.spell.AbstractSpell;
import com.philon.rpg.stat.StatsObj.StatHealth;
import com.philon.rpg.stat.StatsObj.StatM1Stype;
import com.philon.rpg.stat.StatsObj.StatMaxHealth;
import com.philon.rpg.util.RpgUtil;

public abstract class AbstractEnemy extends CombatMapObj {
  int targetUpdateCooldown = Util.rand(0, (int)(PhilonGame.inst.fps/3f));

	public abstract String getName();
	public abstract int getDropValue();
	public abstract int getWorthXP();
	public abstract float getMaxPullDist();

	@Override
	public int getSouFootstep() {
	  return 0;
	}

  @Override
  public Vector getCollRect() {
    return new Vector(0.6f);
  }

	@Override
	public boolean getCanSeeMO( RpgMapObj newTarget ) {
    Vector tile1 = pos.copy().roundAllInst();
    Vector tile2 = newTarget.pos.copy().roundAllInst();
    float aiDist = RpgUtil.sqrMap[ (int) (Math.pow(tile2.x-tile1.x, 2)+Math.pow(tile2.y-tile1.y, 2)) ];
    if (aiDist>getMaxPullDist()) return false;

    return RpgUtil.tilesInSight( tile1, tile2 );
  }

	@Override
	public void update() {
	  if (!(currState instanceof StateHit || currState instanceof StateDying)) {
	    if(currTargetPos!=null) {
	      currSelectedSpell = (Integer)stats.getStatValue(StatM1Stype.class);
	      changeState(StateAttackingSmart.class);
	    }
	  }

		super.update();
	}

	@Override
	public void updateCooldowns() {
	  super.updateCooldowns();

	  //target update cooldown
    if( targetUpdateCooldown==0 ) {
      CombatMapObj newTarget = findNewTarget();
      if(newTarget==null && currTargetPos!=null) {
        currTarget = null; //remember target pos
      } else {
        setTarget(newTarget);
      }
      targetUpdateCooldown = (int)(PhilonGame.inst.fps/3f);
    } else {
      targetUpdateCooldown -= 1;
    }
	}

	@Override
	public void attack(CombatMapObj mo, AbstractSpell spell) {
	  if (mo instanceof AbstractEnemy) return;

	  super.attack(mo, spell);
	}

	@Override
	public void deathTrigger(CombatMapObj killedBy) {
		AbstractItem it = ItemData.createRandomItem( getDropValue() );
		Vector newItemPos = RpgUtil.getNextFreeTile(pos, false, false, true, true);
		if( newItemPos!=null ) {
			it.setPosition(newItemPos);
			it.changeState( AbstractItem.StateMap.class );
		}

		if (killedBy instanceof AbstractChar) {
      ((AbstractChar)killedBy).addXP(getWorthXP());
    }

		super.deathTrigger(killedBy);
	}

	public void interactTrigger(UpdateMapObj objInteracting) {
  }

  private CombatMapObj findNewTarget() {
    CombatMapObj newTarget = null;
    float minDist = Float.MAX_VALUE;
    for(User currUser : RpgGame.inst.users) {
      float tmpDist = Vector.getDistance(pos, ((RpgUser)currUser).character.pos);
      if(tmpDist<minDist) {
        minDist = tmpDist;
        newTarget = ((RpgUser)currUser).character;
      }
    }
    if(!getCanSeeMO(newTarget)) newTarget = null;
    return newTarget;
  }

	public String getDisplayText() {
		String dt = getName() + "\n";
		dt += " Health" + (Integer)stats.getStatValue(StatHealth.class) + "/" + (Integer)stats.getStatValue(StatMaxHealth.class);
		return dt;
	}

}
