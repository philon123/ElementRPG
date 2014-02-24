package com.philon.engine;

import com.philon.engine.util.AnimTextureRegion;

public class FrameAnimation {
  public AnimTextureRegion image;
  private int durationInFrames;
  private int animLengthInFrames;
  private int startFrame=-1;
  private boolean currAnimReversed;

  private int currAnimFrame;
  private int dir=0;

  public FrameAnimation(AnimTextureRegion newImage) {
    this(newImage, 0, false);
  }

  public FrameAnimation(AnimTextureRegion newImage, int newAnimLength, boolean animReversed) {
    setImage(newImage);
    if (newAnimLength>0) {
      startAnim(newAnimLength, animReversed);
    } else {
      int firstFrame = animReversed ? animLengthInFrames-1 : 0;
      currAnimFrame = firstFrame;
    }
  }

  private void setImage( AnimTextureRegion newAnimTexture ) {
    image = newAnimTexture;
    animLengthInFrames = (int)(newAnimTexture.dimensions.x);
    currAnimFrame = 0;
    durationInFrames = 0;
    startFrame = -1;

    updateAnimFrame();
  }

  public void setDir(int newDir) {
    if (newDir > image.dimensions.y-1) {
      newDir = 0;
    }
    dir = newDir;
  }

  public void startAnim( int newAnimLen, boolean reverseAnim ) {
    durationInFrames = newAnimLen;
    startFrame = PhilonGame.currFrame;
    currAnimReversed = reverseAnim;
  }

  public void startAnim( float newAnimLen ) {
    startAnim((int)newAnimLen, false);
  }

  public void updateAnimFrame() {
    if (startFrame<0) return; //no animation -> keep frame frozen

    currAnimFrame = (int) (( ((PhilonGame.currFrame-startFrame) / (durationInFrames*1.0f)) * animLengthInFrames ) % animLengthInFrames);
    if (currAnimReversed) currAnimFrame=(animLengthInFrames-1)-currAnimFrame;
  }

  public int getCurrFrame() {
    updateAnimFrame();

    return dir*animLengthInFrames + currAnimFrame;
  }

}