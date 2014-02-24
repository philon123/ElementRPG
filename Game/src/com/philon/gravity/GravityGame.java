package com.philon.gravity;

import com.badlogic.gdx.Screen;
import com.philon.engine.PhilonGame;

public class GravityGame extends PhilonGame {
  public static GravityGraphics graphics;
  public static RenderMap renderMap;

  @Override
  public void create() {
    graphics = new GravityGraphics();
    renderMap = new RenderMap();

    super.create();
  }

  @Override
  public void resize(int width, int height) {
    super.resize(width, height);

    graphics.resizedTrigger(screenPixSize);
  }

  @Override
  public Screen getMainScreen() {
    return new GravityScreen();
  }

}
