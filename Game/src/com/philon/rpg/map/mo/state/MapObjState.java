package com.philon.rpg.map.mo.state;


public abstract class MapObjState<T extends StateParam> {

  public MapObjState(T param) {
  }

  public abstract void execOnChange();

  public abstract boolean execUpdate(float deltaTime);

  public boolean isStateChangeAllowed(Class<? extends MapObjState<?>> newStateClass) {
    return true;
  }

}
