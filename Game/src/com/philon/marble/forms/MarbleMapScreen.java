package com.philon.marble.forms;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.philon.engine.event.JoystickInputListener;
import com.philon.engine.forms.GuiElement;
import com.philon.engine.input.AbstractController.Joystick;
import com.philon.engine.input.Controller.JoystickLeft;
import com.philon.engine.util.Vector;
import com.philon.marble.MarbleData;
import com.philon.marble.MarbleGame;
import com.philon.marble.levels.MarbleLevel;
import com.philon.marble.objects.CollisionObject;
import com.philon.marble.objects.CollisionObject.CollisionObjectSaveData;
import com.philon.marble.objects.Marble;
import com.philon.marble.objects.Marble.MarbleSaveData;

public class MarbleMapScreen extends GuiElement {
  ArrayList<CollisionObject> objects;
  World world;
  Vector centeredPos;

  public MarbleMapScreen(MarbleLevel newLevel) {
    addInputListener(JoystickLeft.class, new JoystickInputListener() {
      @Override
      protected boolean execChanged(Joystick source) {
        Marble marble = MarbleGame.inst.getActiveUser().marble;
        marble.body.applyTorque(source.pos.x / 10f);
        return true;
      }
    });

    world = new World(new Vec2(0, 10));
    objects = new ArrayList<CollisionObject>();

    Marble userMarble = new MarbleSaveData(newLevel.getSpawnPos(), new Vector(), 0).load(world);
    userMarble.body.setAngularDamping(10);
    MarbleGame.inst.getExclusiveUser().marble = userMarble;
    objects.add(userMarble);

    for (CollisionObjectSaveData tmpData : newLevel.getObjects()) {
      objects.add(tmpData.load(world));
    }
  }

  @Override
  protected boolean isStrechable() {
    return true;
  }
  @Override
  protected Vector getConfiguredSize() {
    return new Vector(1);
  }
  @Override
  protected int getConfiguredBackground() {
    return MarbleData.IMG_BLUE;
  }
  @Override
  protected void execDraw(SpriteBatch batch) {
    super.execDraw(batch);

    world.step(0.02f, 2, 2);
    handleCollisions();

    for(CollisionObject currObj : objects) {
      Vector newPos = Vector.sub(currObj.getPos(), Vector.mulScalar(currObj.getSize(), 0.5f));
      drawNormalized(batch, currObj.animation.image.frames[0], newPos, currObj.getSize());
    }
  }

  public void handleCollisions() { //TODO use ContactListener
  }

}
