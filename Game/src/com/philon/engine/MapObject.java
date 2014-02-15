package com.philon.engine;
import com.philon.engine.util.Vector;

public class MapObject {
  public Vector pos;
  public Vector direction;
  public Vector speed;

  public FrameAnimation animation;

  public void setAnimation( FrameAnimation newAnimation ) {
    animation = newAnimation;
  }

  public void setPosition( Vector newPosition ) {
    pos = newPosition;
  }

  public void turnToDirection( Vector targetDir ) {
    direction = targetDir.copy().normalizeInst();
  }

  public void turnToTarget( Vector targetPos ) {
    turnToDirection( Vector.sub(targetPos, pos) );
  }
}
