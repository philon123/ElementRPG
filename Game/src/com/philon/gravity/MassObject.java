package com.philon.gravity;

import com.philon.engine.FrameAnimation;
import com.philon.engine.MapObject;
import com.philon.engine.util.AnimTextureRegion;
import com.philon.engine.util.Vector;

public class MassObject extends MapObject {
  public float mass = 0;
  public float radius = 0;
  public float radiance = 0;

  public MassObject() {
  }

  public MassObject(AnimTextureRegion newImage, Vector newPos, Vector newSpeed, float newMass, float newRadius, float newRadiance) {
    setAnimation(new FrameAnimation(newImage));
    pos = newPos;
    speed = newSpeed;
    mass = newMass;
    radius = newRadius;
    radiance = newRadiance;
  }
}
