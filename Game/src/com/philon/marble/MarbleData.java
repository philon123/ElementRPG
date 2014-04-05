package com.philon.marble;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.philon.engine.Data;
import com.philon.engine.util.AnimImage;
import com.philon.engine.util.Vector;

public class MarbleData extends Data {
  public final static int IMG_EMPTY = 0;
  public final static int IMG_RED = 1;
  public final static int IMG_GREEN = 2;
  public final static int IMG_BLUE = 3;
  public final static int IMG_MARBLE = 4;
  public final static int IMG_PLATTFORM_GRASS = 5;

  protected ArrayList<AnimImage> loadTextures() {
    ArrayList<AnimImage> result = new ArrayList<AnimImage>();

    String[] texturesForLoad = new String[]{
        "empty.png",
        "red.png",
        "green.png",
        "blue.png",
        "marble.png",
        "grassplattform.png"
    };

    for(int i=0; i<texturesForLoad.length; i++) {
      String tmpPath = "assets/marble/" + texturesForLoad[i];
      Texture newTexture = new Texture(Gdx.files.internal(tmpPath));
      result.add( new AnimImage(i, newTexture, new Vector(), 1) );
    }

    return result;
  }

  protected ArrayList<Sound> loadSounds() {
    ArrayList<Sound> result = new ArrayList<Sound>();

    String[] soundsForLoad = new String[]{
    };

    for(String currPath : soundsForLoad) {
      String tmpPath = "assets/marble/" + currPath;
      result.add( Gdx.audio.newSound(Gdx.files.internal(tmpPath)) );
    }

    return result;
  }

}
