package com.philon.engine;
import com.philon.engine.util.Vector;

public class MapObject {
  public Vector pos;
  public Vector orientation = new Vector();
  public Vector speed;

  public FrameAnimation animation;

  public void setAnimation( FrameAnimation newAnimation ) {
    animation = newAnimation;
  }

  public void setPosition( Vector newPosition ) {
    pos = newPosition;
  }

  public void turnToDirection( Vector targetDir ) {
    orientation = targetDir.copy().normalizeInst();
  }

  public void turnToTarget( Vector targetPos ) {
    turnToDirection( Vector.sub(targetPos, pos) );
  }

  public void turn(float degrees) {
    turnToDirection(orientation.copy().rotateDegInst(degrees));
  }
}
