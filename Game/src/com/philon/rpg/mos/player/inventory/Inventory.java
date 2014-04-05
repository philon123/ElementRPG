package com.philon.rpg.mos.player.inventory;

import java.util.LinkedList;
import java.util.List;

import com.philon.engine.util.Vector;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.item.AbstractItem.ItemSaveData;
import com.philon.rpg.mos.item.category.AmuletItem;
import com.philon.rpg.mos.item.category.ArmorItem;
import com.philon.rpg.mos.item.category.ConsumableItem;
import com.philon.rpg.mos.item.category.HelmItem;
import com.philon.rpg.mos.item.category.RingItem;
import com.philon.rpg.mos.item.category.ShieldItem;
import com.philon.rpg.mos.item.category.WeaponItem;
import com.philon.rpg.mos.player.AbstractChar;
import com.philon.rpg.stat.effect.EffectsObj;
import com.philon.rpg.util.RpgUtil;

public class Inventory {
	public AbstractChar ownerPlayer;
	public EffectsObj effects;

	public Equip equip;
	public InventoryGrid invGrid;
	public BeltGrid beltGrid;

	public int currGold=10000;

	public AbstractItem pickedUpItem;

	//----------

	public Inventory( AbstractChar newOwnerPlayer ) {
		init(newOwnerPlayer);
		currGold = 100;
	}

	//----------

	public Inventory( AbstractChar newOwnerPlayer, InventorySaveData isd ) {
	  init(newOwnerPlayer);
	  currGold = isd.currGold;

		for( ItemSaveData currItemSD : isd.invList ) {
			invGrid.add( (AbstractItem)currItemSD.load(), currItemSD.pos, false );
		}

		for( int i = 0; i < isd.equip.length; i++ ) {
			if( isd.equip[i]!=null ) {
				addItemToEquip((AbstractItem)isd.equip[i].load(), i, false);
			}
		}

		for( int i = 0; i < isd.beltGrid.length; i++ ) {
			if( isd.beltGrid[i]!=null ) {
				beltGrid.add((AbstractItem)isd.beltGrid[i].load(), new Vector(i, 0), false);
			}
		}

		equipChangedTrigger();
		updateEffects();
	}

	//----------

	public void init( AbstractChar newOwnerPlayer ) {
	  ownerPlayer = newOwnerPlayer;

		equip = new Equip();

		invGrid = new InventoryGrid();
		beltGrid = new BeltGrid();

		currGold = 0;

		updateEffects();
	}

	//----------

	public InventorySaveData save() {
		return new InventorySaveData(this);
	}

	//----------

	private boolean isItemForBelt(AbstractItem newItem) {
	  return pickedUpItem instanceof ConsumableItem &&
        pickedUpItem.getInvSize().isAllEqual(new Vector(1));
	}

	public void equipChangedTrigger() {
		updateEffects();

		if (ownerPlayer!=null) ownerPlayer.updateStats();
	}

	//----------

	public void updateEffects() {
	  EffectsObj result = new EffectsObj();
		for (AbstractItem tmpItem : equip.getItems()) {
		  if (tmpItem.reqMetFlag) {
		    result.addToSelf( tmpItem.effects );
		  }
		}

		effects = result;
	}

	//----------

	public void updateReqMetFlags() {
		for (AbstractItem currItem : equip.getItems()) {
		  updateReqMetFlag(currItem);
		}
		for( AbstractItem currItem : invGrid.itemList ) {
		  updateReqMetFlag(currItem);
		}
		for( AbstractItem currItem : beltGrid.itemList ) {
		  updateReqMetFlag(currItem);
    }
	}

	public void updateReqMetFlag(AbstractItem it) {
	  it.reqMetFlag = ownerPlayer.stats.isReqMet( it.requirements );
	}

	//----------

	public void pickupItem( AbstractItem it ) {
	  if(pickedUpItem!=null) System.err.println("pickupAuto() detected existing pickedUpItem");

//	  if( it.id == ItemData.GOLD ) { //TODO gold
//      pickupGold( (int) it.dropValue );
//      return;
//    }

    if( contains(it) ) {
      removeItem( it );
    }

    pickedUpItem = it;
    it.changeState( AbstractItem.StatePickedUp.class );
    updateReqMetFlag(it);
  }

  public boolean pickupAuto( AbstractItem it ) {
    if(pickedUpItem!=null) System.err.println("pickupAuto() detected existing pickedUpItem");

//    if( it.id == ItemData.GOLD ) { //TODO gold
//      pickupGold( (int) it.dropValue );
//      return true;
//    }

    pickupItem( it );
    boolean result;
    if(isItemForBelt(pickedUpItem)) {
      if (beltGrid.addAuto(pickedUpItem) ) {
        pickedUpItem = null;
        result = true;
      } else {
        result = false;
      }
    } else {
      if( invGrid.addAuto(pickedUpItem) ) {
        pickedUpItem = null;
        result = true;
      } else {
        result = false;
      }
    }

    if( result ) {
      return true;
    } else {
      dropPickup();
      return false;
    }
  }

  public void dropPickup() {
    Vector newTile = RpgUtil.getNextFreeTile( ownerPlayer.pos, false, false, true, true );
    if( newTile != null ) {
      pickedUpItem.setPosition(newTile);
      pickedUpItem.changeState( AbstractItem.StateMap.class );
      pickedUpItem = null;
    }
  }

  public boolean addPickupToEquip( int targetSlot ) {
    AbstractItem tmpPickup = pickedUpItem;
    pickedUpItem = null; //clear room for possible displaced item
    AbstractItem displacedItem = addItemToEquip(tmpPickup, targetSlot, true);
    if (displacedItem==null) { //insert failed
      pickedUpItem = tmpPickup; //restore previous state
      return false;
    } else if(displacedItem!=tmpPickup)  { //success, with displacement
      pickupItem(displacedItem);
    }
    return true;
  }

  /**
   * @returns
   *  - if sucessfull without displacement -> the item passed to it
   *  - if sucessfull with displacement -> the displaced item
   *  - if fail -> null
   *
   */
	public AbstractItem addItemToEquip( AbstractItem it, int targetSlot, boolean allowDisplacement ) {
	  updateReqMetFlag(it);
		if (!it.reqMetFlag) return null;
		if( !equip.equipType[targetSlot].isInstance(it) ) return null;

		AbstractItem displacedItem = equip.getBySlot(targetSlot);
		if(!allowDisplacement && displacedItem!=null) return null;

		WeaponItem equippedWeapon = (WeaponItem)equip.getBySlot(Equip.INV_WEAPON);
		ShieldItem equippedShield = (ShieldItem)equip.getBySlot(Equip.INV_SHIELD);

		AbstractItem secondDisplacedItem = null;
		if( targetSlot==Equip.INV_WEAPON && ((WeaponItem)it).isTwoHanded() && equippedShield!=null ) {
		  secondDisplacedItem = equippedShield;
		} else if( targetSlot==Equip.INV_SHIELD && equippedWeapon!=null && equippedWeapon.isTwoHanded() ) {
		  secondDisplacedItem = equippedWeapon;
		}
		if(secondDisplacedItem!=null) {
		  if ( !allowDisplacement ) return null;
		  if(displacedItem==null) {
		    displacedItem = secondDisplacedItem;
		  } else {
		    if ( !pickupAuto(secondDisplacedItem) ) return null;
		  }
		}

		equip.items[targetSlot] = it;
		it.pos = new Vector( targetSlot, 0 );
		it.changeState( AbstractItem.StateInv.class );

		equipChangedTrigger();
		return displacedItem!=null ? displacedItem : it;
	}

	public boolean contains( AbstractItem it ) {
    if (equip.contains(it) || invGrid.contains(it) || beltGrid.contains(it)) {
      return true;
    } else
      return false;
  }

	public void removeItem( AbstractItem it ) {
	  if (equip.contains(it)) {
	    equip.items[(int) it.pos.x]=null;
	    equipChangedTrigger();
	  } else if (invGrid.contains(it)) {
	    invGrid.remove(it);
	  } else if (beltGrid.contains(it)) {
	    beltGrid.remove(it);
	  }
	}

	//----------

	public void pickupGold( int newAmount ) {
		if (newAmount>=0) currGold += newAmount;
		pickedUpItem=null;
	}

	//----------

	public int removeGold( int newAmount ) {
	  int result = (currGold>=newAmount) ? newAmount : currGold;
	  currGold -= result;
	  return result;
	}

	//----------

	public void identifyItem( AbstractItem newItem ) {
		newItem.identify();
		if (equip.contains(newItem)) {
		  equipChangedTrigger();
		}
	}

	//----------

	public class InventoryGrid extends AbstractItemGrid {

	  @Override
	  public Vector getGridSize() {
	    return new Vector(10, 4);
	  }

	}

	public class BeltGrid extends AbstractItemGrid {
	  public static final int length = 10;

    @Override
    public Vector getGridSize() {
      return new Vector(length, 1);
    }

  }

	public class Equip {
	  public static final int numSlots = 7;

	  public static final int INV_WEAPON  = 0;
	  public static final int INV_SHIELD  = 1;
	  public static final int INV_ARMOR   = 2;
	  public static final int INV_HELM    = 3;
	  public static final int INV_AMULETT = 4;
	  public static final int INV_RING1   = 5;
	  public static final int INV_RING2   = 6;

	  AbstractItem[] items;
	  public Class<? extends AbstractItem>[] equipType;

    @SuppressWarnings("unchecked")
    public Equip() {
	    items = new AbstractItem[numSlots];

	    equipType = new Class[numSlots];
	    equipType[INV_WEAPON]  = WeaponItem.class;
	    equipType[INV_SHIELD]  = ShieldItem.class;
	    equipType[INV_ARMOR]   = ArmorItem.class;
	    equipType[INV_HELM]    = HelmItem.class;
	    equipType[INV_AMULETT] = AmuletItem.class;
	    equipType[INV_RING1]   = RingItem.class;
	    equipType[INV_RING2]   = RingItem.class;
	  }

	  public boolean contains(AbstractItem newItem) {
	    for (AbstractItem currItem : items) {
	      if (currItem==newItem) return true;
	    }
	    return false;
	  }

	  public AbstractItem getBySlot(int newSlot) {
	    return items[newSlot];
	  }

	  public List<AbstractItem> getItems() {
	    LinkedList<AbstractItem> result = new LinkedList<AbstractItem>();
	    for (AbstractItem currItem : items) {
        if (currItem!=null) result.addLast(currItem);
      }
	    return result;
	  }

	}

}
