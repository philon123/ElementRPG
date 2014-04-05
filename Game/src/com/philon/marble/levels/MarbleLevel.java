package com.philon.marble.levels;

import java.util.ArrayList;

import com.philon.engine.util.Vector;
import com.philon.marble.objects.CollisionObject.CollisionObjectSaveData;

public abstract class MarbleLevel {
  public abstract ArrayList<CollisionObjectSaveData> getObjects();

  public abstract Vector getSpawnPos();
}
