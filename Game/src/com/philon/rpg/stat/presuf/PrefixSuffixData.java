package com.philon.rpg.stat.presuf;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.philon.engine.util.Util;
import com.philon.rpg.stat.presuf.presufs.AddAllAttributesSuffix;
import com.philon.rpg.stat.presuf.presufs.AddDamageReduceSuffix;
import com.philon.rpg.stat.presuf.presufs.AddDamageReflectedSuffix;
import com.philon.rpg.stat.presuf.presufs.AddDamageSuffix;
import com.philon.rpg.stat.presuf.presufs.AddDexteritySuffix;
import com.philon.rpg.stat.presuf.presufs.AddFireDamageSuffix;
import com.philon.rpg.stat.presuf.presufs.AddHealthSuffix;
import com.philon.rpg.stat.presuf.presufs.AddIceDamageSuffix;
import com.philon.rpg.stat.presuf.presufs.AddLifeLeechSuffix;
import com.philon.rpg.stat.presuf.presufs.AddLightRadiusSuffix;
import com.philon.rpg.stat.presuf.presufs.AddLightningDamageSuffix;
import com.philon.rpg.stat.presuf.presufs.AddMagicFindPrefix;
import com.philon.rpg.stat.presuf.presufs.AddMagicSuffix;
import com.philon.rpg.stat.presuf.presufs.AddManaLeechSuffix;
import com.philon.rpg.stat.presuf.presufs.AddMaxManaPrefix;
import com.philon.rpg.stat.presuf.presufs.AddResistAllPrefix;
import com.philon.rpg.stat.presuf.presufs.AddResistFirePrefix;
import com.philon.rpg.stat.presuf.presufs.AddResistLightningPrefix;
import com.philon.rpg.stat.presuf.presufs.AddResistMagicPrefix;
import com.philon.rpg.stat.presuf.presufs.AddSpellLevelsPrefix;
import com.philon.rpg.stat.presuf.presufs.AddStrengthSuffix;
import com.philon.rpg.stat.presuf.presufs.AddVitalitySuffix;
import com.philon.rpg.stat.presuf.presufs.MulArmorPrefix;
import com.philon.rpg.stat.presuf.presufs.MulAttackRateSuffix;
import com.philon.rpg.stat.presuf.presufs.MulDamagePrefix;
import com.philon.rpg.stat.presuf.presufs.MulHitRecoverySuffix;
import com.philon.rpg.stat.presuf.presufs.SetIgnoreArmorSuffix;


public class PrefixSuffixData {
	public static ArrayList<Class<? extends AbstractPrefixSuffix>> prefixSuffixClasses;
	public static ArrayList<Class<? extends AbstractPrefix>> prefixClasses;
	public static ArrayList<Class<? extends AbstractSuffix>> suffixClasses;

  public static void loadMedia() {
    prefixSuffixClasses = new ArrayList<Class<? extends AbstractPrefixSuffix>>();
    prefixClasses = new ArrayList<Class<? extends AbstractPrefix>>();
    suffixClasses = new ArrayList<Class<? extends AbstractSuffix>>();

    registerPrefixSuffix(AddAllAttributesSuffix.class);
    registerPrefixSuffix(MulAttackRateSuffix.class);
    registerPrefixSuffix(AddDamageSuffix.class);
    registerPrefixSuffix(AddDexteritySuffix.class);
    registerPrefixSuffix(AddFireDamageSuffix.class);
    registerPrefixSuffix(AddHealthSuffix.class);
    registerPrefixSuffix(AddLifeLeechSuffix.class);
    registerPrefixSuffix(AddLightningDamageSuffix.class);
    registerPrefixSuffix(AddIceDamageSuffix.class);
    registerPrefixSuffix(AddMagicSuffix.class);
    registerPrefixSuffix(AddManaLeechSuffix.class);
    registerPrefixSuffix(AddMaxManaPrefix.class);
    registerPrefixSuffix(AddSpellLevelsPrefix.class);
    registerPrefixSuffix(AddStrengthSuffix.class);
    registerPrefixSuffix(AddVitalitySuffix.class);
    registerPrefixSuffix(MulArmorPrefix.class);
    registerPrefixSuffix(MulDamagePrefix.class);
    registerPrefixSuffix(AddDamageReduceSuffix.class);
    registerPrefixSuffix(AddDamageReflectedSuffix.class);
    registerPrefixSuffix(MulHitRecoverySuffix.class);
    registerPrefixSuffix(AddLightRadiusSuffix.class);
    registerPrefixSuffix(AddMagicFindPrefix.class);
    registerPrefixSuffix(AddResistAllPrefix.class);
    registerPrefixSuffix(AddResistFirePrefix.class);
    registerPrefixSuffix(AddResistLightningPrefix.class);
    registerPrefixSuffix(AddResistMagicPrefix.class);
    registerPrefixSuffix(SetIgnoreArmorSuffix.class);
  }

  @SuppressWarnings("unchecked")
  public static void registerPrefixSuffix(Class<? extends AbstractPrefixSuffix> clazz) {
    prefixSuffixClasses.add(clazz);
    if (AbstractPrefix.class.isAssignableFrom(clazz)) {
      prefixClasses.add((Class<? extends AbstractPrefix>) clazz);
    } else {
      suffixClasses.add((Class<? extends AbstractSuffix>) clazz);
    }
  }

  public static Class<? extends AbstractPrefixSuffix> getRandomPrefix() {
    return prefixClasses.get((int) Util.rnd(0, prefixClasses.size()-1));
  }

  public static Class<? extends AbstractPrefixSuffix> getRandomSuffix() {
    return suffixClasses.get((int) Util.rnd(0, suffixClasses.size()-1));
  }

  public static AbstractPrefixSuffix createPrefixSuffix(Class<? extends AbstractPrefixSuffix> clazz, int newDropValue) {
    AbstractPrefixSuffix result=null;
    try {
      result = clazz.
          getDeclaredConstructor(int.class).
          newInstance( newDropValue );
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    }
    return result;
  }

  public static AbstractPrefix createPrefix(Class<? extends AbstractPrefix> clazz, int newDropValue) {
    return (AbstractPrefix) createPrefixSuffix(clazz, newDropValue);
  }

  public static AbstractSuffix createSuffix(Class<? extends AbstractSuffix> clazz, int newDropValue) {
    return (AbstractSuffix) createPrefixSuffix(clazz, newDropValue);
  }

  public static AbstractPrefix createRandomPrefix(int newDropValue) {
    return (AbstractPrefix) createPrefixSuffix(getRandomPrefix(), newDropValue);
  }

  public static AbstractSuffix createRandomSuffix(int newDropValue) {
    return (AbstractSuffix) createPrefixSuffix(getRandomSuffix(), newDropValue);
  }

}
