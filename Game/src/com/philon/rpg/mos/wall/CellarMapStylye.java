package com.philon.rpg.mos.wall;

import com.philon.rpg.mos.chest.AbstractChest;
import com.philon.rpg.mos.chest.Chest1;
import com.philon.rpg.mos.door.AbstractDoor;
import com.philon.rpg.mos.door.Door1;

public class CellarMapStylye extends AbstractMapStyle {

  @Override
  public Class<? extends AbstractWall> getWallClass() {
    return CellarWall.class;
  }

  @Override
  public Class<? extends AbstractDoor> getDoorClass() {
    return Door1.class;
  }

  @Override
  public Class<? extends AbstractChest> getChestClass() {
    return Chest1.class;
  }

}
