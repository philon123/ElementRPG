package com.philon.rpg;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.philon.engine.Data;
import com.philon.engine.Database;
import com.philon.engine.util.AnimImage;
import com.philon.engine.util.Vector;
import com.philon.rpg.mos.enemy.EnemyData;
import com.philon.rpg.mos.item.ItemData;
import com.philon.rpg.mos.player.CharData;
import com.philon.rpg.stat.presuf.PrefixSuffixData;

public class RpgData extends Data {
  public static final int SOU_FOOTSTEP =  6;
  public static final int SOU_PICKUP   = 20;

  public static Database db;

  public static Music souMusic;

  @Override
  public void loadAll() {
    db = new Database("assets/data/db/Game.db");
    super.loadAll();

    souMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/" + "Data/Media/music/dlvla(huge).wav"));
    souMusic.setLooping(true);

    //with db

    //without db
    ItemData.loadMedia();
    PrefixSuffixData.loadMedia();
    EnemyData.loadMedia();
    CharData.loadMedia();
  }

  @Override
  protected ArrayList<AnimImage> loadTextures() {
    Object[][] newData = db.execQuery("SELECT * FROM image ORDER BY id ASC");
    ArrayList<AnimImage> result = new ArrayList<AnimImage>(newData.length);

    for(Object[] currRow : newData) {
      int index = (Integer)currRow[0];
      String tmpPath = (String)currRow[1];
      if (tmpPath==null) {
        result.add(index, null);
        continue;
      }
      Vector tmpFrameSize = new Vector( (Integer)currRow[3], (Integer)currRow[4] );
      float scaleByX = new Float((Double)currRow[5]);
      Vector origin = new Vector((Integer)currRow[6], (Integer)currRow[7]);

      Texture tmpTexture = new Texture(Gdx.files.internal("assets/" + tmpPath));
      result.add(index, new AnimImage(index, tmpTexture, tmpFrameSize, scaleByX, origin));
    }

    return result;
  }

  @Override
  protected ArrayList<Sound> loadSounds() {
    Object[][] newData = db.execQuery("SELECT * FROM sound ORDER BY id ASC");
    ArrayList<Sound> result = new ArrayList<Sound>(newData.length);

    for(Object[] currRow : newData) {
      int index = (Integer)currRow[0];
      String tmpPath = (String)currRow[1];
      if (tmpPath==null) {
        result.add(index, null);
        continue;
      }
      result.add( index, Gdx.audio.newSound(Gdx.files.internal("assets/" + tmpPath)) );
    }

    return result;
  }
}
