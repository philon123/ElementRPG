package com.philon.rpg.util;

import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;

public class RenderMapKey implements Comparable<RenderMapKey> {
	public Float value;
	public Integer uid;

	//----------
	
	public RenderMapKey( Vector newPos, int newUid ) {
		value = (RpgGame.inst.gMap.gridSize.y * newPos.y) + newPos.x;
		uid = newUid;
	}

	//----------
	
	@Override
	public boolean equals(Object obj) {
	  boolean tmpResult = value.equals(((RenderMapKey)obj).value);
    if (tmpResult) { //float values match
      return uid.equals(((RenderMapKey)obj).uid);
    } else {
      return tmpResult;
    }
	}

	//----------
	
  @Override
  public int compareTo(RenderMapKey arg0) {
    int tmpResult = value.compareTo(arg0.value);
    if (tmpResult==0) { //float values match
      return uid.compareTo(arg0.uid);
    } else {
      return tmpResult;
    }
  }

	//----------

}