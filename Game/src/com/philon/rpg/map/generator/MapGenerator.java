package com.philon.rpg.map.generator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;
import com.philon.rpg.map.RpgMapSaveData;
import com.philon.rpg.map.mo.RpgMapObj.RpgMapObjSaveData;
import com.philon.rpg.map.mo.ToggleMapObj.StateClosed;
import com.philon.rpg.map.mo.ToggleMapObj.StateOpen;
import com.philon.rpg.map.mo.ToggleMapObj.ToggleMOSaveData;
import com.philon.rpg.mos.SmithyVendor;
import com.philon.rpg.mos.enemy.AbstractEnemy;
import com.philon.rpg.mos.wall.AbstractMapStyle;
import com.philon.rpg.mos.wall.AbstractWall.WallSaveData;

public class MapGenerator {
  public static final int WALLTYPE_PILLAR   = 0;
  public static final int WALLTYPE_STRAIGHT = 1;
  public static final int WALLTYPE_STUB     = 2;
  public static final int WALLTYPE_CORNER   = 3;
  public static final int WALLTYPE_TRIPOD   = 4;
  public static final int WALLTYPE_CROSS    = 5;

  private RoomData roomData;

	public Vector mapSize;
	public int[][] layout;
	public AbstractMapStyle mapStyle;

	public MapGenerator() {
	}

	public RpgMapSaveData generateRpgMap(Vector newSize, AbstractMapStyle newMapStyle) {
	  roomData = newMapStyle.getRoomData();

	  mapSize = newSize.copy();
	  layout = generateMapLayout(mapSize);
	  mapStyle = newMapStyle;

	  RpgMapSaveData result = new RpgMapSaveData();
	  result.mapStyle = newMapStyle;
	  result.mapSize = mapSize.copy();
	  result.mos = createMapObjSaveData(layout);

	  return result;
	}

	public RpgMapObjSaveData[] createMapObjSaveData( int newMapData[][]) {
	  LinkedList<RpgMapObjSaveData> result = new LinkedList<RpgMapObjSaveData>();

    boolean isStairsUpPlaced = false;
    boolean isStairsDownPlaced = false;
    boolean isVendorPlaced = false;

    for( int y = 0; y < mapSize.y; y++ ) {
      for( int x = 0; x < mapSize.x; x++ ) {
        Vector currTile = new Vector(x, y);

        boolean r;
        boolean l;
        boolean t;
        boolean b;

        //get connections
        r = (currTile.x+1>mapSize.x-1) ? false :
          (newMapData[(int) currTile.y][(int) (currTile.x+1)]==RoomData.TILE_WALL);
        b = (currTile.y+1>mapSize.y-1) ? false :
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

            Object[] wallTypeAndDirection = getWallTypeAndDirection(isBlock, l, r, t, b);
            result.add( new WallSaveData(mapStyle.getWallClass(), currTile,
                (Vector)wallTypeAndDirection[1], isBlock, (Integer)wallTypeAndDirection[0]) );

            break;
          case RoomData.TILE_LIGHT:
            if ( getIsBlock(new Vector(x-1, y), newMapData) ) { //create torch
              result.add(new RpgMapObjSaveData(mapStyle.getTorchClass(), new Vector(x-1, y), new Vector(1, 0)));
            } else if( getIsBlock(new Vector(x, y-1), newMapData) ) {
              result.add(new RpgMapObjSaveData(mapStyle.getTorchClass(), new Vector(x, y-1), new Vector(0, 1)));
            } else { //create firestand
              result.add( new RpgMapObjSaveData(mapStyle.getFirestandClass(), currTile,
                  Math.random()>0.5 ? new Vector(0, 1) : new Vector(1, 0)) );
            }

            break;
          case RoomData.TILE_DOOR:
            Vector leftOrDown = null;
            if (l && r) leftOrDown = new Vector(0, 1);
            if (t && b) leftOrDown = new Vector(1, 0);

            if( leftOrDown!=null ) {
              result.add(new ToggleMOSaveData(mapStyle.getDoorClass(), currTile, leftOrDown,
                  Math.random()<0.3 ? StateOpen.class : StateClosed.class, false));
            }

            break;
          case RoomData.TILE_STAIRS:
            Vector tmpDir = (Math.random()>0.5) ? new Vector(1, 0) : new Vector(0, 1);

            boolean placeSD;
            if( !isStairsDownPlaced && !isStairsUpPlaced ) {
              placeSD = Math.random()<0.5 ? false : true;
            } else {
              placeSD = !isStairsDownPlaced;
            }
            if( placeSD ) {
              result.add(new RpgMapObjSaveData(mapStyle.getStairsDownClass(), currTile, tmpDir));
              isStairsDownPlaced = true;
            } else {
              result.add(new RpgMapObjSaveData(mapStyle.getStairsUpClass(), currTile, tmpDir));
              isStairsUpPlaced = true;
            }

            break;
          case RoomData.TILE_FLOOR:
            //debug: create vendor
            if(!isVendorPlaced) {
              result.add(new RpgMapObjSaveData(SmithyVendor.class, new Vector(2, 5), new Vector(1, 0)));
              isVendorPlaced = true;
            }

            if (Math.random()<0.03) {
              result.addAll( addMonsterGroup( mapStyle.getRandomEnemyClass(), new Vector(x, y), new Vector(Util.rand(1, 2), Util.rand(1, 2)) ) );
            } else if (Math.random()<0.02) { //create chests
              result.add(new ToggleMOSaveData(mapStyle.getChestClass(), currTile, Math.random()<0.5 ? new Vector(1, 0) : new Vector(0, 1),
                  Math.random()<0.9 ? StateClosed.class : StateOpen.class, false));
            } else if (Math.random()<0.02) { //create barrels
              result.add(new ToggleMOSaveData(mapStyle.getBarrelClass(), currTile, Math.random()<0.5 ? new Vector(1, 0) : new Vector(0, 1),
                  Math.random()<0.9 ? StateClosed.class : StateOpen.class, false));
            } else if (Math.random()<0.02) { //create vases
              result.add(new ToggleMOSaveData(mapStyle.getVaseClass(), currTile, Math.random()<0.5 ? new Vector(1, 0) : new Vector(0, 1),
                  Math.random()<0.9 ? StateClosed.class : StateOpen.class, false));
            }

            break;
        }
      }
    }

    //filter out mos placed on top of existing ones
    LinkedList<RpgMapObjSaveData> filteredResult = new LinkedList<RpgMapObjSaveData>();
    HashSet<Vector> tileToMOMap = new HashSet<Vector>();
    for(RpgMapObjSaveData obj : result) {
      if (!tileToMOMap.contains(obj.pos)) {
        tileToMOMap.add(obj.pos);
        filteredResult.add(obj);
      }
    }
    return filteredResult.toArray(new RpgMapObjSaveData[]{});
  }

  public LinkedList<RpgMapObjSaveData> addMonsterGroup( Class<? extends AbstractEnemy> clazz, Vector newPos, Vector count ) {
    LinkedList<RpgMapObjSaveData> result = new LinkedList<RpgMapObjSaveData>();

    for( int y = (int)newPos.y; y < newPos.y+count.y; y++ ) {
      for( int x = (int)newPos.x; x < newPos.x+count.x; x++ ) {
        if(layout[y][x]==RoomData.TILE_FLOOR) {
          result.add( new RpgMapObjSaveData(clazz, new Vector(x, y), new Vector(1, 0)) );
        }
      }
    }

    return result;
  }

  public boolean getIsBlock( Vector newTile, int[][] newMapData ) {
    Vector newMapSize = new Vector(newMapData[0].length, newMapData.length);
    int x = (int) newTile.x;
    int y = (int) newTile.y;

    //assert tile is valid
    if (newMapData[y][x]!=RoomData.TILE_WALL) return false;
    if ( newTile.isEitherSmaller(new Vector()) || newTile.isEitherLarger(new Vector(newMapSize.x-1, newMapSize.y-1)) ) return false;

    //borders are blocks
    if ( newTile.isEitherEqual(new Vector()) || newTile.isEitherEqual(new Vector(newMapSize.x-1, newMapSize.y-1)) ) return true;

    //determine if the 8 neighbor tiles are walls
    boolean l, r, t, b;
    boolean lt, lb, rt, rb;
    r  = newMapData[y][x+1]==RoomData.TILE_WALL;
    l  = newMapData[y][x-1]==RoomData.TILE_WALL;
    t  = newMapData[y-1][x]==RoomData.TILE_WALL;
    b  = newMapData[y+1][x]==RoomData.TILE_WALL;
    lt = newMapData[y-1][x-1]==RoomData.TILE_WALL;
    lb = newMapData[y+1][x-1]==RoomData.TILE_WALL;
    rt = newMapData[y-1][x+1]==RoomData.TILE_WALL;
    rb = newMapData[y+1][x+1]==RoomData.TILE_WALL;

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

  /**
   * @args
   *  - information about surrounding blocks
   * @returns
   *  - {(Integer) wallType, (Vector) direction}
   */
  public Object[] getWallTypeAndDirection(boolean isBlock, boolean l, boolean r, boolean t, boolean b) {
      int tmpWallType = -1;
      Vector tmpDir = null;

      if       (t==false && l==false && r==false && b==false) { //free
        tmpWallType = WALLTYPE_PILLAR;
        tmpDir = new Vector(0, 0);
      } else if(t==false && l==true && r==true && b==false) { // horizontal
        tmpWallType = WALLTYPE_STRAIGHT;
        tmpDir = new Vector(0, 1);
      } else if (t==true && l==false && r==false && b==true) { // vertical
        tmpWallType = WALLTYPE_STRAIGHT;
        tmpDir = new Vector(1, 0);
      } else if (t==true && l==false && r==false && b==false) { //stub down
        tmpWallType = WALLTYPE_STUB;
        tmpDir = new Vector(0, 1);
      } else if (t==false && l==true && r==false && b==false) { //stub right
        tmpWallType = WALLTYPE_STUB;
        tmpDir = new Vector(1, 0);
      } else if (t==false && l==false && r==true && b==false) { //stub left
        tmpWallType = WALLTYPE_STUB;
        tmpDir = new Vector(-1, 0);
      } else if (t==false && l==false && r==false && b==true) { //stub up
        tmpWallType = WALLTYPE_STUB;
        tmpDir = new Vector(0, -1);
      } else if (t==true && l==true && r==false && b==false) { // corner bottom right
        tmpWallType = WALLTYPE_CORNER;
        tmpDir = new Vector(0, 1);
      } else if (t==true && l==false && r==true && b==false) { // corner bottom left
        tmpWallType = WALLTYPE_CORNER;
        tmpDir = new Vector(-1, 0);
      } else if (t==false && l==true && r==false && b==true) { // corner top right
        tmpWallType = WALLTYPE_CORNER;
        tmpDir = new Vector(1, 0);
      } else if (t==false && l==false && r==true && b==true) { // corner top left
        tmpWallType = WALLTYPE_CORNER;
        tmpDir = new Vector(0, -1);
      } else if (t==true && l==true && r==true && b==false) { // horizontal +top
        tmpWallType = WALLTYPE_TRIPOD;
        tmpDir = new Vector(0, 1);
      } else if (t==false && l==true && r==true && b==true) { // horizontal +bot
        tmpWallType = WALLTYPE_TRIPOD;
        tmpDir = new Vector(0, -1);
      } else if (t==true && l==true && r==false && b==true) { // vertical +left
        tmpWallType = WALLTYPE_TRIPOD;
        tmpDir = new Vector(1, 0);
      } else if (t==true && l==false && r==true && b==true) { // vertical +right
        tmpWallType = WALLTYPE_TRIPOD;
        tmpDir = new Vector(-1, 0);
      } else if (t==true && l==true && r==true && b==true) { // enclosed
        tmpWallType = WALLTYPE_CROSS;
        tmpDir = new Vector(0, 1);
      }

      return new Object[]{tmpWallType, tmpDir};
    }


  //### LAYOUT #############################################################

	public int[][] generateMapLayout( Vector newSize ) {

		//init array
		int[][] result = new int[(int) newSize.y][];
		for( int y = 0; y <= newSize.y-1; y++ ) {
			result[y] = new int[(int) newSize.x];
		}

		//init open list
		LinkedList<EntryPoint> openEntryPoints = new LinkedList<EntryPoint>();

		//insert first room
		EntryPoint startPoint = new EntryPoint( new Vector(5, 0), new Vector(0, 1) );
		attemptInsertRoom(result, openEntryPoints, startPoint, 0);
		openEntryPoints.add(startPoint);

		//start insertion
		while( !openEntryPoints.isEmpty() ) {

			//get random untested entryPoint
		  EntryPoint currEntryPoint = openEntryPoints.get( (Util.rand(0, openEntryPoints.size()-1)) );
		  openEntryPoints.remove(currEntryPoint);

		  //build tmp list roomsForTest
		  ArrayList<Integer> roomsForTest = new ArrayList<Integer>();
		  for(int i=0; i<roomData.numRooms; i++) {
		    roomsForTest.add(i);
		  }

		  //try rooms in roomsForTest until match is found
		  boolean roomInserted=false;
		  while(!roomsForTest.isEmpty()) {
		    int tmpRoom = roomsForTest.get(Util.rand(0, roomsForTest.size()-1));
		    roomsForTest.remove((Object)tmpRoom); //make sure to remove by value

		    if (attemptInsertRoom(result, openEntryPoints, currEntryPoint, tmpRoom)) {
		      roomInserted=true;
		      break;
		    }
		  }

		  //if no room could be inserted, seal the gap
		  //or this might be a new doorway between 2 rooms just by chance!
		  if( !roomInserted ) {
		    int x = (int)currEntryPoint.pos.x;
		    int y = (int)currEntryPoint.pos.y;
		    if( !(currEntryPoint.pos.isEitherEqual(new Vector()) || currEntryPoint.pos.isEitherEqual(newSize.copy().subInst(new Vector(-1)))) ) {
          int l = result[y][x-1], r = result[y][x+1], t = result[y-1][x], b = result[y+1][x];
          if((
               l==RoomData.TILE_WALL &&
               r==RoomData.TILE_WALL &&
               t==RoomData.TILE_FLOOR &&
               b==RoomData.TILE_FLOOR
              ) || (
               t==RoomData.TILE_WALL &&
               b==RoomData.TILE_WALL &&
               l==RoomData.TILE_FLOOR &&
               r==RoomData.TILE_FLOOR
              )) {

            result[y][x] = RoomData.TILE_ENTRY;
          } else {
            result[y][x] = RoomData.TILE_WALL;
          }
		    } else {
          result[y][x] = RoomData.TILE_WALL;
        }
		  }

		} //until no open entrypoints left


		//convert TILE_EMPTYs to TILE_WALLs
		//convert TILE_ENTRYs to TILE_DOORs (50% chance)
		//save TILE_STAIRSs as stairPoint candidates
		LinkedList<Vector> stairPoints = new LinkedList<Vector>();
		for( int x=0; x < newSize.x; x++ ) {
			for( int y=0; y < newSize.y; y++ ) {
			  switch (result[y][x]) {
			    case RoomData.TILE_EMPTY :
			      result[y][x] = RoomData.TILE_WALL;

			      break;
			    case RoomData.TILE_ENTRY :
			      if (Math.random()<0.33f) {
  				    result[y][x] = RoomData.TILE_DOOR;
  				  } else {
  				    result[y][x] = RoomData.TILE_FLOOR;
  				  }

            break;
			    case RoomData.TILE_STAIRS :
			      stairPoints.addLast(new Vector(x, y));
			      result[y][x] = RoomData.TILE_FLOOR;

            break;
			  }
			}
		}

		//get two random stairpoints
		Vector sp1 = stairPoints.get(Util.rand(0, stairPoints.size()-1));
		stairPoints.remove(sp1);
		Vector sp2 = stairPoints.get(Util.rand(0, stairPoints.size()-1));
		stairPoints.remove(sp2);

		result[(int)sp1.y][(int)sp1.x] = RoomData.TILE_STAIRS;
		result[(int)sp2.y][(int)sp2.x] = RoomData.TILE_STAIRS;

		return result;
	}

	private boolean attemptInsertRoom( int[][] gMap, LinkedList<EntryPoint> openEntryPoints, EntryPoint targetEP, int newRoom ) { //attempts to insert a room at currEntryPoint
		for( EntryPoint currRoomEntryPoint : roomData.roomEntryPoints[newRoom] ) {
			if (currRoomEntryPoint.normal.isAllEqual( targetEP.normal.copy().mulScalarInst(-1) )) { //entrypoints facing each other
				Vector roomPos = Vector.sub( targetEP.pos, currRoomEntryPoint.pos );
				if( isSpaceForRoom( gMap, roomPos, newRoom ) ) {
					drawRoomToMap( gMap, newRoom, roomPos );
					for( EntryPoint tmpEP : roomData.roomEntryPoints[newRoom] ) {
					  Vector tmpPos = Vector.add(roomPos, tmpEP.pos);
						if ( tmpEP.equals(currRoomEntryPoint) ) continue;
					  if ( tmpPos.x==0 || tmpPos.x==gMap.length-1 || tmpPos.y==0 || tmpPos.y==gMap[0].length ) {
						  gMap[(int)tmpPos.y][(int)tmpPos.x] = RoomData.TILE_WALL;
						  continue;
						}
					  openEntryPoints.addLast( new EntryPoint(tmpPos, tmpEP.normal.copy()) );
					}

					return true;
				}
			}
		}

		return false;
	}

	private boolean isSpaceForRoom( int[][] gMap, Vector newPos, int newRoom ) {
		Vector newSize = roomData.roomSize[newRoom];
		if (newPos.isEitherSmaller(new Vector())) return false;
		Vector maxSize = Vector.add( newPos, newSize );
		if (maxSize.isEitherLarger(new Vector(gMap[0].length-1, gMap.length-1))) return false;

		for( int x = 0; x <= newSize.x-1; x++ ) {
			for( int y = 0; y <= newSize.y-1; y++ ) {
				Vector currRoomTile = new Vector(x, y);
				Vector tmpPos = Vector.add( newPos, currRoomTile );

				int roomTileData = roomData.getData( newRoom, currRoomTile );
				if (roomTileData==RoomData.TILE_EMPTY) continue;
				int mapTileData = gMap[(int) tmpPos.y][(int) tmpPos.x];
				if (mapTileData==RoomData.TILE_FLOOR) return false;
			}
		}
		return true;
	}

	private void drawRoomToMap( int[][] gMap, int newRoom, Vector newPos ) {
		Vector newRoomSize = roomData.roomSize[newRoom];

		for( int x = 0; x <= newRoomSize.x-1; x++ ) {
			for( int y = 0; y <= newRoomSize.y-1; y++ ) {
				Vector currRoomTile = new Vector(x, y);
				Vector tmpPos = Vector.add( newPos, currRoomTile );
				int tmpVal = roomData.getData(newRoom, currRoomTile);


				gMap[(int) tmpPos.y][(int) tmpPos.x] = tmpVal;
			}
		}
	}

}




















