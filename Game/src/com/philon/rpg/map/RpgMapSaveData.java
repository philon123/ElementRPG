package com.philon.rpg.map;

import com.philon.rpg.mos.wall.AbstractMapStyle;


public class RpgMapSaveData {
	public AbstractMapStyle mapStyle;
	public int mapData[][];
//	Field TList toggledObjects

	//----------

	public RpgMapSaveData( RpgMap gm ) {
	mapStyle = gm.mapStyle;
	mapData = new int[(int) gm.gridSize.y][];
	
	//mapData
	for( int y = 0; y <= gm.gridSize.y-1; y++ ) {
		mapData[y] = new int[(int) gm.gridSize.x];
		for( int x = 0; x <= gm.gridSize.x-1; x++ ) {
			mapData[y][x] = gm.grid[y][x].tileType;
		}
	}
	
	//doors/chests
//	gmd.toggledObjects=new TList()
//	Local MapObj mo, ToggleMapObj currToggleGO
//	For mo=EachIn gm.toggleMOs
//		currToggleGO = ToggleMapObj(mo)
//		If currToggleGO!=Null Then
//			gmd.toggledObjects.AddLast(Pos.copy())
//		EndIf
//	Next
	}

	//----------

}
