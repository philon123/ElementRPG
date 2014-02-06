package com.philon.rpg.mos.player.inventory;

import java.util.LinkedList;
import java.util.List;

import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.SoundData;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.item.ItemSaveData;
import com.philon.rpg.mos.item.category.AmuletItem;
import com.philon.rpg.mos.item.category.ArmorItem;
import com.philon.rpg.mos.item.category.ConsumableItem;
import com.philon.rpg.mos.item.category.HelmItem;
import com.philon.rpg.mos.item.category.RingItem;
import com.philon.rpg.mos.item.category.ShieldItem;
import com.philon.rpg.mos.item.category.WeaponItem;
import com.philon.rpg.mos.player.AbstractChar;
import com.philon.rpg.stat.effect.EffectsObj;

public class Inventory {
	public AbstractChar ownerPlayer;
	public EffectsObj effects;

	public Equip equip;
	public InventoryGrid invGrid;
	public BeltGrid beltGrid;

	public int currGold=10000;

	public AbstractItem pickedUpItem;
	public AbstractItem hoveredOverItem;
	public boolean twoHandedWeaponEquiped;
	
	//----------

	public Inventory( AbstractChar newOwnerPlayer ) {
		ownerPlayer = newOwnerPlayer;

		init();
		currGold = 100;
	}

	//----------
	
	public Inventory( InventorySaveData isd ) {
	  init();
		currGold = isd.currGold;

		for( ItemSaveData currItemSD : isd.invList ) {
			AbstractItem tmpItem = AbstractItem.load(currItemSD, ownerPlayer);
			invGrid.add( tmpItem, tmpItem.pos, false );
		}

		for( int i = 0; i <= isd.equip.length-1; i++ ) {
			if( isd.equip[i]!=null ) {
				equip.items[i] = AbstractItem.load( isd.equip[i], ownerPlayer );
			}
		}

		for( int i = 0; i <= isd.beltGrid.length-1; i++ ) {
			if( isd.beltGrid[i]!=null ) {
				beltGrid.itemList.add( AbstractItem.load( isd.beltGrid[i], ownerPlayer ) );
			}
		}

		equipChangedTrigger();
		updateEffects();
	}

	//----------

	public void init() {
		equip = new Equip();

		invGrid = new InventoryGrid();
		beltGrid = new BeltGrid();

		currGold = 0;

		updateEffects();
	}

	//----------

	public InventorySaveData save() {
		return InventorySaveData.create(this);
	}

	//----------
	
	private boolean isItemForBelt(AbstractItem newItem) {
	  return pickedUpItem instanceof ConsumableItem &&
        pickedUpItem.invSize.isAllEqual(new Vector(1));
	}

	public void equipChangedTrigger() {
		updateEffects();

		twoHandedWeaponEquiped=false;
		if( equip.getBySlot(Equip.INV_WEAPON)!=null ) {
			twoHandedWeaponEquiped = ((WeaponItem)equip.getBySlot(Equip.INV_WEAPON)).isTwoHanded();
		}

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
    RpgGame.playSoundFX(SoundData.SOU_PICKUP);
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
      result = beltGrid.addPickupAuto();
    } else {
      result = invGrid.addPickupAuto();
    }
    
    if( result ) {
      return true;
    } else {
      dropPickup();
      return false;
    }
  }

  public void dropPickup() {
    Vector newTile = RpgGame.inst.gMap.getNextFreeTile( ownerPlayer.pos, false, false, true, true );
    if( newTile != null ) {
      pickedUpItem.changeState( AbstractItem.StateMap.class );
      pickedUpItem.setPosition(newTile);
      pickedUpItem = null;
    }
  }

	public boolean addPickupToEquip( int targetSlot ) {
		if (!pickedUpItem.reqMetFlag) return false;

		Class<? extends AbstractItem> targetSlotClass = equip.equipType[targetSlot];
		if( targetSlotClass.isInstance(pickedUpItem) ) {
			AbstractItem displacedItem = equip.getBySlot(targetSlot);
			AbstractItem newItem = pickedUpItem;
			pickedUpItem = null;

			if( targetSlot == Equip.INV_WEAPON ) {
				if( ((WeaponItem)newItem).isTwoHanded() && equip.getBySlot(Equip.INV_SHIELD)!=null ) {
					if (!pickupAuto( equip.getBySlot(Equip.INV_SHIELD) )) return false;
				}
			} else if( targetSlot == Equip.INV_SHIELD ) {
				if( twoHandedWeaponEquiped && equip.getBySlot(Equip.INV_WEAPON)!=null ) {
					if (!pickupAuto( equip.getBySlot(Equip.INV_WEAPON) )) return false;
				}
			}
			if (displacedItem!=null) pickupItem( displacedItem );
			
			equip.items[targetSlot] = newItem;
			newItem.pos = new Vector( targetSlot, 0 );
			newItem.changeState( AbstractItem.StateInv.class );
			if(newItem.souDrop>0) RpgGame.playSoundFX(newItem.souDrop);

			equipChangedTrigger();
			return true;
		}

		return false;
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
    
    @Override
    public Vector getGridSize() {
      return new Vector(10, 1);
    }
    
  }
	
	public class Equip {
	  public int numSlots = 7;

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
