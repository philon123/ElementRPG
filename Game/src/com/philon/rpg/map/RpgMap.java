package com.philon.rpg.map;
import java.util.LinkedList;

import com.philon.engine.FrameAnimation;
import com.philon.engine.PhilonGame;
import com.philon.engine.util.Path;
import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;
import com.philon.rpg.ImageData;
import com.philon.rpg.RpgGame;
import com.philon.rpg.map.generator.RoomData;
import com.philon.rpg.mo.RpgMapObj;
import com.philon.rpg.mos.SmithyVendor;
import com.philon.rpg.mos.breakables.BreakableBarrel;
import com.philon.rpg.mos.breakables.BreakableVase;
import com.philon.rpg.mos.chest.AbstractChest;
import com.philon.rpg.mos.chest.ChestData;
import com.philon.rpg.mos.door.AbstractDoor;
import com.philon.rpg.mos.door.DoorData;
import com.philon.rpg.mos.enemy.AbstractEnemy;
import com.philon.rpg.mos.enemy.EnemyData;
import com.philon.rpg.mos.enemy.EnemySkeleton;
import com.philon.rpg.mos.light.Firestand;
import com.philon.rpg.mos.light.Torch;
import com.philon.rpg.mos.stairs.StairsDown;
import com.philon.rpg.mos.stairs.StairsUp;
import com.philon.rpg.mos.wall.AbstractMapStyle;
import com.philon.rpg.mos.wall.WallData;

public class RpgMap {
	public Vector gridSize;
	public MapTile grid[][];

	public Vector spawnComingDownTile;
	public Vector spawnComingUpTile;
	public AbstractMapStyle mapStyle;

//	public AStarMap aStarMap; //TODO astar

	//----------

	/**
	 * <p>needs additional call of</p>
	 * <p>{@code init( int newMapData[][], int newMapStyle )}</p>
	 * <p>to function</p>
	 */
	public RpgMap() {
  }

	//----------

	public void init( int newMapData[][], AbstractMapStyle newMapStyle ) { //requires mapData and gridSize
		mapStyle = newMapStyle;
		gridSize = new Vector(newMapData.length, newMapData[0].length);

		//grid
		grid = new MapTile[(int) gridSize.y][];
		for( int y = 0; y < gridSize.y; y++ ) {
			grid[y] = new MapTile[(int) gridSize.x];
			for( int x = 0; x < gridSize.x; x++ ) {
				grid[y][x] = new MapTile( new Vector(x, y), newMapData[y][x] );
			}
		}

		initTerrainMos(newMapData);
		RpgGame.inst.gGraphics.updateStaticSeeThroughObjects();
		RpgGame.inst.gGraphics.updateStaticLightGrid();

	  //init aStarMap //TODO astar
//  aStarMap = AStarMap.create( gridSize.x, gridSize.y, 1 );
//  for( int x = 0; x < gridSize.x; x++ ) {
//    for( int y = 0; y < gridSize.y; y++ ) {
//      if( !(grid[y][x].tileType==RoomData.TILE_FLOOR) ) {
//        aStarMap.setvalue( x, y, 0, 1 );
//      }
//    }
//  }

		//determine spawn tiles
    initMapObjects();
	}

	public void init( RpgMapSaveData saveData ) {
    init( saveData.mapData, saveData.mapStyle );

    //restore toggle objects
//    Local TVector toggledTile, MapObj mo
//    For toggledTile=EachIn saveData.toggledObjects
//      For mo=EachIn toggleMOs
//        If Pos.IsAllEqual(toggledTile) Then
//          Local ToggleMapObj currToggleGO = ToggleMapObj(mo)
//          If currToggleGO!=Null Then
//            If currToggleGO.currState=ToggleMapObj.STATE_CLOSED Then
//              currToggleGO.toggle()
//            EndIf
//          EndIf
//        EndIf
//      Next
//    Next
  }

	//----------

	public void deleteObject() { //destroy everything
		grid=null;
		gridSize=null;
//		aStarMap=null; //TODO astar
	}

	//----------

	public RpgMapSaveData save() {
		return new RpgMapSaveData(this);
	}

	//----------

	public Path getAStarPath( Vector tile1, Vector tile2 ) { //TODO astar
//		tile1 = tile1.copy().roundAllInst() ; tile2 = tile2.copy().roundAllInst();
//
//		LinkedList<Node> nodePath = Node.astar8( tile1.x, tile1.y, tile2.x, tile2.y, aStarMap, 0, 1, false, 0 );
//		if (nodePath==null) return null;
//
//		nodePath.removeFirst(); //dont need to go to the tile we are starting from...
//		if( !isTileFree(tile2, true, true, false) ) {
//			nodePath.removeLast();
//		}
//		if (nodePath.isEmpty()) return null;
//
//		PathNode result[] = new PathNode[nodePath.length];
//		Node node;
//		int i=0;
//		for( node : nodePath ) {
//			result[i] = new PathNode( new Vector(node.x, node.y) );
//			i += 1;
//		}
//
//		return new Path(result);
	  return null;
	}

	//----------

	public void initTerrainMos( int newMapData[][] ) {
		boolean isStairsUpPlaced=false;
		boolean isStairsDownPlaced=false;
		int currLevel=RpgGame.inst.currLevel;
		if( currLevel==0 ) {
			spawnComingDownTile=new Vector(2, 2);
			isStairsUpPlaced=true;
		}
		if (currLevel==RpgGame.inst.numLevels-1) isStairsDownPlaced=true;

		for( int y = 0; y < gridSize.y; y++ ) {
			for( int x = 0; x < gridSize.x; x++ ) {
				Vector currTile = new Vector(x, y);
				Vector tmpDir=new Vector(0, 1);

				boolean r;
				boolean l;
				boolean t;
				boolean b;

				//get connections
				r = (currTile.x+1>gridSize.x-1) ? false :
					(newMapData[(int) currTile.y][(int) (currTile.x+1)]==RoomData.TILE_WALL);
				b = (currTile.y+1>gridSize.y-1) ? false :
					(newMapData[(int) (currTile.y+1)][(int) currTile.x]==RoomData.TILE_WALL);
				l = (currTile.x-1<0) ? false :
					(newMapData[(int) currTile.y][(int) (currTile.x-1)]==RoomData.TILE_WALL);
				t = (currTile.y-1<0) ? false :
					(newMapData[(int) (currTile.y-1)][(int) currTile.x]==RoomData.TILE_WALL);

				switch( newMapData[y][x] ) {
					case RoomData.TILE_WALL : //wall

						//determine if self and neighbors are blocks
						boolean isBlock=getIsBlock( new Vector(currTile.x, currTile.y), newMapData );
						if( isBlock ) {
							if (r) r=getIsBlock( new Vector(currTile.x+1, currTile.y), newMapData );
							if (l) l=getIsBlock( new Vector(currTile.x-1, currTile.y), newMapData );
							if (t) t=getIsBlock( new Vector(currTile.x, currTile.y-1), newMapData );
							if (b) b=getIsBlock( new Vector(currTile.x, currTile.y+1), newMapData );
						}

						WallData.createWall(mapStyle.getWallClass(), new Vector(x, y), l, r, t, b, isBlock);
						break;
					case RoomData.TILE_LIGHT:
						if ( getIsBlock(new Vector(x-1, y), newMapData) ) { //create torch
						  Torch newTorch = new Torch();
              newTorch.setPosition(new Vector(x-1, y));
              newTorch.turnToDirection(new Vector(1, 0));
              newTorch.setAnimation(new FrameAnimation(ImageData.images[ImageData.IMG_MAP_TORCH], (int)(PhilonGame.fps/3), false));
						} else if( getIsBlock(new Vector(x, y-1), newMapData) ) {
						  Torch newTorch = new Torch();
              newTorch.setPosition(new Vector(x, y-1));
              newTorch.turnToDirection(new Vector(0, 1));
              newTorch.setAnimation(new FrameAnimation(ImageData.images[ImageData.IMG_MAP_TORCH], (int)(PhilonGame.fps/3), false));
						} else { //create firestand
						  Firestand newFireStand = new Firestand();
						  newFireStand.setPosition(new Vector(x, y));
						  newFireStand.setAnimation(new FrameAnimation(ImageData.images[ImageData.IMG_MAP_FIRESTAND], (int)(PhilonGame.fps/3), false));
						}

						break;
					case RoomData.TILE_DOOR:
						if (l && r) tmpDir=new Vector(0, 1);
						if (t && b) tmpDir=new Vector(1, 0);

						if( tmpDir!=null ) {
						  AbstractDoor newDoor = DoorData.createDoor(mapStyle.getDoorClass());
						  newDoor.setPosition(new Vector(x, y));
						  newDoor.turnToDirection(tmpDir);
						  newDoor.setImgScale(new Vector(2)); //TODO square images
						}

						break;
					case RoomData.TILE_STAIRS:
						if (Math.random()>0.5) tmpDir = new Vector(1, 0);

						if( !isStairsDownPlaced ) {
						  StairsDown newStairs = new StairsDown();
						  newStairs.setPosition(new Vector(x, y));
						  newStairs.turnToDirection(tmpDir);
							spawnComingUpTile=new Vector(x+1, y+1);
							isStairsDownPlaced=true;
						} else if( ! isStairsUpPlaced ) {
						  StairsUp newStairs = new StairsUp();
              newStairs.setPosition(new Vector(x, y));
              newStairs.turnToDirection(tmpDir);
							spawnComingDownTile=new Vector(x+1, y+1);
							isStairsUpPlaced=true;
						}

						break;
				}
			}
		}

	}

	//----------

	public boolean getIsBlock( Vector newTile, int[][] newMapData ) {
		int x=(int) newTile.x;
		int y=(int) newTile.y;
		boolean r;
		boolean l;
		boolean t;
		boolean b;
		boolean rt;
		boolean rb;
		boolean lt;
		boolean lb;
		boolean rMapEnd=false;
		boolean lMapEnd=false;
		boolean tMapEnd=false;
		boolean bMapEnd=false;

		//assert tile is valid
		if (!isTileOnMap(newTile)) return false;
		if (!(newMapData[y][x]==RoomData.TILE_WALL)) return false;

		//determine mapends
		if( x+1>gridSize.x-1 ) rMapEnd=true;
		if( y+1>gridSize.y-1 ) bMapEnd=true;
		if( x-1<0 ) lMapEnd=true;
		if( y-1<0 )	tMapEnd=true;
		if (lMapEnd || rMapEnd || tMapEnd || bMapEnd) return true;

		//determine if the 8 neighbor tiles are walls
		r  = newMapData[y][x+1]==RoomData.TILE_WALL ? true : false;
		l  = newMapData[y][x-1]==RoomData.TILE_WALL ? true : false;
		t  = newMapData[y-1][x]==RoomData.TILE_WALL ? true : false;
		b  = newMapData[y+1][x]==RoomData.TILE_WALL ? true : false;
		lt = newMapData[y-1][x-1]==RoomData.TILE_WALL ? true : false;
		lb = newMapData[y+1][x-1]==RoomData.TILE_WALL ? true : false;
		rt = newMapData[y-1][x+1]==RoomData.TILE_WALL ? true : false;
		rb = newMapData[y+1][x+1]==RoomData.TILE_WALL ? true : false;

		int numTrue1 = (r?1:0)+(l?1:0)+(t?1:0)+(b?1:0);
		int numTrue2 = (rt?1:0)+(lt?1:0)+(rt?1:0)+(lb?1:0);
		if (false ||
		   (t && rt && r) ||
		   (r && rb && b) ||
		   (b && lb && l) ||
		   (l && lt && t) ||
		   (numTrue1>2 && numTrue2>0)) { //tripod with filled corner

			return true;
		} else {
			return false;
		}
	}

	//----------

	public void initMapObjects() {
    for( int x = 0; x < gridSize.x; x++ ) {
      for( int y = 0; y < gridSize.y; y++ ) {
        if( grid[y][x].tileType == RoomData.TILE_FLOOR ) {
          if (Math.random()<0.01) {
            addMonsterGroup( EnemyData.getRandomEnemyClass(), new Vector(x, y), (int)Util.random(0, 3) );
          } else if (Math.random()<0.03) { //create chests
            AbstractChest newChest = ChestData.createChest(mapStyle.getChestClass());
            newChest.setPosition(new Vector(x, y));
          } else if (Math.random()<0.03) { //create barrels
            BreakableBarrel newBarrel = new BreakableBarrel();
            newBarrel.setPosition(new Vector(x, y));
          } else if (Math.random()<0.03) { //create vases
            BreakableVase newVase = new BreakableVase();
            newVase.setPosition(new Vector(x, y));
          }
        }
      }
    }

    SmithyVendor newSmithyVendor = new SmithyVendor();
    newSmithyVendor.setPosition(new Vector(2, 5));
	}

	public void addMonsterGroup( Class<? extends AbstractEnemy> clazz, Vector newPos, int count ) {
		//place army
		int tmpEneCount = 3;
		for( int i = 0; i <= tmpEneCount-1; i++ ) {
			Vector tmpTile = getNextFreeTile( newPos, false, true, false, true );
			if( tmpTile!=null ) {
				AbstractEnemy tmpEnemy = EnemyData.createEnemy(clazz);
				tmpEnemy.setPosition(tmpTile.copy());
				if (!(tmpEnemy instanceof EnemySkeleton)) {
				  tmpEnemy.setImgScale(new Vector(2)); //TODO enemy images
	      }
			}
		}
	}

	//----------

	public Vector getNextFreeTile( Vector newPos, boolean checkForPlayer, boolean checkForEnemy, boolean checkForItem, boolean checkForBreakable ) {
		Vector newTile = newPos.copy().roundAllInst();
		if( isTileFree(newTile, checkForPlayer, checkForEnemy, checkForItem, checkForBreakable ) ) {
			return newTile;
		} else {
			for( int y = -1; y <= 1; y++ ) {
				for( int x = -1; x <= 1; x++ ) {
					Vector tmpPos = new Vector( newTile.x+x, newTile.y+y );
					if( grid[(int) tmpPos.y][(int) tmpPos.x].tileType!=RoomData.TILE_WALL ) {
						if (isTileFree(tmpPos, checkForPlayer, checkForEnemy, checkForItem, checkForBreakable) ) return tmpPos;
					}
				}
			}
		}

		return null;
	}

	//----------

	public boolean isTileFree( Vector newTile, boolean checkForPlayer, boolean checkForEnemy, boolean checkForItem, boolean checkForBreakable ) {
	  LinkedList<RpgMapObj> mosOnTile = new LinkedList<RpgMapObj>();
		for( RpgMapObj tmpMo : grid[(int) newTile.y][(int) newTile.x].collList ) {
		  if (!tmpMo.pos.copy().roundAllInst().isAllEqual(newTile)) continue;
		  mosOnTile.add(tmpMo);
		}
		mosOnTile = RpgMapObj.filterList(mosOnTile, checkForPlayer, checkForEnemy, checkForItem, false, true, checkForBreakable);
		return mosOnTile==null ? true : mosOnTile.isEmpty();
	}

	//----------

	public boolean isTileOnMap( Vector newTile ) {
		if (newTile.isAllLOE(new Vector())
				&& newTile.isAllSOE(gridSize.copy().subInst(new Vector(1)))) {
			return true;
		} else {
			return false;
		}
	}

	//----------

	public void updateOccTiles( RpgMapObj mo ) {
		//clear old area
		if( mo.oldOccTiles!=null ) {
			for( Vector currTile : mo.oldOccTiles ) {
				grid[(int) currTile.y][(int) currTile.x].collList.remove(mo);

//				if( isTileFree(currTile.copy(), true, true, true) ) { //TODO astar
//					aStarMap.setvalue(currTile.x, currTile.y, 0, 0);
//				}
			}
		}

		//write new area()
		if( mo.currOccTiles!=null && mo.isCollObj ) {
			for( Vector currTile : mo.currOccTiles ) {
				grid[(int) currTile.y][(int) currTile.x].collList.addLast(mo);
			}
//			aStarMap.setvalue(Math.round(mo.pos.x), Math.round(mo.pos.y), 0, 1);
		}

	}

	//----------

	public LinkedList<RpgMapObj> getRectColls( Vector newRectPos, Vector newRectSize ) {
		LinkedList<RpgMapObj> result = new LinkedList<RpgMapObj>();

		for( Vector tmpTile : RpgMapObj.getOccTilesByRect(newRectPos, newRectSize) ) {
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
		colls = RpgMapObj.filterList( colls, false, false, false, false, true, false );
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

		//DebugLog " from" + tile1.toString() + "  to" + tile2.toString() + " corner" + target.toString()
		//check path
		Vector tmpPos = tile1.copy();
		deltaDir = Vector.sub( target, tmpPos ).normalizeInst().mulScalarInst(0.5f);
		while( true ) {
			tmpPos.addInst(deltaDir);
			//DebugLog " step" + tmpPos.toString()
			Vector tmpTile = tmpPos.copy().roundAllInst();
			if( tmpTile.isAllEqual(tile2) ) {
				//DebugLog "true!"
				return true;
			}

			if (getIsRectCollidingWithMap( tmpPos, new Vector(0.01f) )) return false;
//			For Local MapObj tmpMo=EachIn collGrid[tmpTile.y][tmpTile.x]
//				If tmpMo.ownedByID=GameMap.TypeId Then
//					If tmpMo.Pos.copy().IsAllEqual(tmpTile) Then
//						'DebugLog "false"
//						Return False
//					EndIf
//				EndIf
//			Next
		}
	}

	//----------

}
