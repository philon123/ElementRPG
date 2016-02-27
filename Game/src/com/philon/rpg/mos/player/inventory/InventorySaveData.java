package com.philon.rpg.mos.player.inventory;
import java.util.LinkedList;

import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.item.AbstractItem.ItemSaveData;
import com.philon.rpg.mos.player.inventory.Inventory.BeltGrid;
import com.philon.rpg.mos.player.inventory.Inventory.Equip;

public class InventorySaveData {
	public ItemSaveData equip[] = new ItemSaveData[Equip.numSlots];
	public LinkedList<ItemSaveData> invList = new LinkedList<ItemSaveData>();
	public ItemSaveData beltGrid[] = new ItemSaveData[BeltGrid.length];
	public int currGold = 0;

	public InventorySaveData() {
	}

	public InventorySaveData( Inventory inv ) {
		for( int i = 0; i < inv.equip.items.length; i++ ) {
			if( inv.equip.items[i]!=null ) {
				equip[i] = inv.equip.items[i].save();
			}
		}

		for( AbstractItem currItem : inv.invGrid.itemList ) {
			invList.addLast( currItem.save() );
		}

		for( int i = 0; i < inv.beltGrid.gridSize.x; i++ ) {
			if( inv.beltGrid.grid[0][i]!=null ) {
				beltGrid[i] = inv.beltGrid.grid[0][i].save();
			}
		}

		currGold = inv.currGold;
	}

}
