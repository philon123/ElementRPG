package com.philon.rpg.util;

import com.philon.engine.util.Vector;

public class RenderMapKey implements Comparable<RenderMapKey> {
	public Float value;
	public Integer hashCode;

	public RenderMapKey( Vector newPos, int newUid ) {
		value = 0.5f * 1.4142f * (newPos.y + newPos.x);
		hashCode = newUid;
	}

	@Override
	public boolean equals(Object obj) {
	  if( !(obj instanceof RenderMapKey) ) return false;
	  return value.equals(((RenderMapKey)obj).value) && hashCode.equals(((RenderMapKey)obj).hashCode);
	}

  @Override
  public int compareTo(RenderMapKey arg0) {
    int compareVal = value.compareTo(arg0.value);
    if (compareVal!=0) return compareVal;
    return hashCode.compareTo(arg0.hashCode);
  }

}