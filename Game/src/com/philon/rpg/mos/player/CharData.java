package com.philon.rpg.mos.player;

import java.util.ArrayList;

import com.philon.rpg.mos.player.inventory.Inventory;
import com.philon.rpg.stat.effect.EffectsObj;

public class CharData {
  public static ArrayList<Class<? extends AbstractChar>> charClasses;
  
	public static final int maxCharLevel = 100;
	public static int[] xpTable;
	
  public static void loadMedia() {
    charClasses = new ArrayList<Class<? extends AbstractChar>>();
    registerCharClass(CharAmazon.class);
    registerCharClass(CharBarbarian.class);
    registerCharClass(CharSorcerer.class);
    
    xpTable = new int[maxCharLevel];
    for (int i=0; i<maxCharLevel; i++) {
      xpTable[i] = getXPForLevel(i);
//      System.out.println(xpTable[i]);
    }
  }
  
  public static void registerCharClass(Class<? extends AbstractChar> clazz) {
    charClasses.add(clazz);
  }
  
  public static AbstractChar createChar(Class<? extends AbstractChar> clazz) {
    AbstractChar result = null;
    
    try {
      result = clazz.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    
    return result;
  }
  
  public static AbstractChar loadChar( PlayerSaveData psd ) {
    AbstractChar result = createChar(psd.charClass);
    
    result.inv = new Inventory( psd.inv );
    result.xp = psd.xp;
    result.baseEffects = new EffectsObj( psd.baseEffects );

    for( int i = 0; i <= result.skills.length-1; i++ ) {
      result.setSkill( i, psd.skills[i] );
    }

    result.updateStats();
    
    return result;
  }
  
  public static int getXPForLevel(int level) {
    return (int)Math.pow(level, 2);
  }

  public static int getLevelForXP(int newXP) {
    for (int i=0; i<xpTable.length-1; i++) {
      if (newXP>=xpTable[i] && newXP<xpTable[i+1]) {
        return i+1;
      }
    }
    return 0;
  }
  
}
