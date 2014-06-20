package com.philon.rpg.mos.enemy;

import com.philon.engine.util.Vector;
import com.philon.rpg.spell.SpellArrow;
import com.philon.rpg.stat.effect.EffectsObj;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddArmor;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddAttackRate;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddCastRate;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddMaxHealth;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddMaxMana;
import com.philon.rpg.stat.effect.EffectsObj.EffectAddNormalDamage;
import com.philon.rpg.stat.effect.EffectsObj.EffectSetDefaultSpell;

public class EnemyGoatBow extends AbstractEnemy {

  @Override
  public String getName() {
    return "Goat Bow";
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
    result.addOrCreateEffect( EffectAddMaxHealth.class, 20 );
    result.addOrCreateEffect( EffectAddMaxMana.class, 0 );
    result.addOrCreateEffect( EffectAddArmor.class, 0 );
    result.addOrCreateEffect( EffectAddNormalDamage.class, new Vector(2, 5) );
    result.addOrCreateEffect( EffectAddAttackRate.class,  1f );
    result.addOrCreateEffect( EffectAddCastRate.class,  1f );
    result.addOrCreateEffect( EffectSetDefaultSpell.class, SpellArrow.class );
    return result;
  }

  @Override
  public float getTilesPerSecond() {
    return 1;
  }

  @Override
  public int getImgIdle() {
    return 337;
  }

  @Override
  public int getImgMoving() {
    return 337;
  }

  @Override
  public int getImgCasting() {
    return 337;
  }

  @Override
  public int getImgHit() {
    return 337;
  }

  @Override
  public int getImgDying() {
    return 337;
  }

  @Override
  public int getSouAttack() {
    return 3;
  }

  @Override
  public int getSouHit() {
    return 4;
  }

  @Override
  public int getSouDie() {
    return 5;
  }

}
