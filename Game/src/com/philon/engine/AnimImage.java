package com.philon.engine;

import com.philon.engine.util.AnimTextureRegion;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;

public class AnimImage {
  public Vector pos;
  public Vector direction;
  public float speed;
  
  public AnimTextureRegion image;
  public int currFrame;
  public int imgAnimLen;
  public int imgAnimFrames;
  public int currAnimStart=-1;
  public boolean currAnimReversed;

  public void setPosition(Vector newPosition) {
    pos = newPosition;
  }
  
  public void setImage( AnimTextureRegion newAnimTexture, int newAnimFrame ) {
    image = newAnimTexture;
    imgAnimFrames = newAnimTexture.frames.length;
    currFrame = newAnimFrame;
    imgAnimLen = 0;
    currAnimStart = -1;
  }
  
  public void startAnim( int newAnimLen, boolean reverseAnim ) {
    imgAnimLen = newAnimLen;
    currAnimStart = RpgGame.currFrame;
    currFrame = 0;
    currAnimReversed = reverseAnim;
  }

  public void startAnim( float newAnimLen ) {
    startAnim((int)newAnimLen, false);
  }
  
  public void updateAnimFrame() {
    if (imgAnimLen==0) return;

    currFrame = ((int)( ((Game.currFrame-currAnimStart) / (imgAnimLen*1.0)) * imgAnimFrames )) % imgAnimLen;
    if (currAnimReversed) currFrame=(imgAnimLen-1)-currFrame;
  }
  
  public void turnToDirection( Vector targetDir ) {
    direction = targetDir.copy().normalizeInst();
  }
  
  public void turnToTarget( Vector targetPos ) {
    turnToDirection( Vector.sub(targetPos, pos) );
  }
}
