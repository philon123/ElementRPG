package com.philon.engine;

import java.util.ArrayList;

import com.badlogic.gdx.audio.Sound;
import com.philon.engine.util.AnimImage;

public class Data {
  public static ArrayList<AnimImage> textures;
  public static ArrayList<Sound> sounds;

  public void loadAll() {
    textures = loadTextures();
    sounds = loadSounds();
  }

  protected ArrayList<AnimImage> loadTextures() {
    ArrayList<AnimImage> result = new ArrayList<AnimImage>();
    return result;
  }

  protected ArrayList<Sound> loadSounds() {
    ArrayList<Sound> result = new ArrayList<Sound>();
    return result;
  }

}
