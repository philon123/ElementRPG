package com.philon.rpg.forms;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.philon.engine.Data;
import com.philon.engine.event.ButtonInputListener;
import com.philon.engine.forms.GuiElement;
import com.philon.engine.input.Controller.Button1;
import com.philon.engine.input.Controller.MouseCursor;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.SkillData;
import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.mos.player.AbstractChar;
import com.philon.rpg.spell.SpellData;
import com.philon.rpg.stat.StatsObj.StatDefaultSpell;
import com.philon.rpg.stat.StatsObj.StatM1Stype;
import com.philon.rpg.stat.StatsObj.StatM2Stype;

public class SpellSelectForm extends GuiElement {
  private boolean isSelectingForLeft;

  public SpellSelectForm() {
    addInputListener(Button1.class, new ButtonInputListener() {
      @Override
      protected boolean execUp() {
        Vector cursorPos = RpgGame.inst.exclusiveUser.controller.getElementByClass(MouseCursor.class).pos;
        Vector relCursorPos = Vector.sub(cursorPos, absPos).divInst(absSize);
        if(relCursorPos.isEitherSmaller(new Vector()) || relCursorPos.isEitherLarger(new Vector(1))) return false;

        Vector clickedCell = relCursorPos.mulInst(getConfiguredGridSize()).floorAllInst();
        int clickedSpell = getSpellBySpellSelectTile(clickedCell);
        CombatMapObj character = RpgGame.inst.getExclusiveUser().character;
        if( character.stats.spells[clickedSpell] > 0 ) {
          if( isSelectingForLeft ) {
            character.stats.addOrCreateStat(StatM1Stype.class, clickedSpell);
          } else {
            character.stats.addOrCreateStat(StatM2Stype.class, clickedSpell);
          }
          RpgGame.inst.guiHierarchy.removeElementByClass(SpellSelectForm.class);
        }
        return true;
      }
    });
  }

  public void setIsSelectingForLeft(boolean isSFL) {
    isSelectingForLeft = isSFL;
  }

  private Vector getConfiguredGridSize() {
    return new Vector(10, 4);
  }

  @Override
  protected boolean isStrechable() {
    return false;
  }
  @Override
  protected float getConfiguredXYRatio() {
    return 10f/4f;
  }
  @Override
  protected float getConfiguredScale() {
    return 0.33f;
  }
  @Override
  protected int getConfiguredAlignment() {
    return GuiElement.ALIGN_CENTER;
  }

  @Override
  public void execDraw(SpriteBatch batch) {
    super.execDraw(batch);

    AbstractChar character = RpgGame.inst.getExclusiveUser().character;
    Vector tileRelSize = new Vector(1/10f, 1/4f);
    Vector tmpTile;
    int currSpell;
    for( int i = 0; i < character.skills.length; i++ ) {
      tmpTile = new Vector(i % 10, (int)(i/10));
      currSpell = getSpellBySpellSelectTile( tmpTile );
      Vector tmpPos = Vector.add( absPos, Vector.mul(tmpTile.mulInst(tileRelSize), absSize) );
      Color tmpColor;
      if( character.stats.spells[currSpell]==0 ) {
        tmpColor = Color.DARK_GRAY;
      } else {
        tmpColor = Color.WHITE;
      }
      batch.setColor(tmpColor);
      batch.draw(Data.textures.get(SpellData.iconImg[currSpell]).frames[0], tmpPos.x, tmpPos.y, tileRelSize.x, tileRelSize.y);
      batch.setColor(Color.WHITE);
    }
  }

  public int getSpellBySpellSelectTile( Vector newSpellSelectTile ) {
    int clickedTile = (int) ((newSpellSelectTile.y*10) + newSpellSelectTile.x);
    AbstractChar character = RpgGame.inst.getExclusiveUser().character;
    if (clickedTile>character.skills.length-1) return 0;

    if (clickedTile==0) {
      return (Integer)character.stats.getStatValue(StatDefaultSpell.class);
    } else {
      return SkillData.spellsForSkill[clickedTile][ character.getSkillGeneration(character.skills[clickedTile]) ];
    }
  }

}
