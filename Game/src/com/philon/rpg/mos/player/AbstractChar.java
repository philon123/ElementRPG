package com.philon.rpg.mos.player;

import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.SkillData;
import com.philon.rpg.forms.CharacterForm;
import com.philon.rpg.forms.InventoryForm;
import com.philon.rpg.forms.SpellForm;
import com.philon.rpg.forms.SpellSelectForm;
import com.philon.rpg.forms.StatusbarForm;
import com.philon.rpg.mo.CombatMapObj;
import com.philon.rpg.mo.Selectable;
import com.philon.rpg.mo.UpdateMapObj;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.item.category.ConsumableItem;
import com.philon.rpg.mos.player.inventory.Inventory;
import com.philon.rpg.mos.player.inventory.Inventory.Equip;
import com.philon.rpg.spell.AbstractSpell;
import com.philon.rpg.spell.SpellData;
import com.philon.rpg.stat.StatsObj.StatDefaultSpell;
import com.philon.rpg.stat.StatsObj.StatDurability;
import com.philon.rpg.stat.StatsObj.StatHealth;
import com.philon.rpg.stat.StatsObj.StatM2Stype;
import com.philon.rpg.stat.StatsObj.StatMana;
import com.philon.rpg.stat.StatsObj.StatMaxHealth;
import com.philon.rpg.stat.effect.EffectsObj;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddDexterity;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddDurability;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddMagic;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddStrength;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddVitality;

public abstract class AbstractChar extends CombatMapObj implements Selectable {
	public Vector newDir=new Vector();
	public boolean isKeyMovement=false;

	public int xp;
	public int currLevel=1;
	public int freeStatPoints;
	
	public Inventory inv;
	public int skills[];
	
	public InventoryForm invForm;
  public CharacterForm charForm;
  public SpellForm spellForm;
  public SpellSelectForm spellSelectForm;
  public StatusbarForm statbarForm;

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
    
    invForm = new InventoryForm();
    charForm = new CharacterForm();
    spellForm = new SpellForm();
    spellSelectForm = new SpellSelectForm();
    statbarForm = new StatusbarForm();
    statbarForm.activate();
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
      if (souNoMana>0) RpgGame.playSoundFX( souNoMana );
      return false;
    }
    
    return super.useMana(amount);
  }

	public PlayerSaveData save() {
		return new PlayerSaveData(this);
	}

  @Override
	public void update() {
		if(isKeyMovement && !(currState==StateHit.class || currState==StateDying.class)) {
			direction = newDir.copy();
			currPath=null;
			pathfindCooldown=0;
			currTarget=null;
			currTargetPos=null;
			changeState(StateMovingStraight.class);
		}

		super.update();
	}

  @Override
	public void interact( Selectable newTarget ) {
		if(currTarget instanceof AbstractItem) {
//		  Game.playSoundFX( SoundData.SOU_PICKUP );
			if( RpgGame.inst.gForms.isFormActive(InventoryForm.class)) {
				inv.pickupItem( (AbstractItem)currTarget );
			} else {
				inv.pickupAuto( (AbstractItem)currTarget );
			}
		}
		super.interact(newTarget);
	}

	public void interactTrigger(UpdateMapObj objInteracting) {
	  
	}

	@Override
	public boolean castSpell( int newSpellID, Vector newTarPos, Selectable newTarget ) {
		boolean superResult = super.castSpell( newSpellID, newTarPos, newTarget );
		if (!superResult) return false;
		
		if( newSpellID==SpellData.MELEE || newSpellID==SpellData.ARROW ) {
		  if (Math.random()<0.05) {
		    AbstractItem tmpWeapon = inv.equip.getBySlot(Equip.INV_WEAPON);
		    tmpWeapon.baseEffects.addOrCreateEffect(EffectAddDurability.class, -1);
		    tmpWeapon.updateEffects();
  			if( (Integer)tmpWeapon.stats.getStatValue(StatDurability.class) <= 0 ) {
  				inv.removeItem(tmpWeapon);
  			}
		  }
		}
		return true;
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

	//----------

	public int getSkillGeneration( int newSkillLevel ) {
		int skillGeneration = (int) Math.floor(newSkillLevel / 5);
		if (skillGeneration>3) skillGeneration=3; //would be 4 is reached skill lvl 2;
		return skillGeneration;
	}

	//----------

	public void improveSkill( int newSkill ) {
		setSkill( newSkill, skills[newSkill]+1 );
	}

	//----------

	public void improveStr() {
	  baseEffects.addOrCreateEffect(EffectAddStrength.class, 1);
		updateStats();
	}

	public void improveDex() {
	  baseEffects.addOrCreateEffect(EffectAddDexterity.class, 1);
		updateStats();
	}

	public void improveVit() {
	  baseEffects.addOrCreateEffect(EffectAddVitality.class, 1);
		updateStats();
	}

	public void improveMag() {
	  baseEffects.addOrCreateEffect(EffectAddMagic.class, 1);
		updateStats();
	}

	public void addXP( int newXP ) {
	  xp += newXP;
	  int newLevel = CharData.getLevelForXP(xp);
	  while(currLevel<newLevel) {
	    levelUp();
	  }
	}

	//----------
	
	public void levelUp() {
	  currLevel++;
	  freeStatPoints += 5;
	  
	  RpgGame.playSoundFX(14);
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

}
