package com.philon.rpg.mos.wall;

import com.philon.engine.util.Vector;
import com.philon.rpg.ImageData;

public abstract class WallData {
  public static int numWallStyles = 5;
  public static int STYLE_TOWN = 0;
  public static int STYLE_CELLAR = 1;
  public static int STYLE_DUNGEON = 2;
  public static int STLYE_CAVES = 3;
  public static int STYLE_HELL = 4;
  
	public static int numWallImgs = 9;
	public static int IMG_BLOCK = 0;
	public static int IMG_BLOCK_SINGLESIDE = 1;
	public static int IMG_BLOCK_CORNER = 2;
	public static int IMG_BLOCK_CROSS = 3;
	public static int IMG_WALL_STUB = 4;
	public static int IMG_WALL_STRAIGHT = 5;
	public static int IMG_WALL_CORNER = 6;
	public static int IMG_WALL_TRIPOD = 7;
	public static int IMG_WALL_CROSS = 8;

	public static int mapStyleImg[][];
	
	public static final int WALLTYPE_PILLAR   = 0;
	public static final int WALLTYPE_STRAIGHT = 1;
	public static final int WALLTYPE_STUB     = 2;
	public static final int WALLTYPE_CORNER   = 3;
	public static final int WALLTYPE_TRIPOD   = 4;
	public static final int WALLTYPE_CROSS    = 5;

	//----------

	public static void loadMedia() {
		mapStyleImg = new int[numWallStyles][];

		mapStyleImg[STYLE_TOWN] = new int[numWallImgs];
		mapStyleImg[STYLE_TOWN][IMG_BLOCK]            = ImageData.IMG_MAP_WALL_BLOCK;
		mapStyleImg[STYLE_TOWN][IMG_BLOCK_SINGLESIDE] = ImageData.IMG_MAP_WALL_BLOCK_SINGLESIDE;
		mapStyleImg[STYLE_TOWN][IMG_BLOCK_CORNER]     = ImageData.IMG_MAP_WALL_BLOCK_CORNER;
		mapStyleImg[STYLE_TOWN][IMG_BLOCK_CROSS]      = ImageData.IMG_MAP_WALL_BLOCK_CROSS;
		mapStyleImg[STYLE_TOWN][IMG_WALL_STUB]        = ImageData.IMG_MAP_WALL_STUB;
		mapStyleImg[STYLE_TOWN][IMG_WALL_STRAIGHT]    = ImageData.IMG_MAP_WALL_STRAIGHT;
		mapStyleImg[STYLE_TOWN][IMG_WALL_CORNER]      = ImageData.IMG_MAP_WALL_CORNER;
		mapStyleImg[STYLE_TOWN][IMG_WALL_TRIPOD]      = ImageData.IMG_MAP_WALL_TRIPOD;
		mapStyleImg[STYLE_TOWN][IMG_WALL_CROSS]       = ImageData.IMG_MAP_WALL_CROSS;
	}
	
	public static AbstractWall createWall(Class<? extends AbstractWall> newWallClass) {
	  AbstractWall result=null;
	  
	  try {
      result = newWallClass.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
	  
	  return result;
	}
	
	public static AbstractWall createWall(Class<? extends AbstractWall> newWallClass, Vector newPos, boolean l, boolean r, boolean t, boolean b, boolean isBlock) {
	  AbstractWall result = createWall(newWallClass);
	  result.isBlock = isBlock;
	  
	  int tmpWallType = -1;
	  Vector tmpDir=null;
	  
	  //determine wall type, rotation
    if       (t==false && l==false && r==false && b==false) { //free
      tmpWallType = WALLTYPE_PILLAR;
      tmpDir = new Vector(0, 0);
    } else if(t==false && l==true && r==true && b==false) { // horizontal
      tmpWallType = WALLTYPE_STRAIGHT;
      tmpDir = new Vector(0, 1);
    } else if (t==true && l==false && r==false && b==true) { // vertical
      tmpWallType = WALLTYPE_STRAIGHT;
      tmpDir = new Vector(1, 0);
    } else if (t==true && l==false && r==false && b==false) { //stub down
      tmpWallType = WALLTYPE_STUB;
      tmpDir = new Vector(0, 1);
    } else if (t==false && l==true && r==false && b==false) { //stub right
      tmpWallType = WALLTYPE_STUB;
      tmpDir = new Vector(1, 0);
    } else if (t==false && l==false && r==true && b==false) { //stub left
      tmpWallType = WALLTYPE_STUB;
      tmpDir = new Vector(-1, 0);
    } else if (t==false && l==false && r==false && b==true) { //stub up
      tmpWallType = WALLTYPE_STUB;
      tmpDir = new Vector(0, -1);
    } else if (t==true && l==true && r==false && b==false) { // corner bottom right
      tmpWallType = WALLTYPE_CORNER;
      tmpDir = new Vector(0, 1);
    } else if (t==true && l==false && r==true && b==false) { // corner bottom left
      tmpWallType = WALLTYPE_CORNER;
      tmpDir = new Vector(-1, 0);
    } else if (t==false && l==true && r==false && b==true) { // corner top right
      tmpWallType = WALLTYPE_CORNER;
      tmpDir = new Vector(1, 0);
    } else if (t==false && l==false && r==true && b==true) { // corner top left
      tmpWallType = WALLTYPE_CORNER;
      tmpDir = new Vector(0, -1);
    } else if (t==true && l==true && r==true && b==false) { // horizontal +top
      tmpWallType = WALLTYPE_TRIPOD;
      tmpDir = new Vector(0, 1);
    } else if (t==false && l==true && r==true && b==true) { // horizontal +bot
      tmpWallType = WALLTYPE_TRIPOD;
      tmpDir = new Vector(0, -1);
    } else if (t==true && l==true && r==false && b==true) { // vertical +left
      tmpWallType = WALLTYPE_TRIPOD;
      tmpDir = new Vector(1, 0);
    } else if (t==true && l==false && r==true && b==true) { // vertical +right
      tmpWallType = WALLTYPE_TRIPOD;
      tmpDir = new Vector(-1, 0);
    } else if (t==true && l==true && r==true && b==true) { // enclosed
      tmpWallType = WALLTYPE_CROSS;
      tmpDir = new Vector(0, 1);
    }
	  
    result.setWallType(tmpWallType);
	  result.setPosition(newPos);
	  result.turnToDirection(tmpDir);
    return result;
	}
	
}
