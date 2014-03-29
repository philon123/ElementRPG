package com.philon.engine.user;

import com.philon.rpg.mos.player.AbstractChar;

public abstract class UserAction {
  public static final int MOVEMENT_DIR = 1;
  public static final int CASTING_DIR  = 2;
  public static final int CLICK_1      = 3;
  public static final int CLICK_2      = 4;
  public static final int CURSOR_OVER  = 5;

  abstract void execute(AbstractChar target);
}
