package com.philon.engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.philon.engine.forms.AbstractForm;
import com.philon.engine.util.Vector;

public class PhilonGame extends Game {
  public static Vector screenPixSize = new Vector(1024, 768);
  public static float ratioXY;
  public static float fps = 60;
  public static int currFrame;

  public static Forms gForms;

  public static Vector getScreenPixSize() {
    return screenPixSize.copy();
  }

  @Override
  public void create() {
    Gdx.graphics.setDisplayMode((int)screenPixSize.x, (int)screenPixSize.y, false);
    ratioXY = screenPixSize.x/screenPixSize.y;
    Texture.setEnforcePotImages(false);

    setScreen(getMainScreen());

    gForms = new Forms();
  }

  public Screen getMainScreen() {
    return new GameScreen();
  }

  @Override
  public void render() {
    if (Gdx.input.isKeyPressed(Keys.ESCAPE)) Gdx.app.exit();
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

    currFrame += 1;

    super.render();
  }

  @Override
  public void resize(int width, int height) {
    screenPixSize = new Vector(width, height);
    ratioXY = screenPixSize.x/screenPixSize.y;

    for (AbstractForm currForm : gForms.activeForms) {
      currForm.screenSizeChangedTrigger(screenPixSize);
    }

    super.resize(width, height);
  }
}
