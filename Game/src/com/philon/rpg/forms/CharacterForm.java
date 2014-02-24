package com.philon.rpg.forms;

import com.philon.engine.PhilonGame;
import com.philon.engine.forms.AbstractButton;
import com.philon.engine.forms.AbstractForm;
import com.philon.engine.forms.AbstractLabel;
import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
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

public class CharacterForm extends AbstractForm {

  @Override
  public Vector getPosByScreenSize(Vector newScreenSize) {
    return new Vector();
  }

  @Override
  public Vector getSizeByScreenSize(Vector newScreenSize) {
    return new Vector(700, 700);
  }

  public class AddStrButton extends AbstractButton {

    public AddStrButton() {
      ID = 4;
      pos = new Vector(0.32f, 0.15f) ;
      size = new Vector(35.00f, 35.00f) ;
      img = 240;
      imgPressed = 241;
    }

    @Override
    public void execAction() {
      if(RpgGame.inst.localPlayer.freeStatPoints>0) {
        RpgGame.inst.localPlayer.improveStr();
        RpgGame.inst.localPlayer.freeStatPoints--;
      }
    }

  }

  public class AddDexButton extends AbstractButton {

    public AddDexButton() {
      ID = 5;
      pos = new Vector(0.32f, 0.25f) ;
      size = new Vector(35.00f, 35.00f) ;
      img = 240;
      imgPressed = 241;
    }

    @Override
    public void execAction() {
      if(RpgGame.inst.localPlayer.freeStatPoints>0) {
        RpgGame.inst.localPlayer.improveDex();
        RpgGame.inst.localPlayer.freeStatPoints--;
      }
    }

  }

  public class AddVitButton extends AbstractButton {

    public AddVitButton() {
      ID = 6;
      pos = new Vector(0.32f, 0.35f) ;
      size = new Vector(35.00f, 35.00f) ;
      img = 240;
      imgPressed = 241;
    }

    @Override
    public void execAction() {
      if(RpgGame.inst.localPlayer.freeStatPoints>0) {
        RpgGame.inst.localPlayer.improveVit();
        RpgGame.inst.localPlayer.freeStatPoints--;
      }
    }

  }

  public class AddMagButton extends AbstractButton {

    public AddMagButton() {
      ID = 7;
      pos = new Vector(0.32f, 0.45f) ;
      size = new Vector(35.00f, 35.00f) ;
      img = 240;
      imgPressed = 241;
    }

    @Override
    public void execAction() {
      if(RpgGame.inst.localPlayer.freeStatPoints>0) {
        RpgGame.inst.localPlayer.improveMag();
        RpgGame.inst.localPlayer.freeStatPoints--;
      }
    }

  }

  public class BackgroundLabel extends AbstractLabel {

    public BackgroundLabel() {
      ID = 3;
      pos = new Vector(0.00f, 0.00f) ;
      size = new Vector(700.00f, 700.00f) ;
      img = 229;
      displayText = "";
    }

  }

  public class StrengthTextLabel extends AbstractLabel {

    public StrengthTextLabel() {
      ID = 4;
      pos = new Vector(0.05f, 0.15f) ;
      size = new Vector(105.00f, 35.00f) ;
      img = 244;
      displayText = "Strength";
    }

  }

  public class DexterityTextLabel extends AbstractLabel {

    public DexterityTextLabel() {
      ID = 5;
      pos = new Vector(0.05f, 0.25f) ;
      size = new Vector(105.00f, 35.00f) ;
      img = 244;
      displayText = "Dexterity";
    }

  }

  public class VitalityTextLabel extends AbstractLabel {

    public VitalityTextLabel() {
      ID = 6;
      pos = new Vector(0.05f, 0.35f) ;
      size = new Vector(105.00f, 35.00f) ;
      img = 244;
      displayText = "Vitality";
    }

  }

  public class MagicTextLabel extends AbstractLabel {

    public MagicTextLabel() {
      ID = 7;
      pos = new Vector(0.05f, 0.45f) ;
      size = new Vector(105.00f, 35.00f) ;
      img = 244;
      displayText = "Magic";
    }

  }

  public class StrengthLabel extends AbstractLabel {

    public StrengthLabel() {
      ID = 8;
      pos = new Vector(0.21f, 0.15f) ;
      size = new Vector(70.00f, 35.00f) ;
      img = 244;
      displayText = "";
    }

    @Override
    public String getDisplayText() {
      return "" + (Integer)RpgGame.inst.localPlayer.stats.getStatValue(StatStrength.class);
    }

  }

  public class DexterityLabel extends AbstractLabel {

    public DexterityLabel() {
      ID = 9;
      pos = new Vector(0.21f, 0.25f) ;
      size = new Vector(70.00f, 35.00f) ;
      img = 244;
      displayText = "";
    }

    @Override
    public String getDisplayText() {
      return "" + (Integer)RpgGame.inst.localPlayer.stats.getStatValue(StatDexterity.class);
    }

  }

  public class VitalityLabel extends AbstractLabel {

    public VitalityLabel() {
      ID = 10;
      pos = new Vector(0.21f, 0.35f) ;
      size = new Vector(70.00f, 35.00f) ;
      img = 244;
      displayText = "";
    }

    @Override
    public String getDisplayText() {
      return "" + (Integer)RpgGame.inst.localPlayer.stats.getStatValue(StatVitality.class);
    }

  }

  public class MagicLabel extends AbstractLabel {

    public MagicLabel() {
      ID = 11;
      pos = new Vector(0.21f, 0.45f) ;
      size = new Vector(70.00f, 35.00f) ;
      img = 244;
      displayText = "";
    }

    @Override
    public String getDisplayText() {
      return "" + (Integer)RpgGame.inst.localPlayer.stats.getStatValue(StatMagic.class);
    }

  }

  public class DescriptionLabel extends AbstractLabel {

    public DescriptionLabel() {
      ID = 12;
      pos = new Vector(0.05f, 0.05f) ;
      size = new Vector(210.00f, 35.00f) ;
      img = 244;
      displayText = "";
    }

    @Override
    public String getDisplayText() {
      return "Philon, " + RpgGame.inst.localPlayer.getCharText() + " level " + RpgGame.inst.localPlayer.currLevel;
    }

  }

  public class Damage1Label extends AbstractLabel {

    public Damage1Label() {
      ID = 14;
      pos = new Vector(0.58f, 0.15f) ;
      size = new Vector(70.00f, 35.00f) ;
      img = 244;
      displayText = "";
    }

    @Override
    public String getDisplayText() {
      int tmpSpellID = (Integer)RpgGame.inst.localPlayer.stats.getStatValue(StatM1Stype.class);
      int tmpSpellLevel = RpgGame.inst.localPlayer.stats.spells[tmpSpellID];
      Vector dispDmg = SpellData.getTotalDmg( tmpSpellID, tmpSpellLevel );

      return (int)dispDmg.x + " - " + (int)dispDmg.y;
    }

  }

  public class Damage2Label extends AbstractLabel {

    public Damage2Label() {
      ID = 16;
      pos = new Vector(0.85f, 0.15f) ;
      size = new Vector(70.00f, 35.00f) ;
      img = 244;
      displayText = "";
    }

    @Override
    public String getDisplayText() {
      int tmpSpellID = (Integer)RpgGame.inst.localPlayer.stats.getStatValue(StatM2Stype.class);
      int tmpSpellLevel = RpgGame.inst.localPlayer.stats.spells[tmpSpellID];
      Vector dispDmg = SpellData.getTotalDmg( tmpSpellID, tmpSpellLevel );

      return (int)dispDmg.x + " - " + (int)dispDmg.y;
    }

  }

  public class ArmorLabel extends AbstractLabel {

    public ArmorLabel() {
      ID = 18;
      pos = new Vector(0.58f, 0.25f) ;
      size = new Vector(70.00f, 35.00f) ;
      img = 244;
      displayText = "";
    }

    @Override
    public String getDisplayText() {
      return "" + (Integer)RpgGame.inst.localPlayer.stats.getStatValue(StatArmor.class);
    }

  }

  public class Damage1TextLabel extends AbstractLabel {

    public Damage1TextLabel() {
      ID = 13;
      pos = new Vector(0.42f, 0.15f) ;
      size = new Vector(105.00f, 35.00f) ;
      img = 244;
      displayText = "Damage 1:";
    }

  }

  public class Damage2TextLabel extends AbstractLabel {

    public Damage2TextLabel() {
      ID = 15;
      pos = new Vector(0.69f, 0.15f) ;
      size = new Vector(105.00f, 35.00f) ;
      img = 244;
      displayText = "Damage 2:";
    }

  }

  public class ArmorTextLabel extends AbstractLabel {

    public ArmorTextLabel() {
      ID = 17;
      pos = new Vector(0.42f, 0.25f) ;
      size = new Vector(105.00f, 35.00f) ;
      img = 244;
      displayText = "Armor:";
    }

  }

  public class DamageReduceTextLabel extends AbstractLabel {

    public DamageReduceTextLabel() {
      ID = 19;
      pos = new Vector(0.69f, 0.25f) ;
      size = new Vector(105.00f, 35.00f) ;
      img = 244;
      displayText = "Dmg red.";
    }

  }

  public class DamageReduceLabel extends AbstractLabel {

    public DamageReduceLabel() {
      ID = 20;
      pos = new Vector(0.85f, 0.25f) ;
      size = new Vector(70.00f, 35.00f) ;
      img = 244;
      displayText = "";
    }

    @Override
    public String getDisplayText() {
      return "" + (Integer)RpgGame.inst.localPlayer.stats.getStatValue(StatReduceDmgTaken.class) * -1;
    }

  }

  public class HealthTextLabel extends AbstractLabel {

    public HealthTextLabel() {
      ID = 21;
      pos = new Vector(0.42f, 0.35f) ;
      size = new Vector(105.00f, 35.00f) ;
      img = 244;
      displayText = "Health:";
    }

  }

  public class HealthLabel extends AbstractLabel {

    public HealthLabel() {
      ID = 22;
      pos = new Vector(0.58f, 0.35f) ;
      size = new Vector(105.00f, 35.00f) ;
      img = 244;
      displayText = "";
    }

    @Override
    public String getDisplayText() {
      return "" +
          (Integer)RpgGame.inst.localPlayer.stats.getStatValue(StatHealth.class) +
          "/" +
          (Integer)RpgGame.inst.localPlayer.stats.getStatValue(StatMaxHealth.class);
    }

  }

  public class ResistFireTextLabel extends AbstractLabel {

    public ResistFireTextLabel() {
      ID = 23;
      pos = new Vector(0.21f, 0.70f) ;
      size = new Vector(140.00f, 35.00f) ;
      img = 244;
      displayText = "Resist Fire:";
    }

  }

  public class ResistLightningTextLabel extends AbstractLabel {

    public ResistLightningTextLabel() {
      ID = 24;
      pos = new Vector(0.21f, 0.80f) ;
      size = new Vector(140.00f, 35.00f) ;
      img = 244;
      displayText = "Resist Lightning:";
    }

  }

  public class ResistMagicTextLabel extends AbstractLabel {

    public ResistMagicTextLabel() {
      ID = 25;
      pos = new Vector(0.21f, 0.90f) ;
      size = new Vector(140.00f, 35.00f) ;
      img = 244;
      displayText = "Resist Magic:";
    }

  }

  public class ResistFireLabel extends AbstractLabel {

    public ResistFireLabel() {
      ID = 26;
      pos = new Vector(0.42f, 0.70f) ;
      size = new Vector(70.00f, 35.00f) ;
      img = 244;
      displayText = "";
    }

    @Override
    public String getDisplayText() {
      return "" + Util.getSignedPercentageString( (Float)RpgGame.inst.localPlayer.stats.getStatValue(StatResistFire.class) );
    }

  }

  public class ResistLightningLabel extends AbstractLabel {

    public ResistLightningLabel() {
      ID = 27;
      pos = new Vector(0.42f, 0.80f) ;
      size = new Vector(70.00f, 35.00f) ;
      img = 244;
      displayText = "";
    }

    @Override
    public String getDisplayText() {
      return "" + Util.getSignedPercentageString( (Float)RpgGame.inst.localPlayer.stats.getStatValue(StatResistLightning.class) );
    }

  }

  public class ResistMagicLabel extends AbstractLabel {

    public ResistMagicLabel() {
      ID = 28;
      pos = new Vector(0.42f, 0.90f) ;
      size = new Vector(70.00f, 35.00f) ;
      img = 244;
      displayText = "";
    }

    @Override
    public String getDisplayText() {
      return "" + Util.getSignedPercentageString( (Float)RpgGame.inst.localPlayer.stats.getStatValue(StatResistIce.class) );
    }

  }

  public class ManaTextLabel extends AbstractLabel {

    public ManaTextLabel() {
      ID = 29;
      pos = new Vector(0.42f, 0.45f) ;
      size = new Vector(105.00f, 35.00f) ;
      img = 244;
      displayText = "Mana:";
    }

  }

  public class ManaLabel extends AbstractLabel {

    public ManaLabel() {
      ID = 30;
      pos = new Vector(0.58f, 0.45f) ;
      size = new Vector(105.00f, 35.00f) ;
      img = 244;
      displayText = "";
    }

    @Override
    public String getDisplayText() {
      return "" +
          (Integer)RpgGame.inst.localPlayer.stats.getStatValue(StatMana.class) +
          "/" +
          (Integer)RpgGame.inst.localPlayer.stats.getStatValue(StatMaxMana.class);
    }

  }

  public class CloseButton extends AbstractButton {

    public CloseButton() {
      ID = 3;
      pos = new Vector(0.94f, 0.01f) ;
      size = new Vector(35.00f, 35.00f) ;
      img = 242;
      imgPressed = 243;
    }

    @Override
    public void execAction() {
      PhilonGame.gForms.removeForm( CharacterForm.this );
    }

  }

}
