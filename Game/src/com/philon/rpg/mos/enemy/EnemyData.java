package com.philon.rpg.mos.enemy;

import java.util.ArrayList;

import com.philon.engine.util.Util;

public class EnemyData {
	public static final int ID_FALSPEAR = 0;
	public static final int ID_SKELETON = 1;
	public static final int ID_ZOMBIE   = 2;
	public static final int ID_GOATBOW  = 3;

	public static ArrayList<Class<? extends AbstractEnemy>> enemyClasses;

	public static void loadMedia() {
	  enemyClasses = new ArrayList<Class<? extends AbstractEnemy>>();
	  
	  registerEnemyClass(EnemyFallenSpear.class);
	  registerEnemyClass(EnemyGoatBow.class);
	  registerEnemyClass(EnemySkeleton.class);
	  registerEnemyClass(EnemyZombie.class);
	}
	
	public static void registerEnemyClass(Class<? extends AbstractEnemy> enemyClass) {
	  enemyClasses.add(enemyClass);
	}
	
	public static AbstractEnemy createEnemy(Class<? extends AbstractEnemy> enemyClass) {
	  AbstractEnemy result = null;
	  
	  try {
      result = enemyClass.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
	  
	  return result;
	}

  public static Class<? extends AbstractEnemy> getRandomEnemyClass() {
    return enemyClasses.get((int) Util.random(0, enemyClasses.size()));
  }

}
