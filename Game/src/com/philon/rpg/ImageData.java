package com.philon.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.philon.engine.AbstractGameData;
import com.philon.engine.util.Vector;
import com.philon.rpg.util.GameImage;

public class ImageData extends AbstractGameData {
  public static Vector defaultImgSize = new Vector(1, 1.818f);
  
  public static final int IMG_FORM_BACK_LEFT = 229;
  public static final int IMG_FORM_BACK_RIGHT = 228;
  public static final int IMG_FORM_BACK_SLOT  = 230;
  public static final int IMG_FORM_BACK_POPUP = 231;
  public static final int IMG_FORM_ORB_HEALTH = 237;
  public static final int IMG_MAP_TILE_DEFAULT = 298; 
  public static final int IMG_MENU_TITLESCREEN = 304;
  
  public static final int IMG_MAP_WALL_STRAIGHT = 299;
  public static final int IMG_MAP_WALL_CORNER = 305;
  public static final int IMG_MAP_WALL_TRIPOD = 306;
  public static final int IMG_MAP_WALL_CROSS = 307;
  public static final int IMG_MAP_WALL_STUB = 329;
  
  public static final int IMG_MAP_WALL_BLOCK = 331;
  public static final int IMG_MAP_WALL_BLOCK_SINGLESIDE = 328;
  public static final int IMG_MAP_WALL_BLOCK_CORNER = 332;
  public static final int IMG_MAP_WALL_BLOCK_CROSS = 330;
  
  public static final int IMG_MAP_DOOR = 333;
  public static final int IMG_MAP_TORCH = 339;
  public static final int IMG_MAP_FIRESTAND = 340;
  
  public static GameImage[] images;
  public static Sound souMusic;
  
  @Override
  public String getTableName() {
    return "image";
  }
  
  @Override
  public void execInitArrays(int rowCount) {
    images = new GameImage[rowCount];
  }
  
  @Override
  public void execLoadRow(int rowNum, Object[] row) {
    String tmpPath = (String) row[1];
    if (tmpPath==null) return;
//    tmpPath = "C:/eclipse/workspace/" + tmpPath;
    Vector tmpFrameSize = new Vector((Integer) row[4], (Integer) row[5]);
    Texture tmpTexture = new Texture(Gdx.files.internal("assets/" + tmpPath));
    images[rowNum] = new GameImage(tmpTexture, tmpFrameSize);
  }

}
