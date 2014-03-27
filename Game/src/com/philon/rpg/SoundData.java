package com.philon.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundData extends AbstractGameData {
  public static final int SOU_FOOTSTEP =  6;
  public static final int SOU_PICKUP   = 20;

  public static Sound[] sounds;
  
  public static Music souMusic;
  
  //----------
  
  @Override
  public String getTableName() {
    return "sound";
  }
  
  //----------
  
  @Override
  public void execInitArrays(int rowCount) {
    sounds = new Sound[rowCount];
  }
  
  //----------
  
  @Override
  public void execLoadRow(int rowNum, Object[] row) {
    String tmpPath = (String)row[1];
    if (tmpPath==null) return;
    tmpPath = "assets/" + tmpPath;
    sounds[rowNum] = Gdx.audio.newSound(Gdx.files.internal(tmpPath));
  }

  //----------
  
  @Override
  protected void execFinishedLoad() {
    souMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/" + "Data/Media/music/dlvla(huge).wav"));
    souMusic.setLooping(true);
  }
  
  //----------
  
}
