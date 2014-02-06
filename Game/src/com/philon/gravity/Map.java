package com.philon.gravity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.philon.engine.Game;
import com.philon.engine.util.Vector;
import com.philon.gravity.LevelData.MassObjectData;

public class Map {
  ArrayList<MassObject> massObjects;
  
  public Map() {
    massObjects = new ArrayList<MassObject>();
  }
  
  public void build(LevelData newLevel) {
    for (MassObjectData tmpData : newLevel.massObjects) {
      MassObject newObject = new MassObject();
      
      newObject.setImage(tmpData.image, 0);
      newObject.pos = tmpData.position;
      newObject.mass = tmpData.mass;
      newObject.radius = tmpData.radius;
      newObject.radiance = tmpData.radiance;
      newObject.turnToDirection(tmpData.initDirection);
      newObject.speed = tmpData.initSpeed;
      
      massObjects.add(newObject);
    }
  }
  
  public void updateMassObjects() {
    HashMap<MassObject, LinkedList<Vector>> objectToForcesMap 
      = new HashMap<MassObject, LinkedList<Vector>>();
    
    //get all impulses
    for (MassObject currObject : massObjects) {
      objectToForcesMap.put(currObject, new LinkedList<Vector>());
      for (MassObject otherObject : massObjects) {
        if (!(otherObject.equals(currObject))) {
          
          Vector delta = Vector.sub(otherObject.pos, currObject.pos);
          float force = 6.67f * (currObject.mass*otherObject.mass)/(delta.x*delta.x + delta.y*delta.y); //F = G * (m1*m2) / (d*d)
          float acceleration = force/currObject.mass; //F=ma -> a=F/m
          Vector newImpulse = delta.copy().normalizeInst()
              .mulScalarInst(acceleration/Game.fps); //simulate 1 frame duration of acceleration
          
          objectToForcesMap.get(currObject).add(newImpulse);
        }
      }
    }
    
    //add up impulses and effect object
    for (MassObject currObject : massObjects) {
      Vector addedImpulses = Vector.mulScalar(currObject.direction, currObject.speed); //include current impulse
      for (Vector currImpuls : objectToForcesMap.get(currObject)) {
        addedImpulses.addInst(currImpuls);
      }
      currObject.turnToDirection(addedImpulses);
      currObject.speed = addedImpulses.getLength();
      currObject.pos = currObject.pos.addInst(addedImpulses);
      
    }
  }
}
