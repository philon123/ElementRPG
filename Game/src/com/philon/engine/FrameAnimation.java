package com.philon.engine;

import com.philon.engine.util.AnimImage;

public class FrameAnimation {
  public AnimImage image;
  private float duration;
  private float startTime = -1;
  private boolean currAnimReversed;

  private int currAnimFrame;
  private int dir=0;

  public FrameAnimation(AnimImage newImage) {
    this(newImage, 0, false);
  }

  public FrameAnimation(AnimImage newImage, float newAnimLength, boolean animReversed) {
    setImage(newImage);
    if (newAnimLength>0) {
      startAnim(newAnimLength, animReversed);
    } else {
      int firstFrame = animReversed ? newImage.getFramesInAnimation()-1 : 0;
      currAnimFrame = firstFrame;
    }
  }

  private void setImage(AnimImage newAnimTexture) {
    image = newAnimTexture;
    currAnimFrame = 0;
    duration = 0;
    startTime = -1;

    updateAnimFrame();
  }

  public void setDir(int newDir) {
    if (newDir > image.getDirectionsInAnimation()-1) {
      newDir = 0;
    }
    dir = newDir;
  }

  public void startAnim(float newAnimLen, boolean reverseAnim) {
    duration = newAnimLen;
    startTime = PhilonGame.inst.currTime;
    currAnimReversed = reverseAnim;
  }

  public void startAnim(float newAnimLen) {
    startAnim(newAnimLen, false);
  }

  public void updateAnimFrame() {
    if (startTime<0) return; //no animation -> keep frame frozen
    float completion = (PhilonGame.inst.currTime - startTime) / duration;
    completion = (float) (completion - Math.floor(completion));
    if(currAnimReversed) completion = 1 - completion;
    currAnimFrame = (int)Math.floor(completion * image.getFramesInAnimation());
  }

  public int getCurrFrame() {
    updateAnimFrame();

    return dir*image.getFramesInAnimation() + currAnimFrame;
  }

}