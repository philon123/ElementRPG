package com.philon.marble;

import com.badlogic.gdx.controllers.Controllers;
import com.philon.engine.Data;
import com.philon.engine.PhilonGame;
import com.philon.engine.input.Controller;
import com.philon.engine.input.KeyboardMouseController;
import com.philon.engine.input.XBox360Controller;
import com.philon.marble.forms.MarbleMainScreen;

public class MarbleGame extends PhilonGame<MarbleMainScreen> {
  public static MarbleGame inst;

  @Override
  public void create() {
    inst = this;
    super.create();

    exclusiveUser = users.get(0);
  }

  @Override
  protected MarbleMainScreen getMainScreen() {
    return new MarbleMainScreen();
  }
  @Override
  protected Class<? extends Data> getConfiguredDataClass() {
    return MarbleData.class;
  }
  @Override
  protected MarbleUser createUser(Controller newController) {
    return new MarbleUser(newController);
  }

  public MarbleUser getActiveUser() {
    return (MarbleUser) activeUser;
  }
  public MarbleUser getExclusiveUser() {
    return (MarbleUser) exclusiveUser;
  }

  @Override
  protected void initUsers() {
    for(com.badlogic.gdx.controllers.Controller currController : Controllers.getControllers()) {
      if (currController.getName().toLowerCase().contains("xbox") &&
          currController.getName().contains("360")) {
        users.add( createUser( new XBox360Controller(currController)) );
        return;
      }
    }
    users.add( createUser(new KeyboardMouseController()) );
    exclusiveUser = users.get(0);
  }

}
