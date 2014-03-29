package com.philon.engine.user;

import com.philon.engine.util.Vector;
import com.philon.rpg.map.mo.UpdateMapObj.StateMovingStraight;
import com.philon.rpg.mos.player.AbstractChar;

public class ActionSetDirection extends UserAction {
  private Vector direction;

  public ActionSetDirection(Vector newPos) {
    direction = newPos.copy().normalizeInst();
  }

  @Override
  void execute(AbstractChar target) {
    if (direction.equals(new Vector())) {
      target.stopKeyMovement();
      if (target.isKeyMovement && target.currState instanceof StateMovingStraight) {
        target.changeState(AbstractChar.StateIdle.class);
      }
    } else {
      target.setKeyMovement(direction);
    }
  }

}
