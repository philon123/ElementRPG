package com.philon.rpg;

import com.philon.engine.input.Controller;
import com.philon.engine.input.User;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.mos.player.AbstractChar;
import com.philon.rpg.mos.player.AbstractChar.CharacterSaveData;

public class RpgUser extends User {
  public AbstractChar character;
  public CharacterSaveData charSaveData;

  public RpgMapObj selectedMO = null;

  public RpgUser(Controller newController) {
    super(newController);
  }
}
