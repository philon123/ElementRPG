package com.philon.marble.objects;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import com.philon.engine.util.Vector;

public class BoxObject extends CollisionObject {
  Vector size;

  public BoxObject(World world, BoxObjectSaveData newData) {
    super(world, newData);

    size = newData.size.copy();
    Vector newHalfSize = Vector.mulScalar(size, 0.5f);
    PolygonShape newShape = new PolygonShape();
    newShape.setAsBox(newHalfSize.x, newHalfSize.y, new Vec2(), 0);
    body.createFixture(newShape, 1f);
    body.setLinearDamping(10);
  }

  @Override
  public Vector getSize() {
    return size;
  }

  public static class BoxObjectSaveData extends CollisionObjectSaveData {
    public Vector size;

    public BoxObjectSaveData(
        int newImage,
        boolean newIsDynamic,
        Vector newPosition,
        Vector newLinearVelocity,
        float newAngularVelocity,
        Vector newSize) {

      super(newImage, newIsDynamic, newPosition, newLinearVelocity, newAngularVelocity);

      size = newSize.copy();
    }

    public BoxObjectSaveData(BoxObject forObj) {
      super(forObj);

      size = forObj.size.copy();
    }

    @Override
    protected Class<BoxObject> getDefaultObjectClass() {
      return BoxObject.class;
    }


  }
}
