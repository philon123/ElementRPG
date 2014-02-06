package com.philon.rpg.mo;

public abstract class StaticMapObj extends AbstractMapObj {
  
  public StaticMapObj() {
    super();
    
    setImage(getImage());
  }
  
  public abstract int getImage();
  
}
