package com.philon.rpg.mos.player.inventory;

import java.util.LinkedList;

import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.mos.item.AbstractItem;

public abstract class AbstractItemGrid {
  public AbstractItem[][] grid;
  public Vector gridSize;
  
  public LinkedList<AbstractItem> itemList;
  
  public AbstractItemGrid() {
    gridSize = getGridSize();
    grid = new AbstractItem[(int) gridSize.y][];
    for( int i=0; i<gridSize.y; i++ ) {
      grid[i] = new AbstractItem[(int) gridSize.x];
    }
    
    itemList = new LinkedList<AbstractItem>();
  }
  
  public abstract Vector getGridSize();
  
  /**
   * <p>
   * attempts to insert an item to the grid. 
   * <p>
   * possible return values: <br>
   *  - null (item inserted without problem) <br>
   *  - the item passed to the function (failed to insert) <br>
   *  - another item (insert successful, returned item was displaced) <br>
   */
  public AbstractItem add( AbstractItem newItem, Vector newCell, boolean allowDisplacement ) {
    Vector itSize = newItem.invSize;
    if (Vector.add( newCell, itSize ).isEitherLarger(gridSize)) return newItem;

    AbstractItem displacedItem=null;
    AbstractItem occItem;

    for( int x=0; x<itSize.x; x++ ) {
      for( int y=0; y<itSize.y; y++ ) {
        occItem = grid[(int) (newCell.y+y)][(int) (newCell.x+x)];
        if( occItem!=null && displacedItem==null ) {
          if (!allowDisplacement) return newItem;
          displacedItem = occItem;
        }
        if (!(occItem==null || occItem==displacedItem)) return newItem;
      }
    }

    if(displacedItem!=null) {
      RpgGame.inst.localPlayer.inv.pickupItem( displacedItem );
    }

    for( int x = 0; x <= itSize.x-1; x++ ) {
      for( int y = 0; y <= itSize.y-1; y++ ) {
        grid[(int) (newCell.y+y)][(int) (newCell.x+x)] = newItem;
      }
    }

    itemList.add( newItem );
    newItem.pos = newCell.copy();
    newItem.changeState( AbstractItem.StateInv.class );
    
    return displacedItem;
  }
  
  public boolean addPickup( Vector newCell, boolean allowDisplacement ) {
    int tmpSouDrop = RpgGame.inst.localPlayer.inv.pickedUpItem.souDrop;
    AbstractItem displacedItem = add( RpgGame.inst.localPlayer.inv.pickedUpItem, newCell, allowDisplacement );
    if (displacedItem==RpgGame.inst.localPlayer.inv.pickedUpItem){
      return false;
    } else if (displacedItem==null) {
      RpgGame.inst.localPlayer.inv.pickedUpItem=null;
    } else {
      RpgGame.inst.localPlayer.inv.pickupItem(displacedItem);
    }
    if(tmpSouDrop>0) RpgGame.playSoundFX(tmpSouDrop);
    return true;
  }
  
  public boolean addAuto(AbstractItem newItem) {
    for( int x=0; x<gridSize.x; x++ ) {
      for( int y=0; y<gridSize.y; y++ ) {
        AbstractItem result = add( newItem, new Vector(x, y), false );
        if (result!=newItem) return true;
      }
    }
    return false;
  }
  
  public boolean addPickupAuto() {
    if (addAuto(RpgGame.inst.localPlayer.inv.pickedUpItem) ) {
      RpgGame.inst.localPlayer.inv.pickedUpItem = null;
      return true;
    }
    return false;
  }
  
  public void remove( AbstractItem it ) {
    if (!itemList.contains(it)) return;
    Vector itSize = it.invSize;

    for( int x = 0; x <= itSize.x-1; x++ ) {
      for( int y = 0; y <= itSize.y-1; y++ ) {
        grid[(int) (it.pos.y+y)][(int) (it.pos.x+x)] = null;
      }
    }

    itemList.remove( it );
  }

  public AbstractItem getItemByCell( Vector cell ) {
    return grid[(int) cell.y][(int) cell.x];
  }
  
  public boolean contains(AbstractItem newItem) {
    return itemList.contains(newItem);
  }
}
