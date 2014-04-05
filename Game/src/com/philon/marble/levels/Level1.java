package com.philon.marble.levels;

import java.util.ArrayList;

import com.philon.engine.util.Vector;
import com.philon.marble.MarbleData;
import com.philon.marble.objects.BoxObject.BoxObjectSaveData;
import com.philon.marble.objects.CollisionObject.CollisionObjectSaveData;

public class Level1 extends MarbleLevel {
  @Override
  public ArrayList<CollisionObjectSaveData> getObjects() {
    ArrayList<CollisionObjectSaveData> result = new ArrayList<CollisionObjectSaveData>();

    result.add(new BoxObjectSaveData(MarbleData.IMG_PLATTFORM_GRASS, false, new Vector(0.5f, 0.75f), new Vector(), 0, new Vector(1, 0.25f)));
    result.add(new BoxObjectSaveData(MarbleData.IMG_PLATTFORM_GRASS, false, new Vector(1.5f, 0.75f), new Vector(), 0, new Vector(1, 0.25f)));
    result.add(new BoxObjectSaveData(MarbleData.IMG_PLATTFORM_GRASS, false, new Vector(0, 0.5f), new Vector(), 0, new Vector(0.25f, 0.25f)));
    result.add(new BoxObjectSaveData(MarbleData.IMG_PLATTFORM_GRASS, false, new Vector(1.75f, 0.5f), new Vector(), 0, new Vector(0.25f, 0.25f)));

    return result;
  }

  @Override
  public Vector getSpawnPos() {
    return new Vector(0.1f);
  }
}
