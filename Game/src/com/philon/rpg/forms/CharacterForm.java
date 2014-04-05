package com.philon.rpg.forms;

import com.philon.engine.event.ButtonInputListener;
import com.philon.engine.forms.AbstractButton;
import com.philon.engine.forms.AbstractTextBox;
import com.philon.engine.forms.GuiElement;
import com.philon.engine.input.Controller.MouseButton1;
import com.philon.engine.util.Util;
import com.philon.engine.util.Util.Order;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.mos.player.AbstractChar;
import com.philon.rpg.spell.SpellData;
import com.philon.rpg.stat.StatsObj.StatArmor;
import com.philon.rpg.stat.StatsObj.StatDexterity;
import com.philon.rpg.stat.StatsObj.StatHealth;
import com.philon.rpg.stat.StatsObj.StatM1Stype;
import com.philon.rpg.stat.StatsObj.StatM2Stype;
import com.philon.rpg.stat.StatsObj.StatMagic;
import com.philon.rpg.stat.StatsObj.StatMana;
import com.philon.rpg.stat.StatsObj.StatMaxHealth;
import com.philon.rpg.stat.StatsObj.StatMaxMana;
import com.philon.rpg.stat.StatsObj.StatReduceDmgTaken;
import com.philon.rpg.stat.StatsObj.StatResistFire;
import com.philon.rpg.stat.StatsObj.StatResistIce;
import com.philon.rpg.stat.StatsObj.StatResistLightning;
import com.philon.rpg.stat.StatsObj.StatStrength;
import com.philon.rpg.stat.StatsObj.StatVitality;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddDexterity;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddMagic;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddStrength;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddVitality;

@Order(10)
public class CharacterForm extends GuiElement {

  public CharacterForm() {
    addInputListener(MouseButton1.class, new ButtonInputListener() {
      @Override
      protected boolean execDown() {
        return true;
      }
    });
  }

  @Override
  protected boolean isStrechable() {
    return false;
  }
  @Override
  protected float getConfiguredXYRatio() {
    return 1;
  }
  @Override
  protected float getConfiguredScale() {
    return 0.75f;
  }
  @Override
  protected int getConfiguredAlignment() {
    return GuiElement.ALIGN_TOP_LEFT;
  }
  @Override
  protected int getConfiguredBackground() {
    return 229;
  }

//don't need a close button anymore
//  public class CloseButton extends AbstractButton {
//    @Override
//    protected int getConfiguredBackground() {
//      return 242;
//    }
//    @Override
//    protected int getConfiguredImgPressed() {
//      return 243;
//    }
//    @Override
//    protected float getConfiguredXYRatio() {
//      return 1;
//    }
//    @Override
//    protected float getConfiguredScale() {
//      return 0.05f;
//    }
//    @Override
//    protected Vector getConfiguredPosition() {
//      return new Vector(0.95f, 0.01f);
//    }
//    @Override
//    public void execAction() {
//      RpgGame.inst.guiHierarchy.removeElementByClass(CharacterForm.class);
//    }
//  }

  public class AddStrButton extends AbstractButton {
    @Override
    public void execAction() {
      AbstractChar character = RpgGame.inst.getExclusiveUser().character;
      if(character.freeStatPoints>0) {
        character.baseEffects.addOrCreateEffect(EffectAddStrength.class, 1);
        character.updateStats();
        character.freeStatPoints--;
      }
    }
    @Override
    protected int getConfiguredBackground() {
      return 240;
    }
    @Override
    protected int getConfiguredImgPressed() {
      return 241;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 1;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.32f, 0.15f) ;
    }
  }

  public class AddDexButton extends AbstractButton {
    @Override
    public void execAction() {
      AbstractChar character = RpgGame.inst.getExclusiveUser().character;
      if(character.freeStatPoints>0) {
        character.baseEffects.addOrCreateEffect(EffectAddDexterity.class, 1);
        character.updateStats();
        character.freeStatPoints--;
      }
    }
    @Override
    protected int getConfiguredBackground() {
      return 240;
    }
    @Override
    protected int getConfiguredImgPressed() {
      return 241;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 1;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.32f, 0.25f) ;
    }
  }

  public class AddVitButton extends AbstractButton {
    @Override
    public void execAction() {
      AbstractChar character = RpgGame.inst.getExclusiveUser().character;
      if(character.freeStatPoints>0) {
        character.baseEffects.addOrCreateEffect(EffectAddVitality.class, 1);
        character.updateStats();
        character.freeStatPoints--;
      }
    }
    @Override
    protected int getConfiguredBackground() {
      return 240;
    }
    @Override
    protected int getConfiguredImgPressed() {
      return 241;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 1;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.32f, 0.35f) ;
    }
  }

  public class AddMagButton extends AbstractButton {
    @Override
    public void execAction() {
      AbstractChar character = RpgGame.inst.getExclusiveUser().character;
      if(character.freeStatPoints>0) {
        character.baseEffects.addOrCreateEffect(EffectAddMagic.class, 1);
        character.updateStats();
        character.freeStatPoints--;
      }
    }
    @Override
    protected int getConfiguredBackground() {
      return 240;
    }
    @Override
    protected int getConfiguredImgPressed() {
      return 241;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 1;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.32f, 0.45f) ;
    }
  }

  public class StrengthTextLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 3;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.05f, 0.15f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "Strength";
    }
  }

  public class DexterityTextLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 3;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.05f, 0.25f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "Dexterity";
    }
  }

  public class VitalityTextLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 3;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.05f, 0.35f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "Vitality";
    }
  }

  public class MagicTextLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 3;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.05f, 0.45f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "Magic";
    }
  }

  public class StrengthLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 2;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.21f, 0.15f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "" + (Integer) RpgGame.inst.getExclusiveUser().character.stats.getStatValue(StatStrength.class);
    }
  }

  public class DexterityLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 2;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.21f, 0.25f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "" + (Integer) RpgGame.inst.getExclusiveUser().character.stats.getStatValue(StatDexterity.class);
    }
  }

  public class VitalityLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 2;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.21f, 0.35f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "" + (Integer) RpgGame.inst.getExclusiveUser().character.stats.getStatValue(StatVitality.class);
    }
  }

  public class MagicLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 2;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.21f, 0.45f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "" + (Integer) RpgGame.inst.getExclusiveUser().character.stats.getStatValue(StatMagic.class);
    }
  }

  public class DescriptionLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 6;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.05f, 0.05f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      AbstractChar character = RpgGame.inst.getExclusiveUser().character;
      return character.getCharText() + " lvl " +  character.currLevel;
    }
  }

  public class Damage1Label extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 2;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.58f, 0.15f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      AbstractChar character = RpgGame.inst.getExclusiveUser().character;
      int tmpSpellID = (Integer)character.stats.getStatValue(StatM1Stype.class);
      int tmpSpellLevel = character.stats.spells[tmpSpellID];
      Vector dispDmg = SpellData.getTotalDmg( character, tmpSpellID, tmpSpellLevel );

      return (int)dispDmg.x + "-" + (int)dispDmg.y;
    }
  }

  public class Damage2Label extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 2;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.85f, 0.15f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      AbstractChar character = RpgGame.inst.getExclusiveUser().character;
      int tmpSpellID = (Integer)character.stats.getStatValue(StatM2Stype.class);
      int tmpSpellLevel = character.stats.spells[tmpSpellID];
      Vector dispDmg = SpellData.getTotalDmg( character, tmpSpellID, tmpSpellLevel );

      return (int)dispDmg.x + "-" + (int)dispDmg.y;
    }
  }

  public class ArmorLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 2;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.58f, 0.25f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "" + (Integer)RpgGame.inst.getExclusiveUser().character.stats.getStatValue(StatArmor.class);
    }
  }

  public class Damage1TextLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 3;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.42f, 0.15f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "Dmg 1";
    }
  }

  public class Damage2TextLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 3;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.69f, 0.15f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "Dmg 2";
    }
  }

  public class ArmorTextLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 3;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.42f, 0.25f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "Armor";
    }
  }

  public class DamageReduceTextLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 3;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.69f, 0.25f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "Dmg Red";
    }
  }

  public class DamageReduceLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 2;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.85f, 0.25f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "" + (Integer)RpgGame.inst.getExclusiveUser().character.stats.getStatValue(StatReduceDmgTaken.class) * -1;
    }
  }

  public class HealthTextLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 3;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.42f, 0.35f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "Health";
    }
  }

  public class HealthLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 3;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.58f, 0.35f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      AbstractChar character = RpgGame.inst.getExclusiveUser().character;
      return "" +
          (Integer)character.stats.getStatValue(StatHealth.class) +
          "/" +
          (Integer)character.stats.getStatValue(StatMaxHealth.class);
    }
  }

  public class ResistFireTextLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 4;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.21f, 0.70f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "Fire Res";
    }
  }

  public class ResistLightningTextLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 4;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.21f, 0.80f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "Electric Res";
    }
  }

  public class ResistMagicTextLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 4;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.21f, 0.90f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "Ice Res";
    }
  }

  public class ResistFireLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 2;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.42f, 0.70f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "" + Util.getSignedPercentageString(
          (Float)RpgGame.inst.getExclusiveUser().character.stats.getStatValue(StatResistFire.class)
      );
    }
  }

  public class ResistLightningLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 2;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.42f, 0.80f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "" + Util.getSignedPercentageString(
          (Float)RpgGame.inst.getExclusiveUser().character.stats.getStatValue(StatResistLightning.class)
      );
    }
  }

  public class ResistIceLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 2;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.42f, 0.90f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "" + Util.getSignedPercentageString(
          (Float)RpgGame.inst.getExclusiveUser().character.stats.getStatValue(StatResistIce.class)
      );
    }
  }

  public class ManaTextLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 3;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.42f, 0.45f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      return "Mana";
    }
  }

  public class ManaLabel extends AbstractTextBox {
    @Override
    protected boolean isStrechable() {
      return false;
    }
    @Override
    protected float getConfiguredXYRatio() {
      return 3;
    }
    @Override
    protected float getConfiguredScale() {
      return 0.05f;
    }
    @Override
    protected Vector getConfiguredPosition() {
      return new Vector(0.58f, 0.45f);
    }
    @Override
    protected int getConfiguredBackground() {
      return 244;
    }
    @Override
    public String getDisplayText() {
      AbstractChar character = RpgGame.inst.getExclusiveUser().character;
      return "" +
          (Integer)character.stats.getStatValue(StatMana.class) +
          "/" +
          (Integer)character.stats.getStatValue(StatMaxMana.class);
    }
  }

}
