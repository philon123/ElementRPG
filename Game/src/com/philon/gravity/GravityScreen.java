package com.philon.gravity;

import java.util.LinkedList;

import com.philon.engine.GameScreen;

public class GravityScreen extends GameScreen {
  public static Map currMap;
  public static Data data;

  @Override
  public void create() {
    super.create();

    data = new Data();
    changeLevel(0);
  }

  @Override
  public void render(float delta) {
    super.render(delta);

    GravityGame.graphics.batch.begin();

    GravityGame.graphics.updateScaling();
    currMap.updateMassObjects();
    drawAll();

    GravityGame.graphics.batch.end();
  }

  public void drawAll() {
    for (LinkedList<Drawable> currZList : GravityGame.renderMap.renderMap.values()) {
      for (Drawable currDrawale : currZList) {
        currDrawale.draw(GravityGame.graphics.batch);
      }
    }
  }

  public void changeLevel(int newLevel) {
    currMap = new Map();
    currMap.build(data.levelData.get(newLevel));
  }

}
