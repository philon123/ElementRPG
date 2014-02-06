package com.philon.gravity;

import com.badlogic.gdx.graphics.Color;
import com.philon.engine.Game;
import com.philon.engine.Graphics;
import com.philon.engine.util.Vector;

public class GravityGame extends Game {
  Data data;
  Graphics graphics;
  Map currMap;
  
  @Override
  public void create() {
    super.create();
    
    graphics = new Graphics();
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
    for (MassObject currObject : currMap.massObjects) {
      graphics.drawTextureRect(currObject.image.frames[0], currObject.pos, new Vector(currObject.radius*2), Color.WHITE);
    }
    
    graphics.batch.end();
  }
  
}
