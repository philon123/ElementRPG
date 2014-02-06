package com.philon.rpg.mos.enemy;

import com.philon.engine.util.Vector;
import com.philon.rpg.spell.SpellData;
import com.philon.rpg.stat.effect.EffectsObj;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddArmor;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddAttackRate;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddCastRate;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddMaxHealth;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddMaxMana;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddNormalDamage;
import com.philon.rpg.stat.effect.EffectsObj.EffectSetDefaultSpell;

public class EnemyZombie extends AbstractEnemy {
  
  @Override
  public String getName() {
    return "Zombie";
  }
  
  @Override
  public float getMaxPullDist() {
    return 7;
  }
  
  @Override
  public int getWorthXP() {
    return 7;
  }
  
  @Override
  public int getDropValue() {
    return 30;
  }
  
  @Override
  public EffectsObj getBaseEffects() {
    EffectsObj result = new EffectsObj();
    result.addOrCreateEffect( EffectAddMaxHealth.class, 30 );
    result.addOrCreateEffect( EffectAddMaxMana.class, 0 );
    result.addOrCreateEffect( EffectAddArmor.class, 0 );
    result.addOrCreateEffect( EffectAddNormalDamage.class, new Vector(3, 6) );
    result.addOrCreateEffect( EffectAddAttackRate.class,  2.5f );
    result.addOrCreateEffect( EffectAddCastRate.class,  2f );
    result.addOrCreateEffect( EffectSetDefaultSpell.class, SpellData.MELEE );
    return result;
  }

  @Override
  public float getTilesPerSecond() {
    return 0.5f;
  }

  @Override
  public int getImgIdle() {
    return 341;
  }
  
  @Override
  public int getImgMoving() {
    return 341;
  }
  
  @Override
  public int getImgCasting() {
    return 341;
  }
  
  @Override
  public int getImgHit() {
    return 341;
  }
  
  @Override
  public int getImgDying() {
    return 341;
  }
  
  @Override
  public int getSouAttack() {
    return 60;
  }
  
  @Override
  public int getSouHit() {
    return 1;
  }
  
  @Override
  public int getSouDie() {
    return 2;
  }
  
}