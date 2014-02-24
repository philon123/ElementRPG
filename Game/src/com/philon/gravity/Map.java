package com.philon.gravity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import com.philon.engine.util.Vector;
import com.philon.gravity.MassObject.MassObjectData;

public class Map {
  ArrayList<MassObject> massObjects;
  World box2dWorld;

  public Map() {
    massObjects = new ArrayList<MassObject>();
    box2dWorld = new World(new Vec2()); //create worl with zero gravity
  }

  public void build(LevelData newLevel) {
    for (MassObjectData tmpData : newLevel.massObjects) {
      MassObject newObject = new MassObject(box2dWorld, tmpData);
      massObjects.add(newObject);
    }
  }

  public void updateMassObjects() {
    handleCollisions();
    updateGravityForces();

    box2dWorld.step(0.02f, 2, 2);
  }

  public void updateGravityForces() {
    HashMap<MassObject, LinkedList<Vector>> objectToForcesMap
      = new HashMap<MassObject, LinkedList<Vector>>();

    float G = 6.67f; //gravitational constant

    //get all forces
    for (MassObject currObject : massObjects) {
      objectToForcesMap.put(currObject, new LinkedList<Vector>());
      for (MassObject otherObject : massObjects) {
        if (!(otherObject.equals(currObject))) {
          Vector delta = Vector.sub( otherObject.getPosition(), currObject.getPosition() );
          float force = G * (currObject.getMass()*otherObject.getMass())/(delta.x*delta.x + delta.y*delta.y); //F = G * (m1*m2) / (d*d)
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
      currObject.body.applyForceToCenter(new Vec2(addedForces.x, addedForces.y));
    }
  }

  public void handleCollisions() { //TODO use ContactListener
//    if (box2dWorld.getContactList() == null) return;
//
//    for (Contact c=box2dWorld.getContactList(); c.getNext()!=null; c=c.getNext() ) {
//      MassObject mo1 = (MassObject)c.getFixtureA().getBody().getUserData();
//      MassObject mo2 = (MassObject)c.getFixtureB().getBody().getUserData();
//
//      AnimTextureRegion newImage = mo1.getMass()>mo2.getMass() ? mo1.animation.image : mo2.animation.image;
//      float newMass = mo1.getMass() + mo2.getMass();
//      float newRadius = ( mo1.getRadius() + mo2.getRadius() ) / 2;
//      float newRadiance = (mo1.radiance + mo2.radiance) / 2;
//      Vector newPos = mo1.getPosition().addInst(mo2.getPosition()).mulScalarInst(0.5f);
//      Vector newSpeed = mo1.getVelocity().mulScalarInst(mo1.getMass()).
//                addInst(mo2.getVelocity().mulScalarInst(mo2.getMass())).
//                divScalarInst(2 * newMass); // ((speed1*mass1) + (speed2*mass2)) / (2*newMass)
//      MassObjectData od = new MassObjectData(newImage, newPos, newMass, newRadius, newRadiance, newSpeed);
//      MassObject newObject = new MassObject(box2dWorld, od);
//
//      massObjects.remove(mo1);
//      massObjects.remove(mo2);
//      massObjects.add(newObject);
//    }
  }

}
