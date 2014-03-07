package com.philon.rpg;
import com.badlogic.gdx.Screen;
import com.philon.engine.PhilonGame;
import com.philon.engine.util.Vector;
import com.philon.rpg.map.RpgMap;
import com.philon.rpg.map.RpgMapSaveData;
import com.philon.rpg.map.generator.MapGenerator;
import com.philon.rpg.mos.item.AbstractItem.ItemSaveData;
import com.philon.rpg.mos.item.items.ItemShortBow;
import com.philon.rpg.mos.player.AbstractChar;
import com.philon.rpg.mos.player.AbstractChar.CharacterSaveData;
import com.philon.rpg.mos.player.CharAmazon;
import com.philon.rpg.mos.player.inventory.Inventory.Equip;
import com.philon.rpg.mos.player.inventory.InventorySaveData;
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
	public CharacterSaveData playerData; //TODO player is already saved by map. what to do?
	public int numLevels=10;
	public int currLevel;

	public AbstractChar localPlayer;

	@Override
	public void create() {
	  super.create();

	  RpgDatabase.loadAll();
	  resetLevelSpecificData();

		gGraphics = new RpgGraphics();
		gInput = new RpgInput();
		gUtil = new RpgUtil();
		levelData = new RpgMapSaveData[numLevels];

//	  SoundData.souMusic.play();
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

    long t = System.currentTimeMillis();

    gInput.handleUserInput();
    System.out.println("game: ui: " + (System.currentTimeMillis()-t)); t = System.currentTimeMillis();

    gGraphics.updateMinMaxTilesOnScreen();
    gMap.updateMapObjs();
    PhilonGame.gForms.updateForms();
    System.out.println("game: mos: " + (System.currentTimeMillis()-t)); t = System.currentTimeMillis();

    gGraphics.drawAll();
    System.out.println("game: drawmap: " + (System.currentTimeMillis()-t)); t = System.currentTimeMillis();
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

  private void resetLevelSpecificData() {
    localPlayer = null;
		gMap = null;
  }

	public void changeLevel( int newLevel ) {
		int prevLevel = currLevel;
		currLevel = newLevel;

		//save and delete current level
		if( gMap!=null ) {
			levelData[currLevel] = gMap.save();
			playerData = localPlayer.save();
			resetLevelSpecificData();
		}

		//load level or create new
		if( levelData[newLevel]==null ) {
			gMap = new RpgMap( new MapGenerator().generateRpgMap(new Vector(100), new CellarMapStylye()) );
		} else {
			gMap = new RpgMap( levelData[newLevel] );
		}

		//determine spawn tile
		Vector newPos;
		if (prevLevel-currLevel<=0) { //level has increased -> coming down
		  newPos = gMap.spawnComingDown.copy();
		} else { //coming up
		  newPos = gMap.spawnComingUp.copy();
		}

		//create or load player
		if( playerData==null ) {
		  localPlayer = createAmazon(newPos).load();
		} else {
			localPlayer = playerData.load();
		}
		gMap.insertMapObj(localPlayer);
		gGraphics.centeredMo = localPlayer;
	}

	public CharacterSaveData createAmazon(Vector newPos) {
	  InventorySaveData newInventorySD = new InventorySaveData();
	  newInventorySD.equip[Equip.INV_WEAPON] = new ItemSaveData( ItemShortBow.class );

	  return new CharacterSaveData(CharAmazon.class, newPos, new Vector(1, 0), 0, newInventorySD);
	}

}
