package com.philon.rpg.mos.player;

import com.badlogic.gdx.Gdx;
import com.philon.engine.PhilonGame;
import com.philon.engine.input.User;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.RpgUser;
import com.philon.rpg.forms.InventoryForm;
import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.map.mo.state.StateParam;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.item.category.ConsumableItem;
import com.philon.rpg.mos.player.inventory.Inventory;
import com.philon.rpg.mos.player.inventory.InventorySaveData;
import com.philon.rpg.spell.AbstractSpell;
import com.philon.rpg.spell.SpellArrow;
import com.philon.rpg.spell.SpellFireBolt;
import com.philon.rpg.spell.SpellFireWall;
import com.philon.rpg.spell.SpellHealing;
import com.philon.rpg.spell.SpellKugelBlitz;
import com.philon.rpg.spell.SpellMelee;
import com.philon.rpg.stat.StatsObj.StatDefaultSpell;
import com.philon.rpg.stat.StatsObj.StatHealth;
import com.philon.rpg.stat.StatsObj.StatM1Stype;
import com.philon.rpg.stat.StatsObj.StatM2Stype;
import com.philon.rpg.stat.StatsObj.StatMana;
import com.philon.rpg.stat.StatsObj.StatMaxHealth;
import com.philon.rpg.stat.StatsObj.StatMaxMana;
import com.philon.rpg.stat.effect.EffectsObj;

public abstract class AbstractChar extends CombatMapObj {
	public int xp;
	public int currLevel = 1;
	public int freeStatPoints;

	public Inventory inv;

	public AbstractChar() {
	  super();

	  setLuminance(0.5f);
    inv = new Inventory(this);

    stats.addOrCreateStat( StatM2Stype.class, SpellFireBolt.class );
    stats.spells.put(SpellFireWall.class, 1);
    stats.spells.put(SpellKugelBlitz.class, 1);
    stats.spells.put(SpellHealing.class, 1);
	}

	public abstract int getSouNoMana();
	public abstract String getCharText();
  @Override
  public float getTilesPerSecond() {
    return 4;
  }
  @Override
  public int getSouFootstep() {
    return 6;
  }
  @Override
  public Vector getCollRect() {
    return new Vector(0.3f);
  }

  @Override
  public boolean useMana(int amount) {
    if(!super.useMana(amount)) {
      RpgGame.inst.playSoundFX( getSouNoMana() );
      return false;
    }
    return true;
  }

  @Override
  public void deleteObject() {
    super.deleteObject();

    if(RpgGame.inst.getExclusiveUser()!=null && RpgGame.inst.getExclusiveUser().character==this) {
      RpgGame.inst.endExclusiveSession();
    }
    User thisUser = null;
    for(User currUser : RpgGame.inst.users) {
      if(((RpgUser)currUser).character==this) {
        thisUser = currUser;
        break;
      }
    }
    RpgGame.inst.users.remove(thisUser);
    if(RpgGame.inst.users.isEmpty()) Gdx.app.exit();
  }

  @Override
	public void interact( RpgMapObj newTarget ) {
    if(newTarget==null) return;
		if(newTarget instanceof AbstractItem) {
			if( RpgGame.inst.guiHierarchy.getElementByClass(InventoryForm.class)!=null ) {
				inv.pickupItem( (AbstractItem)newTarget );
			} else {
				inv.pickupAuto( (AbstractItem)newTarget );
			}
		}
		super.interact(newTarget);
	}

	@Override
  public void attack(CombatMapObj mo, AbstractSpell spell) {
    if (mo instanceof AbstractChar) return;

    super.attack(mo, spell);
  }

  @Override
  @SuppressWarnings("unchecked")
	public void updateStats() {
	  super.updateStats();

	  if (inv!=null){
	    inv.updateReqMetFlags();
	    stats.spells.put(SpellMelee.class, 0);
	    stats.spells.put(SpellArrow.class, 0);
      stats.spells.put((Class<? extends AbstractSpell>)stats.getStatValue(StatDefaultSpell.class), 1);
	  }
	}

	@Override
	public EffectsObj getAdditionalEffects() {
	  return inv==null ? new EffectsObj() : inv.effects;
	}

	public void addXP( int newXP ) {
	  xp += newXP;
	  int newLevel = CharData.getLevelForXP(xp);
	  while(currLevel<newLevel) {
	    levelUp();
	  }
	}

	public void levelUp() {
	  currLevel++;
	  freeStatPoints += 5;
	  stats.addOrCreateStat(StatHealth.class, stats.getStatValue(StatMaxHealth.class));
	  stats.addOrCreateStat(StatMana.class, stats.getStatValue(StatMaxMana.class));

	  RpgGame.inst.playSoundFX(14);
	}

	//----------

	public boolean consumeItem( ConsumableItem newItem ) {
	  newItem.consumedTrigger(this);
		inv.removeItem( newItem );
		return true;
	}

	//----------

	public String getDisplayText() {
		String dt = getCharText() + "\r\n";
		dt += " Health" + (Integer)stats.getStatValue(StatHealth.class) + "/" + (Integer)stats.getStatValue(StatMaxHealth.class);
		return dt;
	}

	//----------

  public CharacterSaveData save() {
    return new CharacterSaveData(this);
  }

  @Override
  protected AIState getDefaultAI() {
    return new ControllerAI();
  }

  public class StateMovingWithFootsteps extends StateMovingStraight {
    int footstepCooldown;
    public StateMovingWithFootsteps(StateMovingParam param) {
      super(param);
    }
    @Override
    public boolean execUpdate() {
      if(!super.execUpdate()) return false;

      if( footstepCooldown>0 ) {
        footstepCooldown -= 1;
      } else {
        footstepCooldown = (int)(PhilonGame.inst.fps/4f);
        RpgGame.inst.playSoundFX(getSouFootstep());
      }
      return true;
    }
  }

  public class ControllerAI extends DefaultAI {
    private Vector m_movementDir = new Vector();
    private Vector m_castingDir = new Vector();
    @Override
    @SuppressWarnings("unchecked")
    public void updateTimed() {
      if(!m_castingDir.isZeroVector()) {
        Class<? extends AbstractSpell> currSpell = (Class<? extends AbstractSpell>)stats.getStatValue(StatM1Stype.class);
        changeState(StateCasting.class, new StateCastingParam(currSpell, Vector.add(pos, m_castingDir), null));
      } else if(!m_movementDir.isZeroVector() && !(currState instanceof StateCasting)) {
        if(currState instanceof StateMovingStraight) {
          ((StateMovingStraight)currState).changeDirection(m_movementDir);
        } else {
          changeState(StateMovingStraight.class, new StateMovingParam(m_movementDir));
        }
      } else {
        if(currState instanceof StateMovingStraight) changeState(StateIdle.class, new StateParam());
      }
    }
    @Override
    protected float getConfiguredAIUpdatesPerSecond() {
      return 30f;
    }
    public void setMovementDir(Vector newDir) {
      m_movementDir = newDir.copy();
    }
    public void setCastingDir(Vector newDir) {
      m_castingDir = newDir.copy();
    }
  }

  public static class CharacterSaveData extends RpgMapObjSaveData {
    public int xp;
    public InventorySaveData inv = new InventorySaveData();

    public CharacterSaveData(Class<? extends RpgMapObj> newObjClass, Vector newPos, Vector newDirection, int newXp) {
      super(newObjClass, newPos, newDirection);

      xp = newXp;
    }

    public CharacterSaveData(AbstractChar obj) {
      this(obj.getClass(), obj.pos, obj.orientation, obj.xp);

      inv = obj.inv.save();
    }

    @Override
    public AbstractChar load() {
      AbstractChar result = (AbstractChar)super.load();

      result.xp = xp;
      result.inv = new Inventory(result, inv);

      result.updateStats();

      return result;
    }
  }

}
