package com.philon.rpg.map.generator;

import com.philon.engine.util.Vector;


public class EntryPoint {
	public Vector pos;
	public Vector normal;
	public int roomID;

	//----------
	
	public EntryPoint( Vector newPos, Vector newNormal, int newRoomID ) {
		pos = newPos;
		normal = newNormal;
		roomID = newRoomID;
	}

	//----------

	public EntryPoint copy() {
		return new EntryPoint( pos.copy(), normal.copy(), roomID );
	}

	//----------

}