package com.philon.rpg.map;
import java.util.LinkedList;

import com.philon.engine.util.Vector;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.util.RpgUtil;

public class MapTile {
	public Vector pos;
	public LinkedList<RpgMapObj> collList = new LinkedList<RpgMapObj>();
	public Vector baseScreenPos;

	public float staticBrightness;
	public float currBrightness;

	public MapTile( Vector newPos ) {
		pos = newPos;
		baseScreenPos = RpgUtil.getBaseScreenPosByTilePos( newPos );
	}

}
