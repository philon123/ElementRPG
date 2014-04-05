package com.philon.marble.objects;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.World;

import com.philon.engine.util.Vector;

public class RoundObject extends CollisionObject {

  public RoundObject(World world, RoundObjectSaveData newData) {
    super(world, newData);

    CircleShape newShape = new CircleShape();
    newShape.setRadius(newData.radius);
    body.createFixture(newShape, 1f);
  }

  @Override
  public Vector getSize() {
    return new Vector(body.getFixtureList().m_shape.m_radius * 2);
  }

  public static class RoundObjectSaveData extends CollisionObjectSaveData {
    public float radius;

    public RoundObjectSaveData(
        int newImage,
        boolean newIsDynamic,
        Vector newPosition,
        Vector newLinearVelocity,
        float newAngularVelocity,
        float newRadius) {

      super(newImage, newIsDynamic, newPosition, newLinearVelocity, newAngularVelocity);

      radius = newRadius;
    }

    public RoundObjectSaveData(RoundObject forObj) {
      super(forObj);

      radius = forObj.body.getFixtureList().m_shape.m_radius;
    }

    @Override
    protected Class<? extends CollisionObject> getDefaultObjectClass() {
      return RoundObject.class;
    }

  }
}
