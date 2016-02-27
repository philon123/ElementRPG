package com.philon.rpg.util;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.philon.engine.PhilonGame;
import com.philon.engine.util.Path;
import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;
import com.philon.rpg.forms.MapScreen;
import com.philon.rpg.map.mo.BreakableMapObj;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.map.mo.StaticMapObj;
import com.philon.rpg.mos.chest.AbstractChest;
import com.philon.rpg.mos.door.AbstractDoor;
import com.philon.rpg.mos.enemy.AbstractEnemy;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.light.Firestand;
import com.philon.rpg.mos.player.AbstractChar;
import com.philon.rpg.mos.shot.AbstractShot;
import com.philon.rpg.mos.stairs.StairsDown;
import com.philon.rpg.mos.stairs.StairsUp;
import com.philon.rpg.mos.wall.AbstractWall;

public class RpgUtil {
  private static RpgUtil inst;

  private MapScreen screen;
	public static float sqrMap[];
	public static float sinMap[];
	public static float cosMap[];

  public static BitmapFont font;

	public RpgUtil(MapScreen newScreen) {
	  screen = newScreen;
	  inst = this;

		//init sqrMap
		sqrMap=new float[100000];
		for( int i = 0; i < sqrMap.length; i++ ) {
			sqrMap[i] = (float) Math.sqrt(i);
		}

		//init sinMap, cosMap
		sinMap = new float[360];
		cosMap = new float[360];
		for( int i = 0; i <= 359; i++ ) {
			sinMap[i] = (float) Math.sin(i);
			cosMap[i] = (float) Math.cos(i);
		}

		//font
    font = new BitmapFont(Gdx.files.internal("assets/" + "data/Media/font/arial64.fnt"), false);
    font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
    font.setColor(Color.BLACK);
    float targetYSize = 0.025f * PhilonGame.inst.screenPixSize.y;
    font.setScale(targetYSize / font.getCapHeight());
	}

	public static Vector getBaseScreenPosByTilePos( Vector newTilePos ) { //does not include offset (.subInst(currOffset)), allows baking static positions
//    Matrix4 matrixRotate = new Matrix4().setToRotation(0, 0, 1, 45);
//    Matrix4 matrixScale = new Matrix4().setToScaling(inst.screen.tileScreenSize.x, inst.screen.tileScreenSize.y, 1);
//    Matrix4 matrixTranslate = new Matrix4().setToTranslation(inst.screen.currOffset.x, inst.screen.currOffset.x, 0);
//
//    Vector3 result = new Vector3(newTilePos.x, newTilePos.y, 0).mul(matrixRotate).mul(matrixScale);
//    return new Vector(result.x, result.y);

	  return Vector.rotateDeg(newTilePos, 45).divScalarInst(1.4142f).mulInst(inst.screen.tileScreenSize);
  }

  public static Vector getScreenPosByTilePos( Vector newTilePos ) {
    return getBaseScreenPosByTilePos(newTilePos).subInst(inst.screen.currOffset);
  }

	public static Vector getTilePosByBaseScreenPos( Vector newScreenPos ) { //does not include offset
	  return Vector.div(newScreenPos, inst.screen.tileScreenSize).mulScalarInst(1.4142f).rotateDegInst(-45);
	}

  public static Vector getTilePosByScreenPos( Vector newScreenPos ) {
    Vector result = getTilePosByBaseScreenPos(Vector.add(newScreenPos, inst.screen.currOffset));
    if (result.isEitherSmaller(new Vector()) || result.isEitherLarger(Vector.sub(inst.screen.gMap.gridSize, new Vector(1)))) return null;
    return result;
  }

  public static Vector getImageScreenSize( float imgXYRatio ) {
    float newXSize = inst.screen.tileScreenSize.x;
    return new Vector(newXSize, newXSize/imgXYRatio);
  }

  public static Vector getScreenOffsetForImageSize(Vector newImgSize) {
    Vector tmpOffset = Vector.mul( newImgSize, new Vector(-0.5f, -1f) );
    tmpOffset.y += inst.screen.tileScreenSize.y / 2f;
    return tmpOffset;
  }

  public static void insertMapObj( RpgMapObj obj ) {
    inst.screen.gMap.insertMapObj(obj);
  }

  public static void removeMapObj( RpgMapObj obj ) {
    inst.screen.gMap.removeMapObj(obj);
  }

  public static void cleanMapObj( RpgMapObj obj ) {
    inst.screen.gMap.cleanMapObj(obj);
  }

	public static int getDir( Vector newV ) { //returns 0-7, zero is 'down', 45degree increments, clockwise
		if (newV.isAllEqual(new Vector())) return 0;
		float rot = (newV.getRotationDegInst()+45) % 360;
//		System.out.println(newV.getRotationDegInst());
		return(( (int)( rot/45.0 + 0.5 ) - 2 + 8 ) % 8);
	}

	public static int getOrientation(int newDir) { //returns 0 for facing up/down, 1 for facing left/right. favores 0
    if (newDir==0) return 0;
    if (newDir==1) return 0;
    if (newDir==2) return 0;
    if (newDir==3) return 1;
    if (newDir==4) return 1;
    if (newDir==5) return 0;
    if (newDir==6) return 0;
    if (newDir==7) return 0;
    return 0;
  }

	public static boolean isPixelInMapObj( RpgMapObj newMo, Vector newPixel ) {
	  return true; //TODO isPixelInMapObj
//		if (newMo.baseImgPixPos==null || newMo.image==0 || newMo.baseImgPixSize==null) return false;
//
//		Vector screenPixel = newPixel.copy().subInst( newMo.baseImgPixPos.copy().addInst(Game.inst.gGraphics.currOffset) );
//		if (screenPixel.isEitherSmaller(new Vector(0))) return false;
//		if (screenPixel.isEitherLarger( newMo.baseImgPixSize)) return false;
//
//		//pixel is in image rect
//		TextureRegion currImg = ImageData.images[newMo.image].frames[newMo.currFrame];
//		Vector imagePixel = Vector.div( screenPixel, newMo.baseImgPixSize ).mulInst( new Vector(currImg.getRegionWidth(), currImg.getRegionHeight()) );
//		if (argb( currImg.readPixel((int)imagePixel.x, (int)imagePixel.y) )[0] != 0) return true;
//		return false;
	}

	public static float getTileDistance(Vector tile1, Vector tile2) {
	  int sqrDist = (int) (Math.pow(tile2.x-tile1.x, 2) + Math.pow(tile2.y-tile1.y, 2));
    return sqrMap[sqrDist];
	}

  public static LinkedList<RpgMapObj> filterList( LinkedList<RpgMapObj> moList,
  		boolean keepPlayer,
  		boolean keepEnemy,
  		boolean keepItem,
  		boolean keepShot,
  		boolean keepChest,
  		boolean keepBreakable) {
  	LinkedList<RpgMapObj> result=new LinkedList<RpgMapObj>();

  	for( RpgMapObj tmpMo : moList ) {
  	  if (tmpMo instanceof AbstractWall ||
  	      tmpMo instanceof AbstractDoor ||
  	      tmpMo instanceof StairsUp ||
  	      tmpMo instanceof StairsDown ||
  	      tmpMo instanceof Firestand ||
  	      (tmpMo instanceof AbstractChar && keepPlayer) ||
          (tmpMo instanceof AbstractEnemy && keepEnemy) ||
          (tmpMo instanceof AbstractItem && keepItem) ||
          (tmpMo instanceof AbstractShot && keepShot) ||
          (tmpMo instanceof AbstractChest && keepChest) ||
          (tmpMo instanceof BreakableMapObj && keepBreakable) ) {
  	    result.addLast(tmpMo);
  	  }
  	}

  	return result;
  }

  public static Vector getNextFreeTile( Vector newPos, boolean checkForPlayer, boolean checkForEnemy, boolean checkForItem, boolean checkForBreakable ) {
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

  public static boolean isTileFree( Vector newTile, boolean checkForPlayer, boolean checkForEnemy, boolean checkForItem, boolean checkForBreakable ) {
    LinkedList<RpgMapObj> mosOnTile = getRectColls( newTile, new Vector(1) );
    mosOnTile = RpgUtil.filterList(mosOnTile, checkForPlayer, checkForEnemy, checkForItem, false, true, checkForBreakable);
    return mosOnTile.isEmpty();
  }

  public static boolean isTileOnMap( Vector newTile ) {
    if(newTile==null) return false;
    if ( newTile.isAllLOE(new Vector())
        && newTile.isAllSmaller(inst.screen.gMap.gridSize)) {
      return true;
    } else {
      return false;
    }
  }

  public static LinkedList<RpgMapObj> getRectColls( Vector newRectPos, Vector newRectSize ) {
    LinkedList<RpgMapObj> result = new LinkedList<RpgMapObj>();

    for( Vector tmpTile : getOccTilesByRect(newRectPos, newRectSize, inst.screen.gMap.gridSize) ) {
      for( RpgMapObj tmpMo : inst.screen.gMap.grid[(int) tmpTile.y][(int) tmpTile.x].collList ) {
        if( tmpMo.isCollObj ) {
          if( !result.contains(tmpMo) ) {
            if( Util.rectsColliding( tmpMo.pos, tmpMo.collRect, newRectPos, newRectSize )!=null ) {
              result.addLast(tmpMo);
            }
          }
        }
      }
    }

    return result;
  }

  public static boolean getIsRectCollidingWithMap( Vector newPos, Vector newSize ) {
    List<?> colls = getRectColls( newPos, newSize );
    colls = Util.filterListByKeepClass(colls, StaticMapObj.class, AbstractWall.class);
    return !colls.isEmpty();
  }

  public static boolean tilesInSight( Vector tile1, Vector tile2 ) {
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

  public static RpgMapObj selectObjectByTilePos(Vector newTilePos, Vector tolerance, RpgMapObj... objToIgnore ) {
    if( !isTileOnMap(newTilePos) ) return null;

    LinkedList<RpgMapObj> selectedList = new LinkedList<RpgMapObj>();

    for(Vector currTile : getOccTilesByRect(Vector.sub(newTilePos, tolerance), Vector.mulScalar(tolerance, 2), inst.screen.gMap.gridSize)) {
      for(RpgMapObj currObj : inst.screen.gMap.grid[(int)currTile.y][(int)currTile.x].collList) {
        if(!currObj.isSelectable) continue;

        boolean ignore = false;
        for(RpgMapObj tmpIgnObj : objToIgnore) {
          if(tmpIgnObj == currObj) {
            ignore = true;
            break;
          }
        }
        if(ignore) continue;
        if(currObj instanceof AbstractEnemy) continue; //TODO selection of enemies

        selectedList.add(currObj);
      }
    }

    RpgMapObj bestObj = null;
    float bestManhattenDist = Float.MAX_VALUE;
    for(RpgMapObj currObj : selectedList) {
      Vector delta = Vector.absolute( Vector.sub(currObj.pos, newTilePos) );
      float deltaLen = delta.x+delta.y;
      if(deltaLen < bestManhattenDist) {
        bestManhattenDist = deltaLen;
        bestObj = currObj;
      }
    }

    return bestObj;
  }

  public static LinkedList<Vector> getOccTilesByRect( Vector newRectPos, Vector newRectSize, Vector newGridSize ) {
    LinkedList<Vector> result=new LinkedList<Vector>();

    Vector vmin = Vector.sub( newRectPos, newRectSize.copy().mulScalarInst(0.5f) ).roundAllInst();
    Vector vmax = Vector.add( newRectPos, newRectSize.copy().mulScalarInst(0.5f) ).subInst(new Vector(0.001f)).roundAllInst();
    if (vmin.x<0) vmin.x=0;
    if (vmin.y<0) vmin.y=0;
    if (vmax.x>newGridSize.x-1) vmax.x=newGridSize.x-1;
    if (vmax.y>newGridSize.y-1) vmax.y=newGridSize.y-1;

    for( int x = (int) vmin.x; x <= vmax.x; x++ ) {
      for( int y = (int) vmin.y; y <= vmax.y; y++ ) {
        result.addLast(new Vector(x, y));
      }
    }

    return result;
  }

  public static Path getAStarPath( Vector tile1, Vector tile2 ) { //TODO astar
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

  public static boolean isTileOnScreen(Vector newTile) { //TODO cut corners
    if (newTile.isAllLOE(inst.screen.minTileOnScreen) && newTile.isAllSOE(inst.screen.maxTileOnScreen)) {
      return true;
    } else {
      return false;
    }
  }

  public static void changeLevel( int delta ) {
    inst.screen.changeLevel(inst.screen.currLevel + delta);
  }
}
