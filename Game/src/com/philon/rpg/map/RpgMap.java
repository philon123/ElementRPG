package com.philon.rpg.map;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeMap;

import com.philon.engine.util.Path;
import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.map.mo.RpgMapObj.RpgMapObjSaveData;
import com.philon.rpg.map.mo.UpdateMapObj;
import com.philon.rpg.mos.door.AbstractDoor;
import com.philon.rpg.mos.shot.AbstractShot;
import com.philon.rpg.mos.stairs.AbstractStairs;
import com.philon.rpg.mos.stairs.StairsDown;
import com.philon.rpg.mos.stairs.StairsUp;
import com.philon.rpg.mos.wall.AbstractMapStyle;
import com.philon.rpg.mos.wall.AbstractWall;
import com.philon.rpg.util.RenderMapKey;
import com.philon.rpg.util.RpgUtil;

public class RpgMap {
	public Vector gridSize;
	public MapTile grid[][];
	public AbstractMapStyle mapStyle;

	public LinkedList<UpdateMapObj> dynamicMapObjs = new LinkedList<UpdateMapObj>();
	public LinkedList<RpgMapObj> staticMapObjs = new LinkedList<RpgMapObj>();

  public LinkedList<RpgMapObj> staticLightSources = new LinkedList<RpgMapObj>();;
  public LinkedList<RpgMapObj> dynamicLightSources = new LinkedList<RpgMapObj>();;

  public HashSet<RpgMapObj> staticSeeThroughObjects = new HashSet<RpgMapObj>();
  public HashSet<RpgMapObj> seeThroughObjects = new HashSet<RpgMapObj>();

  public Vector spawnComingDown;
  public Vector spawnComingUp;

  private boolean staticLightGridDirty = true;

//	public AStarMap aStarMap; //TODO astar

  public RpgMap( RpgMapSaveData saveData ) {
    mapStyle = saveData.mapStyle;
		gridSize = saveData.mapSize.copy();

		//grid
		grid = new MapTile[(int) gridSize.y][];
		for( int y = 0; y < gridSize.y; y++ ) {
			grid[y] = new MapTile[(int) gridSize.x];
			for( int x = 0; x < gridSize.x; x++ ) {
				grid[y][x] = new MapTile( new Vector(x, y) );
			}
		}

		//load objects
		for(RpgMapObjSaveData currMOData : saveData.mos) {
		  RpgMapObj newObj = currMOData.load();
		  insertMapObj( newObj );
		  if( newObj instanceof AbstractStairs ) {
		    Vector spawnTile = newObj.pos.copy().addInst( ((AbstractStairs)newObj).getSpawnOffset() );
  		  if( newObj instanceof StairsDown ) {
  		    spawnComingUp = spawnTile;
  		  } else if( newObj instanceof StairsUp ) {
  		    spawnComingDown = spawnTile;
  		  }
		  }
		}

		updateStaticSeeThroughObjects();
		updateStaticLightGrid();
  }

	public RpgMapSaveData save() {
		return new RpgMapSaveData(this);
	}

	public void insertMapObj( RpgMapObj newObj ) {
	  if (newObj instanceof UpdateMapObj) {
	    dynamicMapObjs.add((UpdateMapObj)newObj);
	  } else {
	    staticMapObjs.add(newObj);
	  }

	  cleanMapObj(newObj);
	}

	public void removeMapObj( RpgMapObj obj ) {
	  if (obj instanceof UpdateMapObj) {
      if (obj.luminance>0) dynamicLightSources.remove(obj);
      dynamicMapObjs.remove((UpdateMapObj)obj);
    } else {
      if (obj.luminance>0) staticLightSources.remove(obj);
      staticMapObjs.remove(obj);
    }

	  removeFromGrid(obj, obj.currOccTiles);
	}

  public void updateMapObjs() {
    for (int i=0; i<dynamicMapObjs.size(); i++) {
      UpdateMapObj currMO = dynamicMapObjs.get(i);
      if( RpgGame.inst.gGraphics.isTileOnScreen( currMO.pos.copy().roundAllInst() ) || currMO instanceof AbstractShot ) {
        currMO.update();
        if( currMO.dirty ) {
          cleanMapObj(currMO);
        }
      }
    }

    if(staticLightGridDirty) updateStaticLightGrid();
  }

  public void cleanMapObj( RpgMapObj obj ) {
    updateOccTiles(obj);
    bakeBasePixDimensions(obj);
    updateLuminance(obj);
  }

  public void updateLuminance(RpgMapObj obj) { //TODO optimize, maybe use new luminanceDirty flag
    if (obj instanceof UpdateMapObj) {
      if( dynamicLightSources.contains(obj) ) {
        if( obj.luminance==0 ) {
          dynamicLightSources.remove(obj);
        }
      } else {
        if( obj.luminance!=0 ) {
          dynamicLightSources.add(obj);
        }
      }
    } else {
      if( staticLightSources.contains(obj) ) {
        if( obj.luminance==0 ) {
          staticLightSources.remove(obj);
        }
      } else {
        if( obj.luminance!=0 ) {
          staticLightSources.add(obj);
        }
      }
      staticLightGridDirty = true;
    }
  }

	public void updateOccTiles( RpgMapObj mo ) {
	  LinkedList<Vector> oldOccTiles = null;

	  //update data on mo
	  LinkedList<Vector> newOccTiles = getOccTilesByRect( mo.pos, mo.collRect );
    if( mo.currOccTiles==null ) {
      mo.currOccTiles = newOccTiles;
    } else if( !mo.currOccTiles.equals(newOccTiles) ) {
      oldOccTiles = mo.currOccTiles;
      mo.currOccTiles = newOccTiles;
    } else {
      return;
    }

    removeFromGrid(mo, oldOccTiles);
    insertToGrid(mo);
  }

  private void bakeBasePixDimensions( RpgMapObj obj ) {
    obj.basePixPos = RpgGame.inst.gGraphics.getBasePixPosByTilePos( obj.pos );
    obj.baseImgPixSize = RpgGame.inst.gGraphics.getPixSizeByTileSize( obj.animation==null ? new Vector() : obj.imgTileSize );

    Vector tmpPixOffset = obj.baseImgPixSize.copy().mulScalarInst(-1);
    tmpPixOffset.x *= 0.5;
    tmpPixOffset.y += RpgGame.inst.gGraphics.getPixSizeByTileSize(new Vector(0.5f)).y;

    obj.baseImgPixPos = Vector.add( obj.basePixPos, tmpPixOffset );
  }

  public TreeMap<RenderMapKey, RpgMapObj> generateRenderMap() {
    TreeMap<RenderMapKey, RpgMapObj> result = new TreeMap<RenderMapKey, RpgMapObj>();

    int minX = (int)RpgGame.inst.gGraphics.minTileOnScreen.x;
    int minY = (int)RpgGame.inst.gGraphics.minTileOnScreen.y;
    int maxX = (int)RpgGame.inst.gGraphics.maxTileOnScreen.x;
    int maxY = (int)RpgGame.inst.gGraphics.maxTileOnScreen.y;
    for( int y=minY; y<=maxY; y++ ) {
      for( int x=minX; x<=maxX; x++ ) {
        for( RpgMapObj currObj : grid[y][x].collList ) {
          result.put( new RenderMapKey(currObj.pos, currObj.hashCode()), currObj );
        }
      }
    }

    return result;
  }

  private void removeFromGrid( RpgMapObj obj, LinkedList<Vector> occTiles ) {
	  if( occTiles!=null ) {
      for( Vector currTile : occTiles ) {
        grid[(int) currTile.y][(int) currTile.x].collList.remove(obj);
        //if( isTileFree(currTile.copy(), true, true, true) ) { //TODO astar
        //  aStarMap.setvalue(currTile.x, currTile.y, 0, 0);
        //}
      }
    }
	}

	private void insertToGrid( RpgMapObj obj ) {
	  if( obj.currOccTiles!=null ) {
      for( Vector currTile : obj.currOccTiles ) {
        grid[(int) currTile.y][(int) currTile.x].collList.addLast(obj);
      }
      //aStarMap.setvalue(Math.round(mo.pos.x), Math.round(mo.pos.y), 0, 1);
    }
  }

	public Vector getNextFreeTile( Vector newPos, boolean checkForPlayer, boolean checkForEnemy, boolean checkForItem, boolean checkForBreakable ) {
		Vector newTile = newPos.copy().roundAllInst();
		if( isTileFree(newTile, checkForPlayer, checkForEnemy, checkForItem, checkForBreakable ) ) {
			return newTile;
		} else {
			for( int y = -1; y <= 1; y++ ) {
				for( int x = -1; x <= 1; x++ ) {
					Vector tmpPos = new Vector( newTile.x+x, newTile.y+y );
				  if (isTileFree(tmpPos, checkForPlayer, checkForEnemy, checkForItem, checkForBreakable) ) {
				    return tmpPos;
				  }
				}
			}
		}

		return null;
	}

	public boolean isTileFree( Vector newTile, boolean checkForPlayer, boolean checkForEnemy, boolean checkForItem, boolean checkForBreakable ) {
	  LinkedList<RpgMapObj> mosOnTile = getRectColls( newTile, new Vector(1) );
	  if (mosOnTile==null) return true;
		mosOnTile = RpgUtil.filterList(mosOnTile, checkForPlayer, checkForEnemy, checkForItem, false, true, checkForBreakable);
		return mosOnTile==null ? true : mosOnTile.isEmpty();
	}

	public boolean isTileOnMap( Vector newTile ) {
		if (   newTile.isAllLOE(new Vector())
		    && newTile.isAllSOE(gridSize.copy().subInst(new Vector(1)))) {
			return true;
		} else {
			return false;
		}
	}

	public LinkedList<RpgMapObj> getRectColls( Vector newRectPos, Vector newRectSize ) {
		LinkedList<RpgMapObj> result = new LinkedList<RpgMapObj>();

		for( Vector tmpTile : getOccTilesByRect(newRectPos, newRectSize) ) {
			for( RpgMapObj tmpMo : grid[(int) tmpTile.y][(int) tmpTile.x].collList ) {
				if( tmpMo.isCollObj ) {
					if( !result.contains(tmpMo) ) {
						if( Util.rectsColliding( tmpMo.pos, tmpMo.collRect, newRectPos, newRectSize )!=null ) {
							result.addLast(tmpMo);
						}
					}
				}
			}
		}

		if (result.size()==0) return null;
		return result;
	}

	//----------

	public boolean getIsRectCollidingWithMap( Vector newPos, Vector newSize ) {
		LinkedList<RpgMapObj> colls = getRectColls( newPos, newSize );
		colls = RpgUtil.filterList( colls, false, false, false, false, false, false );
		if (colls==null) return false;
		return true;
	}

	//----------

	public boolean tilesInSight( Vector tile1, Vector tile2 ) {
		Vector deltaTilePos = Vector.sub( tile2.copy().roundAllInst(), tile1.copy().roundAllInst() );
		Vector deltaDir = Vector.normalize(deltaTilePos);
		if (tile1.isAllEqual(tile2)) return true;

		//find correct target tile corner
		Vector target = tile2.copy();
		if( deltaDir.x<0 ) {
			target.x += 0.5;
		} else if( deltaDir.x>0 ) {
			target.x -= 0.5;
		}
		if( deltaDir.y<0 ) {
			target.y += 0.5;
		} else if( deltaDir.y>0 ) {
			target.y -= 0.5;
		}

		//check path
		Vector tmpPos = tile1.copy();
		deltaDir = Vector.sub( target, tmpPos ).normalizeInst().mulScalarInst(0.5f);
		while( true ) {
			tmpPos.addInst(deltaDir);
			Vector tmpTile = tmpPos.copy().roundAllInst();
			if( tmpTile.isAllEqual(tile2) ) {
				return true;
			}

			if (getIsRectCollidingWithMap( tmpPos, new Vector(0.01f) )) return false;
		}
	}

	private void updateStaticSeeThroughObjects() {
    HashSet<RpgMapObj> result = new HashSet<RpgMapObj>();

    staticSeeThroughObjects = result;
  }

	public void updateSeeThroughObjects() {
    seeThroughObjects.clear();
    seeThroughObjects.addAll(staticSeeThroughObjects);

    LinkedList<Vector> tilesToTest = new LinkedList<Vector>();
    tilesToTest.addLast( new Vector(1, 0) );
    tilesToTest.addLast( new Vector(0, 1) );
    tilesToTest.addLast( new Vector(1, 1) );
    tilesToTest.addLast( new Vector(2, 1) );
    tilesToTest.addLast( new Vector(1, 2) );
    tilesToTest.addLast( new Vector(2, 2) );
    tilesToTest.addLast( new Vector(2, 3) );
    tilesToTest.addLast( new Vector(3, 2) );
    tilesToTest.addLast( new Vector(3, 3) );

    for( RpgMapObj mo : dynamicMapObjs ) {
      if (!(mo.isSelectable)) continue;
      Vector moTile = mo.pos.copy().roundAllInst();
      if( !RpgGame.inst.gGraphics.isTileOnScreen( moTile ) ) continue;

      for( Vector currTile : tilesToTest ) {
        Vector tmpTile = Vector.add( currTile, moTile );
        if( !tmpTile.isAllSmaller(gridSize) ) continue;

        for( RpgMapObj tmpMo : grid[(int)tmpTile.y][(int)tmpTile.x].collList ) {
          if( !(tmpMo instanceof AbstractWall || tmpMo instanceof AbstractDoor) ) continue;
          if( !tmpMo.pos.copy().isAllEqual(tmpTile) ) continue;

          seeThroughObjects.add(tmpMo);
        }
      }
    }
  }

	private void updateStaticLightGrid() {
    //create light grid
    float[][] tmpLightGrid = new float[(int)gridSize.y][];
    for (int y=0; y<tmpLightGrid.length; y++) {
      tmpLightGrid[y] = new float[(int)gridSize.x];
      for (int x=0; x<tmpLightGrid[0].length; x++) {
        tmpLightGrid[y][x] = 0.1f;
      }
    }

    //fill light grid
    for( RpgMapObj mo : staticLightSources ) {
      int tmpRadius = 5;
      Vector moTile = mo.pos.copy().roundAllInst();
      for( int y = (int) moTile.y-tmpRadius; y <= moTile.y+tmpRadius; y++ ) {
        for( int x = (int) moTile.x-tmpRadius; x <= moTile.x+tmpRadius; x++ ) {
          Vector newTile = new Vector(x, y);
          if(isTileOnMap(newTile)) {
            float distance = RpgGame.inst.gUtil.getTileDistance(mo.pos, newTile);
            float ratio = (1 - (distance / tmpRadius)) * mo.luminance;
            if( tmpLightGrid[(int) newTile.y][(int) newTile.x] < ratio ) {
              tmpLightGrid[(int) newTile.y][(int) newTile.x] = ratio;
            }
          }
        }
      }
    }

    //apply light grid on screen
    for (int y=0; y<tmpLightGrid.length; y++) {
      for (int x=0; x<tmpLightGrid[0].length; x++) {
        grid[y][x].staticBrightness = tmpLightGrid[y][x];
      }
    }

    staticLightGridDirty = false;
  }

	public void updateLightGrid() {
    LinkedList<RpgMapObj> tmpLightSources = new LinkedList<RpgMapObj>();
    for( RpgMapObj mo : dynamicLightSources ) {
      if (RpgGame.inst.gGraphics.isTileOnScreen(mo.pos)) {
        tmpLightSources.add(mo);
      }
    }

    //create light grid for screen
    Vector intdelta = Vector.sub( RpgGame.inst.gGraphics.maxTileOnScreen, RpgGame.inst.gGraphics.minTileOnScreen ).floorAllInst();
    float[][] tmpLightGrid = new float[(int) (intdelta.y + 1)][];
    for (int y=0; y<tmpLightGrid.length; y++) {
      tmpLightGrid[y] = new float[(int) (intdelta.x + 1)];
    }

    //fill light grid
    for( RpgMapObj mo : tmpLightSources ) {
      int tmpRadius = 5;
      Vector moTile = mo.pos.copy().roundAllInst();
      for( int y = (int) moTile.y-tmpRadius; y <= moTile.y+tmpRadius; y++ ) {
        for( int x = (int) moTile.x-tmpRadius; x <= moTile.x+tmpRadius; x++ ) {
          Vector newTile = new Vector(x, y);
          Vector newLightGridTile = Vector.sub( newTile, RpgGame.inst.gGraphics.minTileOnScreen );
          if( RpgGame.inst.gGraphics.isTileOnScreen(newTile) ) {
            float distance = RpgGame.inst.gUtil.getTileDistance(mo.pos, newTile);
            float ratio = (1 - (distance / tmpRadius)) * mo.luminance;
            if( tmpLightGrid[(int) newLightGridTile.y][(int) newLightGridTile.x] < ratio ) {
              tmpLightGrid[(int) newLightGridTile.y][(int) newLightGridTile.x] = ratio;
            }
          }
        }
      }
    }

    //apply light grid on screen
    int minX = (int) RpgGame.inst.gGraphics.minTileOnScreen.x;
    int minY = (int) RpgGame.inst.gGraphics.minTileOnScreen.y;
    for (int y=minY; y<minY+tmpLightGrid.length; y++) {
      for (int x=minX; x<minX+tmpLightGrid[0].length; x++) {
        grid[y][x].currBrightness = grid[y][x].staticBrightness + tmpLightGrid[y-minY][x-minX];
      }
    }
  }

	public LinkedList<RpgMapObj> selectMOsAtPixel( Vector newPixel ) {
	  LinkedList<RpgMapObj> result = new LinkedList<RpgMapObj>();

    Vector newTile = RpgGame.inst.gGraphics.getTilePosByPixPos(newPixel);
    if (newTile==null) return result;
    newTile.roundAllInst();

    LinkedList<Vector> tilesToTest = new LinkedList<Vector>();
    tilesToTest.addLast( new Vector(0, 0) );
//    tilesToTest.addLast( new Vector(1, 0) ); //TODO getMOsAtPixel
//    tilesToTest.addLast( new Vector(0, 1) );
//    tilesToTest.addLast( new Vector(1, 1) );
//    tilesToTest.addLast( new Vector(2, 1) );
//    tilesToTest.addLast( new Vector(1, 2) );
//    tilesToTest.addLast( new Vector(2, 2) );

    for( Vector currTile : tilesToTest ) {
      currTile.addInst(newTile);
      if( !currTile.isAllSmaller(RpgGame.inst.gMap.gridSize) ) continue;

      for( RpgMapObj tmpMo : RpgGame.inst.gMap.grid[(int) currTile.y][(int) currTile.x].collList ) {
        if( !tmpMo.isSelectable ) continue;
        if( result.contains(tmpMo) ) continue;
        if (!tmpMo.pos.copy().roundAllInst().equals(currTile)) continue;
        if( !RpgUtil.isPixelInMapObj(tmpMo, newPixel) ) continue;

        result.addLast(tmpMo);
      }
    }

    return result;
  }

  public LinkedList<Vector> getOccTilesByRect( Vector newRectPos, Vector newRectSize ) {
    LinkedList<Vector> result=new LinkedList<Vector>();

    Vector vmin = Vector.sub( newRectPos, newRectSize.copy().mulScalarInst(0.5f) ).roundAllInst();
    Vector vmax = Vector.add( newRectPos, newRectSize.copy().mulScalarInst(0.5f) ).subInst(new Vector(0.001f)).roundAllInst();
    if (vmin.x<0) vmin.x=0;
    if (vmin.y<0) vmin.y=0;
    if (vmax.x>gridSize.x-1) vmax.x=gridSize.x-1;
    if (vmax.y>gridSize.y-1) vmax.y=gridSize.y-1;

    for( int x = (int) vmin.x; x <= vmax.x; x++ ) {
      for( int y = (int) vmin.y; y <= vmax.y; y++ ) {
        result.addLast(new Vector(x, y));
      }
    }

    return result;
  }

  public Path getAStarPath( Vector tile1, Vector tile2 ) { //TODO astar
  //init aStarMap //TODO astar - move to init
//  aStarMap = AStarMap.create( gridSize.x, gridSize.y, 1 );
//  for( int x = 0; x < gridSize.x; x++ ) {
//    for( int y = 0; y < gridSize.y; y++ ) {
//      if( !(grid[y][x].tileType==RoomData.TILE_FLOOR) ) {
//        aStarMap.setvalue( x, y, 0, 1 );
//      }
//    }
//  }


//    tile1 = tile1.copy().roundAllInst() ; tile2 = tile2.copy().roundAllInst();
//
//    LinkedList<Node> nodePath = Node.astar8( tile1.x, tile1.y, tile2.x, tile2.y, aStarMap, 0, 1, false, 0 );
//    if (nodePath==null) return null;
//
//    nodePath.removeFirst(); //dont need to go to the tile we are starting from...
//    if( !isTileFree(tile2, true, true, false) ) {
//      nodePath.removeLast();
//    }
//    if (nodePath.isEmpty()) return null;
//
//    PathNode result[] = new PathNode[nodePath.length];
//    Node node;
//    int i=0;
//    for( node : nodePath ) {
//      result[i] = new PathNode( new Vector(node.x, node.y) );
//      i += 1;
//    }
//
//    return new Path(result);
    return null;
  }

}
