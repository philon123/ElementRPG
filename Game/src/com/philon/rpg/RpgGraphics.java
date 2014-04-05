package com.philon.rpg;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.philon.engine.PhilonGame;
import com.philon.engine.util.Vector;

public class RpgGraphics {

  /**
   * @param newTextureRegion - {@link TextureRegion}
   * @param newPixPos
   *   - origin is top left facing down (bottom right is game.screenPixSize)
   * @param newPixSize
   *   - full screen/window size is game.screenPixSize
   **/
  private void drawTextureRect(SpriteBatch batch, TextureRegion newTexture, Vector newPixPos, Vector newPixSize, Color newColor) {
    Vector tmpPos = newPixPos.copy().divInst(new Vector(PhilonGame.inst.screenPixSize.y));
    Vector tmpSize = newPixSize.copy().divInst(new Vector(PhilonGame.inst.screenPixSize.y));

    batch.setColor(newColor);
    batch.draw(newTexture, tmpPos.x, tmpPos.y, tmpSize.x, tmpSize.y);
    batch.setColor(Color.WHITE);
  }

}
