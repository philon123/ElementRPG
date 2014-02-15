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
    level1.addObject(textures.get(IMG_STAR), new Vector(350, 250), 1000000f, 10f, 5f, new Vector(3, 0));
    level1.addObject(textures.get(IMG_STAR), new Vector(350, 450), 1000000f, 10f, 0.5f, new Vector(-3, 0));
    level1.addObject(textures.get(IMG_ASTEROID), new Vector(350, 0), 100000f, 10f, 0f, new Vector(7, 0));
    level1.addObject(textures.get(IMG_ASTEROID), new Vector(350, 800), 100000f, 10f, 0f, new Vector(7, 0));
    levelData.add(level1);
  }
  
}
