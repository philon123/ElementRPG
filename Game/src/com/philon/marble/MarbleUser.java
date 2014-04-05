package com.philon.marble;

import com.philon.engine.input.Controller;
import com.philon.engine.input.User;
import com.philon.marble.objects.Marble;

public class MarbleUser extends User {
  public Marble marble;

  public MarbleUser(Controller newController) {
    super(newController);
  }

}
