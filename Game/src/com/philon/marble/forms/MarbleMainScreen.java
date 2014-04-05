package com.philon.marble.forms;

import com.badlogic.gdx.Gdx;
import com.philon.engine.event.ButtonInputListener;
import com.philon.engine.forms.GuiElement;
import com.philon.engine.input.Controller.StartButton;
import com.philon.engine.util.Vector;
import com.philon.marble.levels.Level1;

public class MarbleMainScreen extends GuiElement {

  public MarbleMainScreen() {
    addInputListener(StartButton.class, new ButtonInputListener() {
      @Override
      protected boolean execDown() {
        Gdx.app.exit();
        return true;
      }
    });
    insertElementInit(new MarbleMapScreen(new Level1()));
  }

  @Override
  protected boolean isStrechable() {
    return true;
  }
  @Override
  protected Vector getConfiguredSize() {
    return new Vector(1);
  }

}
