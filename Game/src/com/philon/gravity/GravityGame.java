package com.philon.gravity;

import com.philon.engine.Game;

public class GravityGame extends Game {
  public static Data data;
  public static GravityGraphics graphics;
  public static Map currMap;
  
  @Override
  public void create() {
    super.create();
    
    graphics = new GravityGraphics();
    data = new Data();
    
    changeLevel(0);
  }
  
  public void changeLevel(int newLevel) {
    currMap = new Map();
    currMap.build(data.levelData.get(newLevel));
  }
  
  @Override
  public void render() {
    super.render();
    
    graphics.batch.begin();
    
    currMap.updateMassObjects();
    graphics.updateScaling();
    graphics.drawAll();
    
    graphics.batch.end();
  }
  
}
