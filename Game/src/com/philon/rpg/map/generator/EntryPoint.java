package com.philon.rpg.map.generator;

import com.philon.engine.util.Vector;

public class EntryPoint {
	public Vector pos;
	public Vector normal;

	public EntryPoint( Vector newPos, Vector newNormal ) {
		pos = newPos;
		normal = newNormal;
	}

	public EntryPoint copy() {
		return new EntryPoint( pos.copy(), normal.copy() );
	}

}