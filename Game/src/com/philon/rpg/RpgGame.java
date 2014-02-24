package com.philon.rpg;
import java.util.LinkedList;

import com.badlogic.gdx.Screen;
import com.philon.engine.PhilonGame;
import com.philon.engine.util.Vector;
import com.philon.rpg.map.RpgMap;
import com.philon.rpg.map.RpgMapSaveData;
import com.philon.rpg.map.generator.MapGenerator;
import com.philon.rpg.mo.UpdateMapObj;
import com.philon.rpg.mos.enemy.AbstractEnemy;
import com.philon.rpg.mos.player.AbstractChar;
import com.philon.rpg.mos.player.CharAmazon;
import com.philon.rpg.mos.player.CharData;
import com.philon.rpg.mos.player.PlayerSaveData;
import com.philon.rpg.mos.shot.AbstractShot;
import com.philon.rpg.mos.wall.CellarMapStylye;
import com.philon.rpg.util.RpgUtil;

public class RpgGame extends PhilonGame {
	public static RpgGame inst; //holds current instance

	public RpgDatabase gMedia;
	public RpgMap gMap;
	public RpgGraphics gGraphics;
	public RpgInput gInput;
	public RpgUtil gUtil;

	public RpgMapSaveData levelData[];
	public PlayerSaveData playerData;
	public int numLevels=10;
	public int currLevel;

	public AbstractChar localPlayer;
	public LinkedList<UpdateMapObj> dynamicMapObjs = new LinkedList<UpdateMapObj>();
	public LinkedList<AbstractEnemy> enemies = new LinkedList<AbstractEnemy>();

	@Override
	public void create() {
	  super.create();

	  RpgDatabase.loadAll();

		gGraphics = new RpgGraphics();
		gInput = new RpgInput();
		gUtil = new RpgUtil();
		levelData = new RpgMapSaveData[numLevels];

//	  SoundData.souMusic.play(); //TODO music
		inst = this;
		changeLevel(0);
	}

	@Override
	public Screen getMainScreen() {
	  return new RpgScreen();
	}

  @Override
  public void render() {
    super.render();

//    long t = System.currentTimeMillis();

    gInput.handleUserInput();
//    System.out.println("game: ui: " + (System.currentTimeMillis()-t)); t = System.currentTimeMillis();

    gGraphics.updateMinMaxTilesOnScreen();
    updateMapObjs();
    PhilonGame.gForms.updateForms();
//    System.out.println("game: mos: " + (System.currentTimeMillis()-t)); t = System.currentTimeMillis();

    gGraphics.drawAll();
//    System.out.println("game: drawmap: " + (System.currentTimeMillis()-t)); t = System.currentTimeMillis();
  }

  @Override
  public void resize(int width, int height) {
    super.resize(width, height);

    gGraphics.resizedTrigger(screenPixSize);
  }

	@Override
  public void dispose() {
    gGraphics.dispose();
  }

  public static void playSoundFX(int sound) {
    if (sound==0) return;
    SoundData.sounds[sound].play();
  }

	public void changeLevel( int newLevel ) {
		int prevLevel = currLevel;
		currLevel = newLevel;
		enemies = new LinkedList<AbstractEnemy>();

		//save and delete current level
		if( gMap!=null ) {
			levelData[currLevel] = gMap.save();
			playerData = localPlayer.save();
			gMap.deleteObject();
		}

		//load level or create new
		gMap = new RpgMap();
		if( levelData[newLevel]==null ) {
			gMap.init(new MapGenerator().generateMap(new Vector(100)), new CellarMapStylye());
		} else {
			gMap.init(levelData[newLevel]);
		}

		//determine spawn tile
		Vector newPos;
		if (currLevel-prevLevel<0) { //coming up
			newPos=gMap.spawnComingUpTile.copy();
		} else { //going down or spawning in lvl0
			newPos=gMap.spawnComingDownTile.copy();
		}

		//create or load player
		if( playerData==null ) {
			localPlayer = CharData.createChar(CharAmazon.class);
			localPlayer.setPosition(newPos);
		} else {
			localPlayer = CharData.loadChar(playerData);
			localPlayer.setPosition(newPos);
			localPlayer.pos = newPos.copy();
		}
		gGraphics.centeredMo=localPlayer;
	}

	public void updateMapObjs() {
	  for (int i=0; i<dynamicMapObjs.size(); i++) {
	    UpdateMapObj currMO = dynamicMapObjs.get(i);
			if( gGraphics.isTileOnScreen( currMO.pos.copy().roundAllInst() ) || currMO instanceof AbstractShot ) {
				currMO.update();
			}
	  }
	}

}
