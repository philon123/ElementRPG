package com.philon.gravity;

import java.util.ArrayList;

import com.philon.engine.util.AnimTextureRegion;
import com.philon.engine.util.Vector;
import com.philon.gravity.MassObject.MassObjectData;

public class LevelData {
  public ArrayList<MassObjectData> massObjects;

  public LevelData() {
    massObjects = new ArrayList<MassObjectData>();
  }

  public void addObject(AnimTextureRegion newImage, Vector newPosition, float newMass, float newRadius, float newRadiance, Vector newInitSpeed) {
    MassObjectData newData = new MassObjectData(newImage, newPosition, newMass, newRadius, newRadiance, newInitSpeed);

    massObjects.add(newData);
  }

}
