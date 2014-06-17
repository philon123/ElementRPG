package com.philon.rpg;
import com.philon.engine.Data;
import com.philon.engine.PhilonGame;
import com.philon.engine.forms.GuiElement;
import com.philon.engine.input.Controller;
import com.philon.engine.input.User;
import com.philon.engine.util.Vector;
import com.philon.rpg.forms.MapScreen;
import com.philon.rpg.mos.player.AbstractChar.ControllerAI;

public class RpgGame extends PhilonGame<MapScreen> {
	public static RpgGame inst; //holds current instance

	@Override
	public void create() {
	  inst = this;
	  super.create();
	}

	@Override
	protected Class<? extends Data> getConfiguredDataClass() {
	  return RpgData.class;
	}

	@Override
  protected RpgUser createUser(Controller newController) {
    return new RpgUser(newController);
  }

  public RpgUser getActiveUser() {
    return (RpgUser)activeUser;
  }

  public RpgUser getExclusiveUser() {
    return (RpgUser)exclusiveUser;
  }

	@Override
  protected MapScreen getMainScreen() {
    return new MapScreen();
  }

  public void playSoundFX(int sound) {
    if (sound==0) return;
    RpgData.sounds.get(sound).play();
  }

  @Override
  public void startExclusiveSession(User forUser, GuiElement... newExclusiveElements) {
    super.startExclusiveSession(forUser, newExclusiveElements);

    for(User currUser : users) {
      ((ControllerAI)((RpgUser)currUser).character.getAI()).setMovementDir(new Vector());
    }
  }

}
