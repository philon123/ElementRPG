package com.philon.rpg.mos.player;

import com.badlogic.gdx.Gdx;
import com.philon.engine.input.User;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.RpgUser;
import com.philon.rpg.SkillData;
import com.philon.rpg.forms.InventoryForm;
import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.item.category.ConsumableItem;
import com.philon.rpg.mos.player.inventory.Inventory;
import com.philon.rpg.mos.player.inventory.InventorySaveData;
import com.philon.rpg.spell.AbstractSpell;
import com.philon.rpg.spell.SpellData;
import com.philon.rpg.stat.StatsObj.StatDefaultSpell;
import com.philon.rpg.stat.StatsObj.StatHealth;
import com.philon.rpg.stat.StatsObj.StatM1Stype;
import com.philon.rpg.stat.StatsObj.StatM2Stype;
import com.philon.rpg.stat.StatsObj.StatMana;
import com.philon.rpg.stat.StatsObj.StatMaxHealth;
import com.philon.rpg.stat.effect.EffectsObj;
import com.philon.rpg.util.RpgUtil;

public abstract class AbstractChar extends CombatMapObj {
	public int xp;
	public int currLevel = 1;
	public int freeStatPoints;

	public Inventory inv;
	public int skills[];

  public int souNoMana;

	public AbstractChar() {
	  super();

	  setLuminance(0.5f);

    souNoMana = getSouNoMana();
    inv = new Inventory(this);
    skills = new int[getNumSkills()];

    stats.addOrCreateStat( StatM2Stype.class, SpellData.FIRE_BOLT );
    stats.spells[SpellData.FIRE_WALL] += 1; //TODO spells
    stats.spells[SpellData.FIRE_BOLT] += 1;
    stats.spells[SpellData.KUGELBLITZ] += 1;
    stats.spells[SpellData.HEALING] += 1;
	}

	public abstract int getSouNoMana();
	public abstract int getNumSkills();
	public abstract String getCharText();
  @Override
  public float getTilesPerSecond() {
    return 4;
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
  public boolean useMana(int amount) {
    if( amount > (Integer)stats.getStatValue(StatMana.class) ) {
      if (souNoMana>0) RpgGame.inst.playSoundFX( souNoMana );
      return false;
    }

    return super.useMana(amount);
  }

  @Override
  public void deathTrigger(CombatMapObj killedBy) {
    super.deathTrigger(killedBy);

    User thisUser = null;
    for(User currUser : RpgGame.inst.users) {
      if(((RpgUser)currUser).character==this) {
        thisUser = currUser;
        break;
      }
    }
    if(RpgGame.inst.getExclusiveUser()!=null && RpgGame.inst.getExclusiveUser().character==this) {
      RpgGame.inst.endExclusiveSession();
    }
    RpgGame.inst.users.remove(thisUser);
    if(RpgGame.inst.users.isEmpty()) Gdx.app.exit();
  }

  public void setKeyMovement(Vector newDirection) {
    if(newDirection.isAllEqual(new Vector())) {
      stopKeyMovement();
      return;
    }
    if( !(currState instanceof StateHit || currState instanceof StateDying || currState instanceof StateCasting) ) {
      direction = newDirection.copy();
      currPath = null;
      pathfindCooldown = 0;
      setTarget(null, pos.copy().addInst(newDirection));
      currTarget = null;
      currTargetPos = null;

      if(!(currState instanceof StateMovingStraight)) changeState(StateMovingStraight.class);
    }
  }

  private void stopKeyMovement() {
    currPath = null;
    pathfindCooldown = 0;
    currTarget = null;
    currTargetPos = null;

    if( !(currState instanceof StateHit || currState instanceof StateDying || currState instanceof StateCasting) ) {
      changeState(StateIdle.class);
    }
  }

  public void setAttackingDir(Vector newDirection) {
    if(newDirection.isAllEqual(new Vector())) return;

    if( !(currState instanceof StateHit || currState instanceof StateDying || currState instanceof StateCasting) ) {
      Vector newMapDir = RpgUtil.getTilePosByBaseScreenPos(newDirection);
      Vector newTargetPos = Vector.add(pos, newMapDir);
      prepareSpell((Integer)stats.getStatValue(StatM1Stype.class), false, newTargetPos, null);
    }
  }

  public void stopAttackingDir() {
    if( !(currState instanceof StateHit || currState instanceof StateDying || currState instanceof StateCasting) ) {
      changeState(StateIdle.class);
    }
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
	public void updateStats() {
	  super.updateStats();

	  if (inv!=null){
	    inv.updateReqMetFlags();
	    stats.spells[SpellData.MELEE]=0;
      stats.spells[SpellData.ARROW]=0;
      int newDefaultSpell = (Integer)stats.getStatValue(StatDefaultSpell.class);
      if (newDefaultSpell!=SpellData.EMPTY) stats.spells[newDefaultSpell]=1;
	  }
	}

	@Override
	public EffectsObj getAdditionalEffects() {
	  return inv==null ? new EffectsObj() : inv.effects;
	}

	public void setSkill( int newSkill, int newSkillLvl ) {
		int prevGen = getSkillGeneration(skills[newSkill]);
		int currGen = getSkillGeneration(newSkillLvl);
		if( prevGen!=currGen ) {
			stats.spells[SkillData.spellsForSkill[newSkill][prevGen]] = 0;
			stats.spells[SkillData.spellsForSkill[newSkill][currGen]] = newSkillLvl;
		} else {
			skills[newSkill] = newSkillLvl;
		}
	}

	public int getSkillGeneration( int newSkillLevel ) {
		int skillGeneration = (int) Math.floor(newSkillLevel / 5);
		if (skillGeneration>3) skillGeneration=3; //would be 4 is reached skill lvl 2;
		return skillGeneration;
	}

	public void improveSkill( int newSkill ) {
		setSkill( newSkill, skills[newSkill]+1 );
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

  public static class CharacterSaveData extends RpgMapObjSaveData {
    public int xp;
    public InventorySaveData inv = new InventorySaveData();
    public int[] skills = new int[SkillData.numSkills];

    public CharacterSaveData(Class<? extends RpgMapObj> newObjClass, Vector newPos, Vector newDirection, int newXp) {
      super(newObjClass, newPos, newDirection);

      xp = newXp;
    }

    public CharacterSaveData(AbstractChar obj) {
      this(obj.getClass(), obj.pos, obj.direction, obj.xp);

      skills = obj.skills;
      inv = obj.inv.save();
    }

    @Override
    public AbstractChar load() {
      AbstractChar result = (AbstractChar)super.load();

      result.xp = xp;
      result.inv = new Inventory(result, inv);
      for( int i = 0; i < skills.length-1; i++ ) {
        result.setSkill( i, skills[i] );
      }

      result.updateStats();

      return result;
    }
  }

}
