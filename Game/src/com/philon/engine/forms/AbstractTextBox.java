package com.philon.engine.forms;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.util.RpgUtil;

public abstract class AbstractTextBox extends GuiElement {

  @Override
  protected void execRender(SpriteBatch batch, float deltaTime) {
    super.execRender(batch, deltaTime);

    RpgGame.inst.graphics.drawText( batch, RpgUtil.font, getDisplayText(), Vector.add(absPos, new Vector(0.01f)) );
  }

  public abstract String getDisplayText();

}