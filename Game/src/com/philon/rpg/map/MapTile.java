package com.philon.rpg.map;
import java.util.LinkedList;

import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.mo.GameMapObj;

public class MapTile {
	public int tileType;

	public Vector pos;
	public LinkedList<GameMapObj> collList;
	public Vector basePixPos;

	public float staticBrightness;
	public float currBrightness;

	//----------
	public MapTile( Vector newPos, int newTileType ) {
		tileType = newTileType;
		pos = newPos;
		collList = new LinkedList<GameMapObj>();
		basePixPos = RpgGame.inst.gGraphics.getBasePixPosByTilePos( newPos );
	}

	//----------

}
