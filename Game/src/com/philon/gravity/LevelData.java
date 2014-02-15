package com.philon.gravity;

import java.util.ArrayList;

import com.philon.engine.util.AnimTextureRegion;
import com.philon.engine.util.Vector;

public class LevelData {
  public ArrayList<MassObjectData> massObjects;
  
  public LevelData() {
    massObjects = new ArrayList<MassObjectData>();
  }
  
  public void addObject(AnimTextureRegion newImage, Vector newPosition, float newMass, float newRadius, float newRadiance, Vector newInitSpeed) {
    MassObjectData newData = new MassObjectData();
    
    newData.image = newImage;
    newData.position = newPosition;
    newData.mass = newMass;
    newData.radius = newRadius;
    newData.radiance = newRadiance;
    newData.initSpeed = newInitSpeed;
    
    massObjects.add(newData);
  }
  
  public static class MassObjectData {
    public AnimTextureRegion image;
    public Vector position;
    public float mass;
    public float radius;
    public float radiance;
    public Vector initSpeed;
  }
}
