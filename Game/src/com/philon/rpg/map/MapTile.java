package com.philon.rpg.map;
import java.util.LinkedList;

import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.map.mo.RpgMapObj;

public class MapTile {
	public Vector pos;
	public LinkedList<RpgMapObj> collList = new LinkedList<RpgMapObj>();
	public Vector basePixPos;

	public float staticBrightness;
	public float currBrightness;

	public MapTile( Vector newPos ) {
		pos = newPos;
		basePixPos = RpgGame.inst.gGraphics.getBasePixPosByTilePos( newPos );
	}

}
