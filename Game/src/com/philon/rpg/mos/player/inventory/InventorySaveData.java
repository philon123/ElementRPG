package com.philon.rpg.mos.player.inventory;
import java.util.LinkedList;

import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.item.ItemSaveData;

public class InventorySaveData {
	public ItemSaveData equip[];
	public LinkedList<ItemSaveData> invList = new LinkedList<ItemSaveData>();
	public ItemSaveData beltGrid[];
	public int currGold;

	//----------

	public static InventorySaveData create( Inventory inv ) {
		InventorySaveData isd = new InventorySaveData();

		isd.equip = new ItemSaveData[inv.equip.items.length];
		for( int i = 0; i <= inv.equip.items.length-1; i++ ) {
			if( inv.equip.items[i]!=null ) {
				isd.equip[i] = inv.equip.items[i].save();
			}
		}

		isd.invList = new LinkedList<ItemSaveData>();
		for( AbstractItem currItem : inv.invGrid.itemList ) {
			isd.invList.addLast( currItem.save() );
		}

		isd.beltGrid = new ItemSaveData[(int) inv.beltGrid.gridSize.x];
		for( int i = 0; i < inv.beltGrid.gridSize.x; i++ ) {
			if( inv.beltGrid.grid[0][i]!=null ) {
				isd.beltGrid[i] = inv.beltGrid.grid[0][i].save();
			}
		}

		isd.currGold = inv.currGold;

		return isd;
	}

	//----------

}
