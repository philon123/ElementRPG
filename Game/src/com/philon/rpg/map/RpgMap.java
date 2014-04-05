package com.philon.rpg.map;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeMap;

import com.philon.engine.util.Vector;
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

	public void insertMapObj( RpgMapObj obj ) {
	  if (obj instanceof UpdateMapObj) {
	    dynamicMapObjs.add((UpdateMapObj)obj);
	  } else {
	    staticMapObjs.add(obj);
	  }

	  cleanMapObj(obj);
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

  public void updateMapObjs() {
    for (int i=0; i<dynamicMapObjs.size(); i++) {
      UpdateMapObj currMO = dynamicMapObjs.get(i);
      if( RpgUtil.isTileOnScreen( currMO.pos.copy().roundAllInst() ) || currMO instanceof AbstractShot ) {
        currMO.update();
        if( currMO.dirty ) {
          cleanMapObj(currMO);
        }
      }
    }

    if(staticLightGridDirty) updateStaticLightGrid();
  }

  private void cleanMapObj( RpgMapObj obj ) {
    updateOccTiles(obj);
    bakeBaseScreenDimensions(obj);
    updateLuminance(obj);
  }

  private void updateLuminance(RpgMapObj obj) { //TODO optimize, maybe use new luminanceDirty flag
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

  private void updateOccTiles( RpgMapObj mo ) {
	  LinkedList<Vector> oldOccTiles = null;

	  //update data on mo
	  LinkedList<Vector> newOccTiles = RpgUtil.getOccTilesByRect( mo.pos, mo.collRect, gridSize );
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
      if( !RpgUtil.isTileOnScreen( moTile ) ) continue;

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
          if(RpgUtil.isTileOnMap(newTile)) {
            float distance = RpgUtil.getTileDistance(mo.pos, newTile);
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

  public void updateLightGrid(Vector minTile, Vector maxTile) {
    LinkedList<RpgMapObj> tmpLightSources = new LinkedList<RpgMapObj>();
    for( RpgMapObj mo : dynamicLightSources ) {
      if (RpgUtil.isTileOnScreen(mo.pos)) {
        tmpLightSources.add(mo);
      }
    }

    //create light grid for screen
    Vector intdelta = Vector.sub( maxTile, minTile ).floorAllInst();
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
          Vector newLightGridTile = Vector.sub( newTile, minTile );
          if( RpgUtil.isTileOnScreen(newTile) ) {
            float distance = RpgUtil.getTileDistance(mo.pos, newTile);
            float ratio = (1 - (distance / tmpRadius)) * mo.luminance;
            if( tmpLightGrid[(int) newLightGridTile.y][(int) newLightGridTile.x] < ratio ) {
              tmpLightGrid[(int) newLightGridTile.y][(int) newLightGridTile.x] = ratio;
            }
          }
        }
      }
    }

    //apply light grid on screen
    int minX = (int)minTile.x;
    int minY = (int)minTile.y;
    for (int y=minY; y<minY+tmpLightGrid.length; y++) {
      for (int x=minX; x<minX+tmpLightGrid[0].length; x++) {
        grid[y][x].currBrightness = grid[y][x].staticBrightness + tmpLightGrid[y-minY][x-minX];
      }
    }
  }

  private void bakeBaseScreenDimensions( RpgMapObj obj ) {
    obj.baseScreenPos = RpgUtil.getBaseScreenPosByTilePos( obj.pos );

    if(obj.animation==null) {
      obj.baseImgScreenSize = new Vector();
      return;
    }
    obj.baseImgScreenSize = RpgUtil.getImageScreenSize(obj.animation.image.xyRatio).mulScalarInst(obj.animation.image.scaleByX);
    obj.baseImgScreenPos = Vector.add( obj.baseScreenPos, RpgUtil.getScreenOffsetForImageSize(obj.baseImgScreenSize) );
  }

  public TreeMap<RenderMapKey, RpgMapObj> generateRenderMap(Vector minTile, Vector maxTile) {
    TreeMap<RenderMapKey, RpgMapObj> result = new TreeMap<RenderMapKey, RpgMapObj>();

    int minX = (int)minTile.x;
    int minY = (int)minTile.y;
    int maxX = (int)maxTile.x;
    int maxY = (int)maxTile.y;
    for( int y=minY; y<=maxY; y++ ) {
      for( int x=minX; x<=maxX; x++ ) {
        for( RpgMapObj currObj : grid[y][x].collList ) {
          result.put( new RenderMapKey(currObj.pos, currObj.hashCode()), currObj );
        }
      }
    }

    return result;
  }

}
