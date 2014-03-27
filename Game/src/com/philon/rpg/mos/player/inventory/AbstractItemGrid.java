package com.philon.rpg.mos.player.inventory;

import java.util.LinkedList;

import javax.management.RuntimeErrorException;

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
   * checks if an item can be inserted to the grid.
   * <p>
   * possible return values: <br>
   *  - null (yes, no displacement) <br>
   *  - the item passed to the function (no) <br>
   *  - another item (yes, and returned item was displaced) <br>
   */
  public AbstractItem isCanInsert( AbstractItem newItem, Vector newCell, boolean allowDisplacement ) {
    Vector itSize = newItem.invSize;
    if (Vector.add( newCell, itSize ).isEitherLarger(gridSize)) return newItem;

    AbstractItem displacedItem=null;

    for( int x=0; x<itSize.x; x++ ) {
      for( int y=0; y<itSize.y; y++ ) {
        AbstractItem occItem = grid[(int) (newCell.y+y)][(int) (newCell.x+x)];
        if(occItem==null) continue; //no collision -> check next
        if(!allowDisplacement ) return newItem; //no displacement allowed -> fail
        if(displacedItem!=null && displacedItem!=occItem ) return newItem; //collision with several items -> fail

        displacedItem = occItem;
      }
    }

    return displacedItem;
  }

  public void insert(AbstractItem newItem, Vector newCell) {
    Vector itSize = newItem.getInvSize();
    for( int x = 0; x < itSize.x; x++ ) {
      for( int y = 0; y < itSize.y; y++ ) {
        if(grid[(int) (newCell.y+y)][(int) (newCell.x+x)]!=null)
          throw new RuntimeErrorException(new Error(), "You tried to overwrite an Item!");
        grid[(int) (newCell.y+y)][(int) (newCell.x+x)] = newItem;
      }
    }
    itemList.add( newItem );
    newItem.pos = newCell.copy();
    newItem.changeState( AbstractItem.StateInv.class );
  }

  /**
   * <p>
   * attempts to insert an item to the grid.
   * <p>
   * possible return values: <br>
   *  - 1 - null (item inserted without problem) <br>
   *  - 2 - the item passed to the function (failed to insert) <br>
   *  - 3 - another item (insert successful, returned item was displaced) <br>
   */
  public AbstractItem add( AbstractItem newItem, Vector newCell, boolean allowDisplacement ) {
    AbstractItem displacedItem = isCanInsert(newItem, newCell, allowDisplacement);
    if(displacedItem==newItem) return newItem; //case 2

    if(displacedItem==null) { //case 1
      insert(newItem, newCell);
      return null;
    }

    //case 3
    remove(displacedItem);
    insert(newItem, newCell);
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
    RpgGame.playSoundFX(tmpSouDrop);
    return true;
  }

  public boolean addAuto(AbstractItem newItem) {
    for( int x=0; x<gridSize.x; x++ ) {
      for( int y=0; y<gridSize.y; y++ ) {
        if(isCanInsert(newItem, new Vector(x,y), false)==null) {
          insert( newItem, new Vector(x, y));
          return true;
        }
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
