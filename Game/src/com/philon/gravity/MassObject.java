package com.philon.gravity;

import com.philon.engine.AnimImage;

public class MassObject extends AnimImage {
  public float mass = 0;
  public float radius = 100;
  public float radiance = 0;
  
  public MassObject() {
  }
  
  public MassObject(float newMass, float newRadius, float newRadiance) {
    mass = newMass;
    radius = newRadius;
    radiance = newRadiance;
  }
}
