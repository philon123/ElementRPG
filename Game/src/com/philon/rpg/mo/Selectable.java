package com.philon.rpg.mo;

/**
 *  marks a MapObj to be considered for selection
 */
public interface Selectable {
  public void interactTrigger(UpdateMapObj objInteracting);
}
