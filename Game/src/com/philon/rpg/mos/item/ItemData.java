package com.philon.rpg.mos.item;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.category.ConsumableItem;
import com.philon.rpg.mos.item.items.ItemAmulet1;
import com.philon.rpg.mos.item.items.ItemAmulet2;
import com.philon.rpg.mos.item.items.ItemAxe;
import com.philon.rpg.mos.item.items.ItemBastardSword;
import com.philon.rpg.mos.item.items.ItemBattleAxe;
import com.philon.rpg.mos.item.items.ItemBlade;
import com.philon.rpg.mos.item.items.ItemBreastPlate;
import com.philon.rpg.mos.item.items.ItemBroadAxe;
import com.philon.rpg.mos.item.items.ItemBroadSword;
import com.philon.rpg.mos.item.items.ItemBuckler;
import com.philon.rpg.mos.item.items.ItemCap;
import com.philon.rpg.mos.item.items.ItemCape;
import com.philon.rpg.mos.item.items.ItemChainMail;
import com.philon.rpg.mos.item.items.ItemClaymore;
import com.philon.rpg.mos.item.items.ItemCloak;
import com.philon.rpg.mos.item.items.ItemClub;
import com.philon.rpg.mos.item.items.ItemCompositeBow;
import com.philon.rpg.mos.item.items.ItemCompositeStaff;
import com.philon.rpg.mos.item.items.ItemCrown;
import com.philon.rpg.mos.item.items.ItemDagger;
import com.philon.rpg.mos.item.items.ItemFalchion;
import com.philon.rpg.mos.item.items.ItemFieldPlate;
import com.philon.rpg.mos.item.items.ItemFlail;
import com.philon.rpg.mos.item.items.ItemFullHelm;
import com.philon.rpg.mos.item.items.ItemFullPlateMail;
import com.philon.rpg.mos.item.items.ItemGothicPlate;
import com.philon.rpg.mos.item.items.ItemGothicShield;
import com.philon.rpg.mos.item.items.ItemGreatAxe;
import com.philon.rpg.mos.item.items.ItemGreatHelm;
import com.philon.rpg.mos.item.items.ItemGreatSword;
import com.philon.rpg.mos.item.items.ItemHardLeatherArmor;
import com.philon.rpg.mos.item.items.ItemHelm;
import com.philon.rpg.mos.item.items.ItemHuntersBow;
import com.philon.rpg.mos.item.items.ItemKiteShield;
import com.philon.rpg.mos.item.items.ItemLargeAxe;
import com.philon.rpg.mos.item.items.ItemLargeHealthPotion;
import com.philon.rpg.mos.item.items.ItemLargeManaPotion;
import com.philon.rpg.mos.item.items.ItemLargeShield;
import com.philon.rpg.mos.item.items.ItemLeatherArmor;
import com.philon.rpg.mos.item.items.ItemLongBattleBow;
import com.philon.rpg.mos.item.items.ItemLongBow;
import com.philon.rpg.mos.item.items.ItemLongStaff;
import com.philon.rpg.mos.item.items.ItemLongSword;
import com.philon.rpg.mos.item.items.ItemLongWarBow;
import com.philon.rpg.mos.item.items.ItemMace;
import com.philon.rpg.mos.item.items.ItemMaul;
import com.philon.rpg.mos.item.items.ItemMorningStar;
import com.philon.rpg.mos.item.items.ItemPlateMail;
import com.philon.rpg.mos.item.items.ItemQuarterStaff;
import com.philon.rpg.mos.item.items.ItemQuiltedArmor;
import com.philon.rpg.mos.item.items.ItemRags;
import com.philon.rpg.mos.item.items.ItemRing1;
import com.philon.rpg.mos.item.items.ItemRing2;
import com.philon.rpg.mos.item.items.ItemRingMail;
import com.philon.rpg.mos.item.items.ItemRobe;
import com.philon.rpg.mos.item.items.ItemSabre;
import com.philon.rpg.mos.item.items.ItemScaleMail;
import com.philon.rpg.mos.item.items.ItemScimitar;
import com.philon.rpg.mos.item.items.ItemScrollOfIdentify;
import com.philon.rpg.mos.item.items.ItemShortBattleBow;
import com.philon.rpg.mos.item.items.ItemShortBow;
import com.philon.rpg.mos.item.items.ItemShortStaff;
import com.philon.rpg.mos.item.items.ItemShortSword;
import com.philon.rpg.mos.item.items.ItemShortWarBow;
import com.philon.rpg.mos.item.items.ItemSkullCap;
import com.philon.rpg.mos.item.items.ItemSmallAxe;
import com.philon.rpg.mos.item.items.ItemSmallHealthPotion;
import com.philon.rpg.mos.item.items.ItemSmallManaPotion;
import com.philon.rpg.mos.item.items.ItemSmallShield;
import com.philon.rpg.mos.item.items.ItemSpikedClub;
import com.philon.rpg.mos.item.items.ItemSplintMail;
import com.philon.rpg.mos.item.items.ItemStuddedLeatherArmor;
import com.philon.rpg.mos.item.items.ItemTowerShield;
import com.philon.rpg.mos.item.items.ItemTwoHandedSword;
import com.philon.rpg.mos.item.items.ItemWarHammer;
import com.philon.rpg.stat.presuf.PrefixSuffixData;

public class ItemData {
	public static final int EFFTYPE_NORMAL = 0;
  public static final int EFFTYPE_RARE   = 1;
  public static final int EFFTYPE_UNIQUE = 2;

	public static LinkedHashMap<Class<? extends AbstractItem>, Integer> itemClassToRarityMap;

	public static AbstractItem createItem(Class<? extends AbstractItem> clazz) {
	  AbstractItem result=null;

	  try {
      result = clazz.newInstance();
      result.setPosition(new Vector());
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

	  return result;
	}

  public static void loadMedia() {
		itemClassToRarityMap = new LinkedHashMap<Class<? extends AbstractItem>, Integer>();
		registerItemClass( ItemAmulet1.class, 1 );
		registerItemClass( ItemAmulet2.class, 1 );
		registerItemClass( ItemAxe.class, 1 );
		registerItemClass( ItemBastardSword.class, 1 );
		registerItemClass( ItemBattleAxe.class, 1 );
		registerItemClass( ItemBlade.class, 1 );
		registerItemClass( ItemBreastPlate.class, 1 );
		registerItemClass( ItemBroadAxe.class, 1 );
		registerItemClass( ItemBroadSword.class, 1 );
		registerItemClass( ItemBuckler.class, 1 );
		registerItemClass( ItemCap.class, 1 );
		registerItemClass( ItemCape.class, 1 );
		registerItemClass( ItemChainMail.class, 1 );
		registerItemClass( ItemClaymore.class, 1 );
		registerItemClass( ItemCloak.class, 1 );
		registerItemClass( ItemClub.class, 1 );
		registerItemClass( ItemCompositeBow.class, 1 );
		registerItemClass( ItemCompositeStaff.class, 1 );
		registerItemClass( ItemCrown.class, 1 );
		registerItemClass( ItemDagger.class, 1 );
		registerItemClass( ItemFalchion.class, 1 );
		registerItemClass( ItemFieldPlate.class, 1 );
		registerItemClass( ItemFlail.class, 1 );
		registerItemClass( ItemFullHelm.class, 1 );
		registerItemClass( ItemFullPlateMail.class, 1 );
		registerItemClass( ItemGothicPlate.class, 1 );
		registerItemClass( ItemGothicShield.class, 1 );
		registerItemClass( ItemGreatAxe.class, 1 );
		registerItemClass( ItemGreatHelm.class, 1 );
		registerItemClass( ItemGreatSword.class, 1 );
		registerItemClass( ItemHardLeatherArmor.class, 1 );
		registerItemClass( ItemHelm.class, 1 );
		registerItemClass( ItemHuntersBow.class, 1 );
		registerItemClass( ItemKiteShield.class, 1 );
		registerItemClass( ItemLargeAxe.class, 1 );
		registerItemClass( ItemLargeHealthPotion.class, 1 );
		registerItemClass( ItemLargeManaPotion.class, 1 );
		registerItemClass( ItemLargeShield.class, 1 );
		registerItemClass( ItemLeatherArmor.class, 1 );
		registerItemClass( ItemLongBattleBow.class, 1 );
		registerItemClass( ItemLongBow.class, 1 );
		registerItemClass( ItemLongStaff.class, 1 );
		registerItemClass( ItemLongSword.class, 1 );
		registerItemClass( ItemLongWarBow.class, 1 );
		registerItemClass( ItemMace.class, 1 );
		registerItemClass( ItemMaul.class, 1 );
		registerItemClass( ItemMorningStar.class, 1 );
		registerItemClass( ItemPlateMail.class, 1 );
		registerItemClass( ItemQuarterStaff.class, 1 );
		registerItemClass( ItemQuiltedArmor.class, 1 );
		registerItemClass( ItemRags.class, 1 );
		registerItemClass( ItemRing1.class, 1 );
		registerItemClass( ItemRing2.class, 1 );
		registerItemClass( ItemRingMail.class, 1 );
		registerItemClass( ItemRobe.class, 1 );
		registerItemClass( ItemSabre.class, 1 );
		registerItemClass( ItemScaleMail.class, 1 );
		registerItemClass( ItemScimitar.class, 1 );
		registerItemClass( ItemScrollOfIdentify.class, 1 );
		registerItemClass( ItemShortBattleBow.class, 1 );
		registerItemClass( ItemShortBow.class, 1 );
		registerItemClass( ItemShortStaff.class, 1 );
		registerItemClass( ItemShortSword.class, 1 );
		registerItemClass( ItemShortWarBow.class, 1 );
		registerItemClass( ItemSkullCap.class, 1 );
		registerItemClass( ItemSmallAxe.class, 1 );
		registerItemClass( ItemSmallHealthPotion.class, 1 );
		registerItemClass( ItemSmallManaPotion.class, 1 );
		registerItemClass( ItemSmallShield.class, 1 );
		registerItemClass( ItemSpikedClub.class, 1 );
		registerItemClass( ItemSplintMail.class, 1 );
		registerItemClass( ItemStuddedLeatherArmor.class, 1 );
		registerItemClass( ItemTowerShield.class, 1 );
		registerItemClass( ItemTwoHandedSword.class, 1 );
		registerItemClass( ItemWarHammer.class, 1 );
  }

  public static void registerItemClass(Class<? extends AbstractItem> itemClass, int rarity) {
    itemClassToRarityMap.put(itemClass, rarity);
  }

  //----------

  public static AbstractItem createRandomItem( float newDropValue ) {
    AbstractItem it;
    float targetValue = (float) ((newDropValue*0.5f) + (Math.random()*newDropValue)); //0.5 - 1.5 dropvalues
    float maxBaseItemCost = (float) (targetValue / 2);

    //create base item
    ArrayList<Class<? extends AbstractItem>> possibleItems = new ArrayList<Class<? extends AbstractItem>>();
    for (Entry<Class<? extends AbstractItem>, Integer> currEntry : itemClassToRarityMap.entrySet()) {
      if (currEntry.getValue() <= maxBaseItemCost) possibleItems.add(currEntry.getKey());
    }
    Class<? extends AbstractItem> randomItemClass = possibleItems.get((int) Util.rnd(0, possibleItems.size()-1));
    if (Math.random()<0.3) randomItemClass=ItemScrollOfIdentify.class;

    it = createItem(randomItemClass);
    targetValue -= it.getDropValue();
    if (!(it instanceof ConsumableItem)) {
      //add prefix/suffix
      it.prefix = PrefixSuffixData.createRandomPrefix( (int) (targetValue*0.75f) );
      if( it.prefix != null ) {
        targetValue -= it.prefix.getDropValue();
      }

      it.suffix = PrefixSuffixData.createRandomSuffix( (int) (targetValue*0.75f) );
      if( it.suffix != null ) {
        targetValue -= it.suffix.getDropValue();
      }
    }

    if( it.prefix!=null || it.suffix!=null ) {
      it.iEffType = EFFTYPE_RARE;
      it.deidentify();
    } else {
      it.iEffType = EFFTYPE_NORMAL;
    }

    //finalize
    it.updateEffects();
    return it;
  }

}
