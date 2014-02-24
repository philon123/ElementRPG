package com.philon.gravity;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.philon.engine.FrameAnimation;
import com.philon.engine.util.AnimTextureRegion;
import com.philon.engine.util.Vector;

public class MassObject implements Drawable {
  public float radiance = 0;

  public Body body;
  public FrameAnimation animation;

  public MassObject(World world, MassObjectData tmpData) {
    radiance = tmpData.radiance;
    setAnimation(new FrameAnimation(tmpData.image));

    BodyDef bd = new BodyDef();
    bd.type = BodyType.DYNAMIC;

    body = world.createBody(bd);

    MassData newMassData = new MassData();
    newMassData.mass = tmpData.mass;
    body.setMassData(new MassData());

    CircleShape newShape = new CircleShape();
    newShape.setRadius(tmpData.radius);
    body.createFixture(newShape, 1f);

    body.setTransform(new Vec2(tmpData.position.x, tmpData.position.y), 0);
    body.setLinearVelocity( new Vec2(tmpData.initSpeed.x, tmpData.initSpeed.y) );

//    body.m_userData = this; //backwards reference

    GravityGame.renderMap.add(0f, this);
  }

  public void setAnimation( FrameAnimation newAnimation ) {
    animation = newAnimation;
  }

  public void draw(SpriteBatch batch) {
    Vector imgSize = new Vector(getRadius()*2);
    if (radiance>0) { //draw glow first
      batch.setColor(new Color(0.9f, 0.15f, 0.15f, 1f));
      Vector glowSize = Vector.mulScalar(imgSize, 1+radiance).mulScalarInst(2);
      drawMapImageCentered(batch, GravityScreen.data.textures.get(Data.IMG_GLOW).frames[0], getPosition(), glowSize);
      batch.setColor(Color.WHITE);
    }
    //then draw object
    drawMapImageCentered(batch, animation.image.frames[animation.getCurrFrame()], getPosition(), imgSize);
  }

  public void drawMapImageCentered(SpriteBatch batch, TextureRegion newTex, Vector newMapPos, Vector newMapSize) {
    Vector tmpPos = GravityGame.graphics.getScreenPosByMapPos( newMapPos.copy().subInst(Vector.divScalar(newMapSize, 2f)) );
    Vector tmpSize = GravityGame.graphics.getScreenSizeByMapSize(newMapSize);
    batch.draw(newTex, tmpPos.x, tmpPos.y, tmpSize.x, tmpSize.y);
  }

  public Vector getPosition() {
    return new Vector(body.getTransform().p);
  }

  public Vector getVelocity() {
    return new Vector(body.getLinearVelocity());
  }

  public float getMass() {
    return body.getMass();
  }

  public float getRadius() {
    return body.getFixtureList().getShape().getRadius();
  }

  public static class MassObjectData {
    public AnimTextureRegion image;
    public Vector position;
    public float mass;
    public float radius;
    public float radiance;
    public Vector initSpeed;

    public MassObjectData(AnimTextureRegion newImage, Vector newPosition, float newMass, float newRadius, float newRadiance, Vector newInitSpeed) {
      image = newImage;
      position = newPosition;
      mass = newMass;
      radius = newRadius;
      radiance = newRadiance;
      initSpeed = newInitSpeed;
    }
  }
}
