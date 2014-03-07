package com.philon.rpg.map;

import java.util.ArrayList;

import com.philon.engine.util.Vector;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.map.mo.RpgMapObj.RpgMapObjSaveData;
import com.philon.rpg.mos.player.AbstractChar;
import com.philon.rpg.mos.wall.AbstractMapStyle;


public class RpgMapSaveData {
  public Vector mapSize;
	public AbstractMapStyle mapStyle;
	public RpgMapObjSaveData[] mos;

  public RpgMapSaveData() {
  }

	public RpgMapSaveData( RpgMap gm ) {
  	mapSize = gm.gridSize.copy();
	  mapStyle = gm.mapStyle;

	  ArrayList<RpgMapObjSaveData> objDataList = new ArrayList<RpgMapObjSaveData>();
	  for(RpgMapObj currObj : gm.staticMapObjs) {
	    RpgMapObjSaveData tmpData = currObj.save();
	    if(tmpData!=null) objDataList.add(tmpData);
	  }
	  for(RpgMapObj currObj : gm.dynamicMapObjs) {
	    if(currObj instanceof AbstractChar) continue; //save player (or other network characters) seperately
	    RpgMapObjSaveData tmpData = currObj.save();
	    if(tmpData!=null) objDataList.add(tmpData);
	  }
	  mos = objDataList.toArray(new RpgMapObjSaveData[]{});
	}

}
