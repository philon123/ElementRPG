package com.philon.engine;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.philon.engine.event.ButtonEvent;
import com.philon.engine.event.InputEvent;
import com.philon.engine.forms.GuiElement;
import com.philon.engine.input.AbstractController.Button;
import com.philon.engine.input.Controller;
import com.philon.engine.input.Controller.MouseControllerElement;
import com.philon.engine.input.Controller.MouseCursor;
import com.philon.engine.input.KeyboardMouseController;
import com.philon.engine.input.User;
import com.philon.engine.input.XBox360Controller;
import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;

public abstract class PhilonGame<GUIROOT extends GuiElement> implements ApplicationListener {
  public static PhilonGame<?> inst;
  public Vector screenPixSize = new Vector(1240, 600);
  public float xyRatio;
  public float fps = 60;
  public int currFrame;

  public OrthographicCamera camera;
  public SpriteBatch batch;

  public GUIROOT guiHierarchy;
  public Graphics graphics;

  public ArrayList<User> users = new ArrayList<User>();
  public User activeUser = null;
  public User exclusiveUser = null;
  GuiElement[] exclusiveElements;

  @Override
  public void create() {
    inst = this;

    Gdx.graphics.setDisplayMode((int)screenPixSize.x, (int)screenPixSize.y, false);
    xyRatio = screenPixSize.x/screenPixSize.y;
    Texture.setEnforcePotImages(false);
    batch = new SpriteBatch();

    Util.instantiateClass(getConfiguredDataClass()).loadAll();

    graphics = new Graphics(this);

    initUsers();

    guiHierarchy = getMainScreen();
    guiHierarchy.setContainerTransform(new Vector(), new Vector(xyRatio, 1));
  }

  protected abstract Class<? extends Data> getConfiguredDataClass();

  @Override
  public void resize(int width, int height) {
    screenPixSize = new Vector(width, height);
    xyRatio = screenPixSize.x/screenPixSize.y;

    camera = new OrthographicCamera(screenPixSize.x/screenPixSize.y, 1);
    camera.setToOrtho(true, screenPixSize.x/screenPixSize.y, 1);
    batch.setProjectionMatrix(camera.combined);

    guiHierarchy.setContainerTransform(new Vector(), new Vector(xyRatio, 1));
  }


  @Override
  public void render() {
    currFrame += 1;
    if (Gdx.input.isKeyPressed(Keys.ESCAPE)) Gdx.app.exit();

    handleInput();

    batch.begin();
    clearScreen();
    execRender(batch);
    batch.end();
  }

  @Override
  public void pause() {
  }

  @Override
  public void resume() {
  }

  @Override
  public void dispose() {
    batch.dispose();
  }

  protected abstract GUIROOT getMainScreen();

  protected void clearScreen() {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
  }

  protected void execRender(SpriteBatch batch) {
    guiHierarchy.draw(batch);
//    batch.draw(ImageData.images[231].frames[0], 0, 0, guiHierarchy.xyRatio, 1);
//    graphics.drawText(batch, RpgUtil.font, "XXX", new Vector(0.5f, 0f));
  }


  protected void initUsers() {
    users.add( createUser(new KeyboardMouseController()) );

    for(com.badlogic.gdx.controllers.Controller currController : Controllers.getControllers()) {
      if (currController.getName().toLowerCase().contains("xbox") &&
          currController.getName().contains("360")) {
        users.add( createUser(new XBox360Controller(currController)) );
      }
    }
  }

  protected User createUser(Controller newController) {
    return new User(newController);
  }

  public void handleInput() {
    for(User currUser : users) {
      if(exclusiveUser!=null && exclusiveUser!=currUser) continue;
      activeUser = currUser;
      if(currUser.selectedEle==null) currUser.selectedEle = guiHierarchy;
      for(InputEvent<?> currEvt : currUser.controller.poll()) {

        if(currEvt.source instanceof MouseControllerElement) {
          if(currUser != exclusiveUser && !(currUser.controller instanceof KeyboardMouseController)) {
            continue; //only handle mouse events from controllers when user is exclusiveUser
          }

          //find/update hovered over element
          Vector cursorPos = currUser.controller.getElementByClass(MouseCursor.class).pos;
          GuiElement newHoveredOver = guiHierarchy.getElementByAbsPos(cursorPos);
          if(currUser.hoveredOverEle!=newHoveredOver) {
            if(currUser.hoveredOverEle!=null) currUser.hoveredOverEle.execCursorExit();
            currUser.hoveredOverEle = newHoveredOver;
            currUser.hoveredOverEle.execCursorEnter();
          }

          //if mousecursor event -> handle at hovered over element
          if(currEvt.source instanceof MouseCursor) {
            currUser.hoveredOverEle.handleEvent(currEvt);
            continue;
          }

          //if mousebutton click -> select hovered over element
          if(currEvt.source instanceof Button && currEvt.opCode==ButtonEvent.ACTION_DOWN) {
            if(currUser.selectedEle!=null) currUser.selectedEle.execDeselected();
            currUser.selectedEle = currUser.hoveredOverEle;
            currUser.selectedEle.execSelected();
//            currUser.selectedEle.handleEvent(currEvt);
//            continue;
          }
        }

        currUser.selectedEle.handleEvent(currEvt);
      }
    }
    activeUser = null;
  }

  public void startExclusiveSession(User forUser, GuiElement... newExclusiveElements) {
    exclusiveUser = forUser;
    exclusiveElements = newExclusiveElements;
    for(GuiElement currEle : exclusiveElements) {
      guiHierarchy.insertElement(currEle);
    }
    exclusiveUser.controller.getElementByClass(MouseCursor.class).pos = new Vector(xyRatio/2f, 0.5f);
  }

  public void endExclusiveSession() {
    for(GuiElement currEle : exclusiveElements) {
      guiHierarchy.removeElement(currEle);
    }
    exclusiveUser = null;
  }

}
