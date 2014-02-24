package com.philon.rpg.forms;

import com.badlogic.gdx.graphics.Color;
import com.philon.engine.PhilonGame;
import com.philon.engine.forms.AbstractForm;
import com.philon.engine.forms.AbstractLabel;
import com.philon.engine.util.Vector;
import com.philon.rpg.ImageData;
import com.philon.rpg.RpgGame;
import com.philon.rpg.SkillData;
import com.philon.rpg.spell.SpellData;
import com.philon.rpg.stat.StatsObj.StatDefaultSpell;
import com.philon.rpg.stat.StatsObj.StatM1Stype;
import com.philon.rpg.stat.StatsObj.StatM2Stype;

public class SpellSelectForm extends AbstractForm {

  @Override
  public Vector getPosByScreenSize(Vector newScreenSize) {
    return newScreenSize.copy().
        mulInst(new Vector(0.5f, 0.8f)).
        subInst(size.copy().mulScalarInst(0.5f));
  }

  @Override
  public Vector getSizeByScreenSize(Vector newScreenSize) {
    return new Vector(640, 256);
  }

  public class BackgroundLabel extends AbstractLabel {

    public BackgroundLabel() {
      ID = 45;
      pos = new Vector(0.00f, 0.00f) ;
      size = new Vector(640.00f, 256.00f) ;
      img = 0;
      displayText = "";
    }

  }

  public class SpellSelectLabel extends AbstractLabel {

    public SpellSelectLabel() {
      pos = new Vector(0.00f, 0.00f) ;
      size = new Vector(640.00f, 256.00f) ;
      img = 0;
      displayText = "";
    }

    @Override
    public void handleClickLeft(Vector clickedPixel) {
      Vector clickedCell = clickedPixel.divInst(new Vector(64)).floorAllInst();
      int clickedSpell = getSpellBySpellSelectTile(clickedCell);
      if( RpgGame.inst.localPlayer.stats.spells[clickedSpell] > 0 ) {
        if( RpgGame.inst.gInput.isSelectingLeft ) {
          RpgGame.inst.localPlayer.stats.addOrCreateStat(StatM1Stype.class, clickedSpell); //TODO propper spell select
        } else {
          RpgGame.inst.localPlayer.stats.addOrCreateStat(StatM2Stype.class, clickedSpell);
        }
        PhilonGame.gForms.removeForm(SpellSelectForm.this);
      }
    }

    @Override
    public void draw(Vector containerPos, Vector containerSize) {
      super.draw(containerPos, containerSize);

      Vector tmpSize = new Vector(64);
      Vector tmpTile;
      int currSpell;
      for( int i = 0; i <= RpgGame.inst.localPlayer.skills.length-1; i++ ) {
        tmpTile = new Vector(i % 10, (int)(i/10));
        currSpell = getSpellBySpellSelectTile( tmpTile );
        Vector tmpPos = tmpTile.mulInst(tmpSize).addInst( containerPos );
        Color tmpColor;
        if( RpgGame.inst.localPlayer.stats.spells[currSpell]==0 ) {
          tmpColor = Color.DARK_GRAY;
        } else {
          tmpColor = Color.WHITE;
        }
        RpgGame.inst.gGraphics.drawTextureRect( ImageData.images[SpellData.iconImg[currSpell]].frames[0], tmpPos, tmpSize, tmpColor );
      }
    }

    public int getSpellBySpellSelectTile( Vector newSpellSelectTile ) {
      int clickedTile = (int) ((newSpellSelectTile.y*10) + newSpellSelectTile.x);
      if (clickedTile>RpgGame.inst.localPlayer.skills.length-1) return 0;

      if (clickedTile==0) {
        return (Integer)RpgGame.inst.localPlayer.stats.getStatValue(StatDefaultSpell.class);
      } else {
        return SkillData.spellsForSkill[clickedTile][ RpgGame.inst.localPlayer.getSkillGeneration(RpgGame.inst.localPlayer.skills[clickedTile]) ];
      }
    }

  }

}
