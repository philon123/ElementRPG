package com.philon.gravity;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.philon.engine.util.AnimTextureRegion;
import com.philon.engine.util.Vector;

public class Data {
  public final static int IMG_STAR = 0;
  public final static int IMG_ASTEROID = 1;
  public final static int IMG_GLOW = 2;
  
  ArrayList<AnimTextureRegion> textures;
  ArrayList<Sound> sounds;
  
  ArrayList<LevelData> levelData;
  
  public Data() {
    //load textures
    textures = new ArrayList<AnimTextureRegion>();
    
    String[] texturesForLoad = new String[]{
        "star.png", 
        "asteroid.png", 
        "glow.png"
      };
    
    for (String tmpPath : texturesForLoad) {
      AnimTextureRegion tmpAnimTexture = new AnimTextureRegion(
          new Texture(Gdx.files.internal("assets/gravity/" + tmpPath)), new Vector()
          );
      textures.add(tmpAnimTexture);
    }
    
    //load sound
    sounds = new ArrayList<Sound>();
    
    //load levels
    levelData = new ArrayList<LevelData>();
    
    LevelData level1 = new LevelData();
    level1.addObject(textures.get(IMG_STAR), new Vector(100, 100), 10000f, 50f, 0.5f, new Vector(1, 0), 10f);
    level1.addObject(textures.get(IMG_STAR), new Vector(300, 100), 10000f, 50f, 0.5f, new Vector(0, 1), 10f);
    levelData.add(level1);
  }
  
}
