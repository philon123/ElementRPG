package com.philon.rpg.map.generator;
import java.util.LinkedList;

import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;

public class RoomData {
	public static final int TILE_EMPTY  = 0;
	public static final int TILE_FLOOR  = 1;
	public static final int TILE_WALL   = 2;
	public static final int TILE_ENTRY  = 3;
	public static final int TILE_LIGHT  = 4;
	public static final int TILE_DOOR   = 5;
	public static final int TILE_STAIRS = 7;

	public int numRooms = 14;

	public int[][][] rooms;
	public Vector[] roomSize;
	public LinkedList<EntryPoint>[] roomEntryPoints;

	public RoomData() {
	  loadData();
	}

	@SuppressWarnings("unchecked")
  public void loadData() {
		rooms = new int[numRooms][][];
		roomSize = new Vector[numRooms];
		roomEntryPoints = new LinkedList[numRooms];

		//rooms
		generateRooms();

		Vector tmpNormal=new Vector();

		//roomSize
		for( int i = 0; i < numRooms; i++ ) {
			roomSize[i] = new Vector(rooms[i][0].length, rooms[i].length);
		}

		//entryPoints
		for( int i = 0; i < numRooms; i++ ) {
			roomEntryPoints[i] = new LinkedList<EntryPoint>();
			for( int x = 0; x < roomSize[i].x; x++ ) {
				for( int y = 0; y < roomSize[i].y; y++ ) {
					if( rooms[i][y][x]==TILE_ENTRY ) {
						if (x==0) tmpNormal = new Vector(-1, 0);
						if (y==0) tmpNormal = new Vector(0, -1);
						if (x==roomSize[i].x-1) tmpNormal = new Vector(1, 0);
						if (y==roomSize[i].y-1) tmpNormal = new Vector(0, 1);
						roomEntryPoints[i].addLast( new EntryPoint(new Vector(x, y), tmpNormal) );
					}
				}
			}
		}

	}

	public void generateRooms() {
		int currRoom = -1;

		currRoom += 1;
		rooms[currRoom] = new int[][] {
		{2, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2}, //11x11
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 4, 1, 2},
		{2, 1, 1, 7, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 7, 1, 1, 2},
		{2, 1, 4, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2}};

		currRoom += 1;
		rooms[currRoom] = new int[][]{
		{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}, //11x5
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 4, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 4, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}};

		currRoom += 1;
		rooms[currRoom] = new int[][]{
		{2, 2, 3, 2, 2}, //5x11
		{2, 1, 1, 1, 2},
		{2, 1, 1, 1, 2},
		{2, 4, 1, 4, 2},
		{2, 1, 1, 1, 2},
		{2, 1, 1, 1, 2},
		{2, 1, 1, 1, 2},
		{2, 1, 1, 1, 2},
		{2, 1, 1, 1, 2},
		{2, 1, 1, 1, 2},
		{2, 2, 3, 2, 2}};

		currRoom += 1;
		rooms[currRoom] = new int[][]{
		{2, 2, 2, 2, 2, 2, 2, 2, 3, 2, 2}, //bend lb
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{3, 1, 1, 1, 1, 1, 1, 1, 4, 1, 3},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 2},
		{0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2},
		{0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2},
		{0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2},
		{0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2},
		{0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2},
		{0, 0, 0, 0, 0, 0, 2, 2, 3, 2, 2}};
		currRoom += 1;
		rooms[currRoom] = Util.mirrorMatrix(rooms[currRoom-1],  new Vector(1, 0)); //bend lt
		currRoom += 1;
		rooms[currRoom] = Util.mirrorMatrix(rooms[currRoom-2],  new Vector(0, 1)); //bend rb
		currRoom += 1;
		rooms[currRoom] = Util.mirrorMatrix(rooms[currRoom-3],  new Vector(1, 1)); //bend rt

//		currRoom += 1
//		rooms[currRoom] = new int[][]{..
//		{0, 0, 0, 0, 0, 0, 2, 2, 3, 2, 2}, 'tripod l
//		{0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2},
//		{0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2},
//		{2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 2},
//		{2, 4, 1, 1, 1, 1, 1, 1, 1, 1, 2},
//		{3, 1, 1, 1, 1, 1, 1, 1, 1, 4, 2},
//		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
//		{2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 2},
//		{0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2},
//		{0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2},
//		{0, 0, 0, 0, 0, 0, 2, 2, 3, 2, 2}}
//		currRoom += 1
//		rooms[currRoom] = Util.mirrorMatrix(rooms{currRoom-1},  Tnew Vector(0, 1)) 'tripod r

		currRoom += 1;
		rooms[currRoom] = new int[][]{
		{0, 0, 0, 2, 2, 3, 2, 2, 0, 0, 0}, //tripod t
		{0, 0, 0, 2, 1, 1, 1, 2, 0, 0, 0},
		{0, 0, 0, 2, 1, 1, 1, 2, 0, 0, 0},
		{0, 0, 0, 2, 1, 1, 1, 2, 0, 0, 0},
		{0, 0, 0, 2, 1, 1, 1, 2, 0, 0, 0},
		{0, 0, 0, 2, 1, 1, 1, 2, 0, 0, 0},
		{2, 2, 2, 2, 1, 1, 1, 2, 2, 2, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
		{2, 1, 1, 1, 1, 4, 1, 1, 1, 1, 2},
		{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}};
		currRoom += 1;
		rooms[currRoom] = Util.mirrorMatrix(rooms[currRoom-1],  new Vector(1, 0));

		currRoom += 1;
		rooms[currRoom] = new int[][]{
		{2, 2, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}, //15x15
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 4, 1, 1, 1, 1, 1, 4, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 4, 1, 1, 1, 1, 1, 4, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 2, 2, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2, 2, 2}};
		currRoom += 1;
		rooms[currRoom] = Util.mirrorMatrix(rooms[currRoom-1], new Vector(1, 0));
		currRoom += 1;
		rooms[currRoom] = Util.mirrorMatrix(rooms[currRoom-2], new Vector(0, 1));

		currRoom += 1;
		rooms[currRoom] = new int[][]{
		{2, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2}, //11x31 hallway
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 2, 1, 1, 1, 2, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 4, 1, 1, 1, 1, 1, 1, 1, 4, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 2, 1, 1, 1, 2, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 2, 1, 1, 1, 2, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 4, 1, 1, 1, 1, 1, 1, 1, 4, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 2, 1, 1, 1, 2, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 2, 1, 1, 1, 2, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 4, 1, 1, 1, 1, 1, 1, 1, 4, 2},
		{2, 1, 1, 2, 1, 1, 1, 2, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2}};

		currRoom += 1;
		rooms[currRoom] = new int[][]{
		{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}, //11x31 hallway
		{2, 1, 1, 1, 1, 1, 1, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}};

	}

	public int getData( int roomID, Vector newPos ) {
		return rooms[roomID][(int) newPos.y][(int) newPos.x];
	}

}
