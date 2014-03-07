package com.philon.rpg.mos.wall;

import com.philon.rpg.map.generator.RoomData;
import com.philon.rpg.mos.breakables.BreakableBarrel;
import com.philon.rpg.mos.breakables.BreakableVase;
import com.philon.rpg.mos.chest.AbstractChest;
import com.philon.rpg.mos.door.AbstractDoor;
import com.philon.rpg.mos.enemy.AbstractEnemy;
import com.philon.rpg.mos.light.Firestand;
import com.philon.rpg.mos.light.Torch;
import com.philon.rpg.mos.stairs.StairsDown;
import com.philon.rpg.mos.stairs.StairsUp;

public abstract class AbstractMapStyle {
  public abstract RoomData getRoomData();

  public abstract Class<? extends AbstractWall> getWallClass();
  public abstract Class<? extends AbstractDoor> getDoorClass();
  public abstract Class<? extends AbstractChest> getChestClass();
  public abstract Class<? extends StairsUp> getStairsUpClass();
  public abstract Class<? extends StairsDown> getStairsDownClass();
  public abstract Class<? extends Torch> getTorchClass();
  public abstract Class<? extends Firestand> getFirestandClass();
  public abstract Class<? extends BreakableBarrel> getBarrelClass();
  public abstract Class<? extends BreakableVase> getVaseClass();
  public abstract Class<? extends AbstractEnemy> getRandomEnemyClass();
}
