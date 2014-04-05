package com.philon.marble.objects;

import org.jbox2d.dynamics.World;

import com.philon.engine.util.Vector;
import com.philon.marble.MarbleData;

public class Marble extends RoundObject {

  public Marble(World world, RoundObjectSaveData newData) {
    super(world, newData);
  }

  public static class MarbleSaveData extends RoundObjectSaveData {
    public float radius;

    public MarbleSaveData(
        Vector newPosition,
        Vector newLinearVelocity,
        float newAngularVelocity) {

      super(MarbleData.IMG_MARBLE, true, newPosition, newLinearVelocity, newAngularVelocity, 0.05f);
    }

    public MarbleSaveData(Marble forObj) {
      this(
          new Vector(forObj.body.getTransform().p),
          new Vector(forObj.body.getLinearVelocity()),
          forObj.body.getAngularVelocity()
      );
    }

    @Override
    protected Class<Marble> getDefaultObjectClass() {
      return Marble.class;
    }

    @Override
    public Marble load(World newWorld) {
      return (Marble) super.load(newWorld);
    }
  }

}
