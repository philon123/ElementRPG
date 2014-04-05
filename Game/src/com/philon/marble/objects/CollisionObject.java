package com.philon.marble.objects;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import com.philon.engine.FrameAnimation;
import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;
import com.philon.marble.MarbleData;

/**
 * Needs a fixture
 */
public abstract class CollisionObject {
  public Body body;
  public FrameAnimation animation;

  public CollisionObject(World world, CollisionObjectSaveData newData) {
    animation = new FrameAnimation(MarbleData.textures.get(newData.image));

    BodyDef bd = new BodyDef();
    bd.type = newData.isDynamic ? BodyType.DYNAMIC : BodyType.STATIC;
    body = world.createBody(bd);

    body.setTransform(new Vec2(newData.position.x, newData.position.y), 0);
    body.setLinearVelocity( new Vec2(newData.initLinearVelocity.x, newData.initLinearVelocity.y) );
    body.setAngularVelocity(newData.initAngularVelocity);
  }

  public Vector getPos() {
    return new Vector(body.getTransform().p);
  }

  public abstract Vector getSize();

  public static abstract class CollisionObjectSaveData {
    public Class<? extends CollisionObject> objectClass;
    public int image;
    public boolean isDynamic;
    public Vector position;
    public Vector initLinearVelocity;
    public float initAngularVelocity;

    public CollisionObjectSaveData(
        int newImage,
        boolean newIsDynamic,
        Vector newPosition,
        Vector newLinearVelocity,
        float newAngularVelocity) {

      objectClass = getDefaultObjectClass();
      image = newImage;
      isDynamic = newIsDynamic;
      position = newPosition.copy();
      initLinearVelocity = newLinearVelocity.copy();
      initAngularVelocity = newAngularVelocity;
    }

    public CollisionObjectSaveData(CollisionObject forObj) {
      this(
          forObj.animation.image.imgIndex,
          forObj.body.getType()==BodyType.DYNAMIC,
          new Vector(forObj.body.getTransform().p),
          new Vector(forObj.body.getLinearVelocity()),
          forObj.body.getAngularVelocity()
      );
      objectClass = forObj.getClass();
    }

    protected abstract Class<? extends CollisionObject> getDefaultObjectClass();

    public CollisionObject load(World newWorld) {
      return Util.instantiateClass(objectClass, newWorld, this);
    }
  }

}
