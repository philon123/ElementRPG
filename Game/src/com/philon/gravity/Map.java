package com.philon.gravity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import com.philon.engine.FrameAnimation;
import com.philon.engine.util.AnimTextureRegion;
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

      newObject.setAnimation(new FrameAnimation(tmpData.image));
      newObject.pos = tmpData.position;
      newObject.mass = tmpData.mass;
      newObject.radius = tmpData.radius;
      newObject.radiance = tmpData.radiance;
      newObject.speed = tmpData.initSpeed;

      massObjects.add(newObject);
    }
  }

  public void updateMassObjects() {
    HashMap<MassObject, LinkedList<Vector>> objectToForcesMap
      = new HashMap<MassObject, LinkedList<Vector>>();

    LinkedHashMap<MassObject, MassObject> collisionsMap
      = new LinkedHashMap<MassObject, MassObject>();

    float G = 6.67f; //gravitational constant
    float timeStep = 0.001f;//1/Game.fps;

    //get all forces, check for collisions
    for (MassObject currObject : massObjects) {
      objectToForcesMap.put(currObject, new LinkedList<Vector>());
      for (MassObject otherObject : massObjects) {
        if (!(otherObject.equals(currObject))) {
          Vector delta = Vector.sub(otherObject.pos, currObject.pos);
          if (delta.getLength() < currObject.radius+otherObject.radius) { //collision!
            collisionsMap.put(currObject, otherObject);
          }

          float force = G * (currObject.mass*otherObject.mass)/(delta.x*delta.x + delta.y*delta.y); //F = G * (m1*m2) / (d*d)
          Vector newForce = delta.normalizeInst().mulScalarInst(force);

          objectToForcesMap.get(currObject).add(newForce);
        }
      }
    }

    //add up forces and accelerate object
    for (MassObject currObject : massObjects) {
      Vector addedForces = new Vector();
      for (Vector currForce : objectToForcesMap.get(currObject)) {
        addedForces.addInst(currForce);
      }

      Vector acceleration = addedForces.copy().normalizeInst().mulScalarInst(addedForces.getLength() / currObject.mass); //F=ma -> a=F/m
      currObject.speed.addInst(acceleration.mulScalarInst(timeStep));
      currObject.pos.addInst(currObject.speed);
    }

    //handle collisions
    LinkedHashMap<MassObject, MassObject> objToNewObjMap
      = new LinkedHashMap<MassObject, MassObject>();
    for(Entry<MassObject, MassObject> currCollision : collisionsMap.entrySet()) {
      MassObject mo1 = currCollision.getKey();
      MassObject mo2 = currCollision.getValue();

      if (objToNewObjMap.containsKey(mo2)) {
        AnimTextureRegion newImage = mo1.mass>mo2.mass ? mo1.animation.image : mo2.animation.image;
        float newMass = mo1.mass + mo2.mass;
        float newRadius = (mo1.radius + mo2.radius) / 2;
        float newRadiance = (mo1.radiance + mo2.radiance) / 2;
        Vector newPos = mo1.pos.copy().addInst(mo2.pos).mulScalarInst(0.5f);
        Vector newSpeed = mo1.speed.copy().mulScalarInst(mo1.mass).
                  addInst(mo2.speed.copy().mulScalarInst(mo2.mass)).
                  divScalarInst(2 * newMass); // ((speed1*mass1) + (speed2*mass2)) / (2*newMass)
        MassObject newObject = new MassObject(newImage, newPos, newSpeed, newMass, newRadius, newRadiance);

        massObjects.remove(mo1);
        massObjects.remove(mo2);
        massObjects.add(newObject);
      } else {
        objToNewObjMap.put(mo1, null);
      }
    }
  }
}
