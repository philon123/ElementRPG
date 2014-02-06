package com.philon.rpg.mos.wall;

import com.philon.rpg.mos.chest.AbstractChest;
import com.philon.rpg.mos.door.AbstractDoor;

public abstract class AbstractMapStyle {
  public abstract Class<? extends AbstractWall> getWallClass();
  public abstract Class<? extends AbstractDoor> getDoorClass();
  public abstract Class<? extends AbstractChest> getChestClass();
}
