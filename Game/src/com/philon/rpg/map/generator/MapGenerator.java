package com.philon.rpg.map.generator;
import java.util.LinkedList;

import com.philon.engine.util.Vector;

public class MapGenerator {
	public int gMap[][];
	public Vector mapSize;
	public LinkedList<EntryPoint> openEntryPoints=new LinkedList<EntryPoint>();
	public LinkedList<Vector> stairPoints=new LinkedList<Vector>();
	public LinkedList<Integer> testedRooms;
	public LinkedList<EntryPoint> testedEntryPoints;
	public EntryPoint currEntryPoint;

	//----------

	public MapGenerator() {
	}

	//----------

	public int[][] generateMap( Vector newSize ) {
		RoomData.loadData();

		mapSize = newSize.copy();
		gMap = new int[(int) mapSize.y][];

		//init array
		for( int y = 0; y <= mapSize.y-1; y++ ) {
			gMap[y] = new int[(int) mapSize.x];
		}

		//set start-EP
		currEntryPoint=new EntryPoint( new Vector(5, 0), new Vector(0, 1), 2 );
		attemptInsertRoom(0);
		openEntryPoints.addLast(currEntryPoint);

		//attach rooms one by one
		boolean isFinished=false;
		while( !isFinished ) {
			testedEntryPoints = new LinkedList<EntryPoint>();
			boolean roomInserted=false;
			while( !openEntryPoints.isEmpty() ) {
				//get random untested entryPoint
				currEntryPoint=null;
				while( currEntryPoint==null ) {
					currEntryPoint = (EntryPoint)openEntryPoints.get( (int) (Math.random()*(openEntryPoints.size()-1)) );
					if( testedEntryPoints.contains(currEntryPoint) ) {
						currEntryPoint=null;
					}
				}
				testedEntryPoints.addLast( currEntryPoint );

				//test for each room
				LinkedList<Integer>testedRooms = new LinkedList<Integer>();
				testedRooms.addLast( (Integer)currEntryPoint.roomID ); //prohibit equal rooms from connecting
				while( testedRooms.size()<RoomData.numRooms ) {
					roomInserted=false;

					//get random untested room
					int newRoom=-1;
					while( newRoom == -1 ) {
						newRoom = (int)(Math.random()*(RoomData.numRooms));
						for( Integer i : testedRooms ) {
							if (i.intValue()==newRoom) { //room has been tested
								newRoom=-1;
							}
						}
					}
					testedRooms.addLast( (Integer)newRoom );

					//attempt to insert room
					if (attemptInsertRoom(newRoom)) roomInserted=true;
					//DebugLog "testing room " + newRoom + " at EP " + currEntryPoint.pos.toString() + "  succsess" + roomInserted
					if (roomInserted) break;
				} //until all rooms tested

				if( roomInserted ) {
					break;
				} else { //EP cannot be used
					openEntryPoints.remove(currEntryPoint);
					gMap[(int) currEntryPoint.pos.y][(int)currEntryPoint.pos.x] = RoomData.TILE_WALL;
					if (!(currEntryPoint.pos.x==0 ||
						currEntryPoint.pos.x==gMap[0].length-1 ||
						currEntryPoint.pos.y==0 ||
						currEntryPoint.pos.y==gMap.length-1)) {

						if ((gMap[(int) (currEntryPoint.pos.y-1)][(int) currEntryPoint.pos.x] == RoomData.TILE_FLOOR &&
							gMap[(int) (currEntryPoint.pos.y+1)][(int) currEntryPoint.pos.x] == RoomData.TILE_FLOOR) ||
						   (gMap[(int) currEntryPoint.pos.y][(int) (currEntryPoint.pos.x-1)] == RoomData.TILE_FLOOR &&
							gMap[(int) currEntryPoint.pos.y][(int) (currEntryPoint.pos.x+1)] == RoomData.TILE_FLOOR)) {

							gMap[(int) currEntryPoint.pos.y][(int) currEntryPoint.pos.x] = RoomData.TILE_DOOR;
						}
					}

				}

			}  //until all entryPoints tested

			if (!roomInserted) isFinished=true;
		}

		//fill empty space and entry points
		for( int x = 0; x <= mapSize.x-1; x++ ) {
			for( int y = 0; y <= mapSize.y-1; y++ ) {
				if( gMap[y][x]==RoomData.TILE_EMPTY ) {
					gMap[y][x]=RoomData.TILE_WALL;
				}
			}
		}
		
		//determine two random stairpoints
		Vector sp1;
		Vector sp2;
		sp1 = stairPoints.get((int) (Math.random()*(stairPoints.size()-1)));
		sp2=sp1;
		do {
			sp2 = stairPoints.get((int) (Math.random()*(stairPoints.size()-1)));
		} while( sp2.isAllEqual(sp1) );
		
		//fill unused stairPoints
		for( Vector tmpTile : stairPoints ) {
			if( !( tmpTile.isAllEqual(sp1) || tmpTile.isAllEqual(sp2) ) ) {
				gMap[(int) tmpTile.y][(int) tmpTile.x]=RoomData.TILE_FLOOR;
			}
		}
		return gMap;
	}

	//----------

	public boolean attemptInsertRoom( int newRoom ) { //attempts to insert a room at currEntryPoint
		for( EntryPoint currRoomEntryPoint : RoomData.roomEntryPoints[newRoom] ) {
			if (currRoomEntryPoint.normal.isAllEqual( currEntryPoint.normal.copy().mulScalarInst(-1) )) { //entrypoints facing each other
				Vector roomPos = Vector.sub( currEntryPoint.pos, currRoomEntryPoint.pos );
				if( isRectFreeForRoom( roomPos, newRoom ) ) {
					drawRoomToMap( newRoom, roomPos );
					for( EntryPoint tmpEP : RoomData.roomEntryPoints[newRoom] ) {
					  Vector tmpPos = Vector.add(roomPos, tmpEP.pos);
						if( !(tmpEP.equals(currRoomEntryPoint)) ) {
							openEntryPoints.addLast( new EntryPoint( tmpPos, tmpEP.normal.copy(), tmpEP.roomID) );
							//DebugLog "inserted  EP" + EntryPoint(openEntryPoints.Last()).pos.toString()
						} else if (tmpPos.x==0 || tmpPos.x==gMap.length-1 || tmpPos.y==0 || tmpPos.y==gMap[0].length) {
						  gMap[(int)tmpPos.y][(int)tmpPos.x] = RoomData.TILE_WALL;
						} else if( Math.random()>0.5 ) {
						  gMap[(int)tmpPos.y][(int)tmpPos.x] = RoomData.TILE_DOOR;
						} else {
						  gMap[(int)tmpPos.y][(int)tmpPos.x] = RoomData.TILE_FLOOR;
						}
					}
					openEntryPoints.remove(currEntryPoint);

					return true;
				}
			}
		}

		return false;
	}

	//----------

	public boolean isRectFreeForRoom( Vector newPos, int newRoom ) {
		Vector newSize=RoomData.roomSize[newRoom];
		
		for( int x = 0; x <= newSize.x-1; x++ ) {
			for( int y = 0; y <= newSize.y-1; y++ ) {
				Vector currRoomTile = new Vector(x, y);
				Vector tmpPos = Vector.add( newPos, currRoomTile );
				if (tmpPos.x<0 || tmpPos.y<0 || tmpPos.x>gMap[0].length-1 || tmpPos.y>gMap.length-1) return false;

				int roomTileData = RoomData.getData( newRoom, currRoomTile );
				if (roomTileData==RoomData.TILE_EMPTY) continue;
				int mapTileData = gMap[(int) tmpPos.y][(int) tmpPos.x];
				if (mapTileData==RoomData.TILE_FLOOR) return false;

			}
		}
		return true;
	}

	//----------

	public boolean drawRoomToMap( int newRoom, Vector newPos ) {
		Vector newRoomSize = RoomData.roomSize[newRoom];
		
		for( int x = 0; x <= newRoomSize.x-1; x++ ) {
			for( int y = 0; y <= newRoomSize.y-1; y++ ) {
				Vector currRoomTile = new Vector(x, y);
				Vector tmpPos = Vector.add( newPos, currRoomTile );
				int tmpVal = RoomData.getData(newRoom, currRoomTile);

				switch( tmpVal ) {
					case RoomData.TILE_EMPTY:
						break;
					case RoomData.TILE_ENTRY:
//						if( Math.random()>0.5 ) {
//							tmpVal=RoomData.TILE_DOOR; //randomly create doors
//						} else {
//							tmpVal=RoomData.TILE_FLOOR;
//						}
						break;
						
					case RoomData.TILE_STAIRS:
						stairPoints.addLast(tmpPos.copy());
						break;
				}

				gMap[(int) tmpPos.y][(int) tmpPos.x] = tmpVal;
			}
		}
		return true;
	}

	//----------

}




















