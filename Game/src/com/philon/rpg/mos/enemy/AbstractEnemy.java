package com.philon.rpg.mos.enemy;

import java.util.Comparator;
import java.util.TreeMap;

import com.philon.engine.input.User;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.RpgUser;
import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.map.mo.UpdateMapObj;
import com.philon.rpg.map.mo.state.StateParam;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.item.AbstractItem.StateMap;
import com.philon.rpg.mos.item.ItemData;
import com.philon.rpg.mos.player.AbstractChar;
import com.philon.rpg.spell.AbstractSpell;
import com.philon.rpg.spell.SpellMelee;
import com.philon.rpg.stat.StatsObj.StatHealth;
import com.philon.rpg.stat.StatsObj.StatM1Stype;
import com.philon.rpg.stat.StatsObj.StatMaxHealth;
import com.philon.rpg.util.RpgUtil;

public abstract class AbstractEnemy extends CombatMapObj {
  private Vector lastOffset=new Vector();

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
  protected boolean changePosition(Vector newOffset) {
    if(        newOffset.x==0 && lastOffset.x==0 && Math.signum(newOffset.y) == -Math.signum(lastOffset.y) ) {
      return false; //180 degree turns forbidden
    } else if( newOffset.y==0 && lastOffset.y==0 && Math.signum(newOffset.x) == -Math.signum(lastOffset.x) ) {
      return false; //180 degree turns forbidden
    }
    lastOffset = newOffset.copy();

    return super.changePosition(newOffset);
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
			it.changeState(StateMap.class, new StateParam());
		}

		if (killedBy instanceof AbstractChar) {
      ((AbstractChar)killedBy).addXP(getWorthXP());
    }

		super.deathTrigger(killedBy);
	}

	public void interactTrigger(UpdateMapObj objInteracting) {
  }

	public String getDisplayText() {
		String dt = getName() + "\n";
		dt += " Health" + (Integer)stats.getStatValue(StatHealth.class) + "/" + (Integer)stats.getStatValue(StatMaxHealth.class);
		return dt;
	}

  @Override
  protected AIState getDefaultAI() {
    return new BasicAggressiveEnemyAI();
  }

  public class BasicAggressiveEnemyAI extends DefaultAI {
    private MoveToTargetAI stateMoving;
    private UpdateMapObj m_target;
    private Vector m_targetPos;
    public BasicAggressiveEnemyAI() {
      stateMoving = new MoveToTargetAI(new Vector());
    }
    @Override
    @SuppressWarnings("unchecked")
    public void updateTimed() {
      m_target = findNewTarget();
      if(m_target!=null) m_targetPos = m_target.pos;
      if(m_targetPos==null) return;

      float targetDist = Vector.getDistance(pos, m_targetPos);
      Class<? extends AbstractSpell> currSpellClass = (Class<? extends AbstractSpell>)stats.getStatValue(StatM1Stype.class);
      if(m_target==null || (currSpellClass==SpellMelee.class && targetDist>getMaxMeleeRange())) {
        stateMoving.setTargetPos(m_targetPos);
        stateMoving.updateTimed();
      } else {
        changeState(StateCasting.class, new StateCastingParam(currSpellClass, m_targetPos, m_target));
      }
    }
    private CombatMapObj findNewTarget() {
      TreeMap<Float, User> sortedUsers = new TreeMap<Float, User>(new Comparator<Float>(){
        @Override
        public int compare(Float paramT1, Float paramT2) {
          return (int) Math.signum(paramT2-paramT1); //reverse order
        }
      });
      for(User currUser : RpgGame.inst.users) {
        float tmpDist = Vector.getDistance(pos, ((RpgUser)currUser).character.pos);
        sortedUsers.put(tmpDist, currUser);
      }
      for(User currUser : sortedUsers.values()) {
        CombatMapObj currChar = ((RpgUser)currUser).character;
        if(getCanSeeMO(currChar)) return currChar;
      }
      return null;
    }
  }

}
