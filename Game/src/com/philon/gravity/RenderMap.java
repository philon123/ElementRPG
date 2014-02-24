package com.philon.gravity;

import java.util.LinkedList;
import java.util.TreeMap;

public class RenderMap {
  TreeMap<Float, LinkedList<Drawable>> renderMap; //z-buffer, larger key -> later draw

  public RenderMap() {
    renderMap = new TreeMap<Float, LinkedList<Drawable>>();
  }

  public void add(float key, Drawable obj) {
    LinkedList<Drawable> currList = renderMap.get(key);
    if (currList==null) { //no list exists -> create new list, add obj
      renderMap.put(key, new LinkedList<Drawable>());
      renderMap.get(key).add(obj);
    } else { //list exists -> add obj, if it's not already there
      if (!currList.contains(obj)) {
        currList.add(obj);
      }
    }
  }

  public void remove(float key, Drawable obj) {
    LinkedList<Drawable> currList = renderMap.get(key);
    if (currList!=null) {
      currList.remove(obj);
      if (currList.isEmpty()) { //delete empty lists
        renderMap.remove(key);
      }
    }
  }

}
