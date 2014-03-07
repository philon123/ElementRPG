package com.philon.rpg.mos.wall;

import com.philon.rpg.map.generator.RoomData;
import com.philon.rpg.mos.breakables.BreakableBarrel;
import com.philon.rpg.mos.breakables.BreakableVase;
import com.philon.rpg.mos.chest.AbstractChest;
import com.philon.rpg.mos.chest.Chest1;
import com.philon.rpg.mos.door.AbstractDoor;
import com.philon.rpg.mos.door.Door1;
import com.philon.rpg.mos.enemy.AbstractEnemy;
import com.philon.rpg.mos.enemy.EnemyFallenSpear;
import com.philon.rpg.mos.enemy.EnemyGoatBow;
import com.philon.rpg.mos.enemy.EnemySkeleton;
import com.philon.rpg.mos.enemy.EnemyZombie;
import com.philon.rpg.mos.light.Firestand;
import com.philon.rpg.mos.light.Torch;
import com.philon.rpg.mos.stairs.StairsDown;
import com.philon.rpg.mos.stairs.StairsUp;

public class CellarMapStylye extends AbstractMapStyle {

  @Override
  public RoomData getRoomData() {
    return new RoomData();
  }

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

  @Override
  public Class<? extends StairsUp> getStairsUpClass() {
    return StairsUp.class;
  }

  @Override
  public Class<? extends StairsDown> getStairsDownClass() {
    return StairsDown.class;
  }

  @Override
  public Class<? extends Torch> getTorchClass() {
    return Torch.class;
  }

  @Override
  public Class<? extends Firestand> getFirestandClass() {
    return Firestand.class;
  }

  @Override
  public Class<? extends BreakableBarrel> getBarrelClass() {
    return BreakableBarrel.class;
  }

  @Override
  public Class<? extends BreakableVase> getVaseClass() {
    return BreakableVase.class;
  }

  @Override
  public Class<? extends AbstractEnemy> getRandomEnemyClass() {
    double rnd = Math.random();
    if(rnd<0.3) {
      return EnemyFallenSpear.class;
    } else if(rnd<0.6) {
      return EnemySkeleton.class;
    } else if(rnd<0.9) {
      return EnemyZombie.class;
    } else {
      return EnemyGoatBow.class; //less goatbows...
    }
  }

}
