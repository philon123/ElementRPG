package com.philon.rpg;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.philon.engine.Game;
import com.philon.engine.Graphics;
import com.philon.engine.forms.AbstractForm;
import com.philon.engine.util.Vector;
import com.philon.rpg.mo.AbstractMapObj;
import com.philon.rpg.mo.BreakableMapObj;
import com.philon.rpg.mo.Selectable;
import com.philon.rpg.mo.BreakableMapObj.IntactState;
import com.philon.rpg.mos.door.AbstractDoor;
import com.philon.rpg.mos.wall.AbstractWall;
import com.philon.rpg.util.GameUtil;
import com.philon.rpg.util.RenderMapKey;

public class GameGraphics extends Graphics {
  public Vector squashFactor;
  public Vector tilePixSize;

  public Vector baseOffset;
  public Vector currOffset;
  public AbstractMapObj centeredMo;

  public HashSet<AbstractMapObj> seeThroughObjects=new HashSet<AbstractMapObj>();
  public HashSet<AbstractMapObj> staticSeeThroughObjects=new HashSet<AbstractMapObj>();

  public Vector minTileOnScreen=new Vector();
  public Vector maxTileOnScreen=new Vector();

  public LinkedList<AbstractMapObj> staticLightSources;
  public LinkedList<AbstractMapObj> dynamicLightSources;

  public TreeMap<RenderMapKey, AbstractMapObj> renderMap;
  
  public BitmapFont font;
  
  public GameGraphics() {
    super();
    
    font = new BitmapFont(Gdx.files.internal("assets/" + "data/Media/font/centaur25.fnt"), false);
    font.setColor(new Color(1, 1, 1, 1));
//    font.setScale(0.8f);
    
    renderMap = new TreeMap<RenderMapKey, AbstractMapObj>();

    dynamicLightSources = new LinkedList<AbstractMapObj>();
    staticLightSources  = new LinkedList<AbstractMapObj>();

    squashFactor = new Vector(1, 0.57f);
    tilePixSize = new Vector(64f);
    updateOffset();
  }
  
  public void updateOffset() {
    if( centeredMo!=null ) {
      Vector tmpOffset = getPixDirByTileDir(centeredMo.pos).mulScalarInst(-1);
      currOffset = baseOffset.copy().addInst(tmpOffset);
    } else {
      currOffset = baseOffset.copy();
    }
  }
  
  public void resizedTrigger(Vector newScreenSize) {
    super.resizedTrigger(newScreenSize);
    
    baseOffset = newScreenSize.copy().mulInst(new Vector(0.5f));
  }
  
  public Vector getTilePosByScreenPos( Vector newScreenPos ) {
    return newScreenPos.copy().subInst(currOffset).divInst(tilePixSize).divInst(squashFactor);
  }
  
  public Vector getBasePixPosByTilePos( Vector newTilePos ) { //does not include global offset, allows baking static positions
    return newTilePos.copy().rotateDegInst(45).mulInst(tilePixSize).mulInst(squashFactor);
  }
  
  public Vector getTilePosByPixPos( Vector newPixel ) {
    Vector result = newPixel.copy().subInst(currOffset).divInst(squashFactor).divInst(tilePixSize).rotateDegInst(-45);
    if (result.isEitherSmaller(new Vector()) || result.isEitherLarger(Vector.sub(RpgGame.inst.gMap.gridSize, new Vector(1)))) return null;
    return result;
  }

  //----------
  
  public Vector getPixSizeByTileSize( Vector newTileSize ) {
    return newTileSize.copy().mulInst(tilePixSize).mulScalarInst(1.4142f).mulInst(squashFactor);
  }

  //----------

  public Vector getPixDirByTileDir( Vector newTileDir ) {
    return newTileDir.copy().rotateDegInst(45).mulInst(tilePixSize).mulInst(squashFactor);
  }

  //----------

  public void insertStaticLightSource( AbstractMapObj mo ) {
    staticLightSources.addLast(mo);
  }

  //----------

  public void insertDynamicLightSource( AbstractMapObj mo ) {
    dynamicLightSources.addLast(mo);
  }

  //----------

  public void removeDynamicLightSource( AbstractMapObj mo ) {
    dynamicLightSources.remove(mo);
  }

  //----------

  public void updateStaticLightGrid() {
    //create light grid
    float[][] tmpLightGrid = new float[(int)RpgGame.inst.gMap.gridSize.y][];
    for (int y=0; y<tmpLightGrid.length; y++) {
      tmpLightGrid[y] = new float[(int)RpgGame.inst.gMap.gridSize.x];
      for (int x=0; x<tmpLightGrid[0].length; x++) {
        tmpLightGrid[y][x] = 0.1f;
      }
    }
    
    //fill light grid
    for( AbstractMapObj mo : staticLightSources ) {
      int tmpRadius = 5;
      Vector moTile = mo.pos.copy().roundAllInst();
      for( int y = (int) moTile.y-tmpRadius; y <= moTile.y+tmpRadius; y++ ) {
        for( int x = (int) moTile.x-tmpRadius; x <= moTile.x+tmpRadius; x++ ) {
          Vector newTile = new Vector(x, y);
          if(RpgGame.inst.gMap.isTileOnMap(newTile)) {
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
        RpgGame.inst.gMap.grid[y][x].staticBrightness = tmpLightGrid[y][x];
      }
    }
  }

  //----------

  public void updateLightGrid() {
    LinkedList<AbstractMapObj> tmpLightSources = new LinkedList<AbstractMapObj>();
    for( AbstractMapObj mo : dynamicLightSources ) {
      if (isTileOnScreen(mo.pos)) {
        tmpLightSources.add(mo);
      }
    }
    
    //create light grid for screen
    float[][] tmpLightGrid = new float[(int) (maxTileOnScreen.y-minTileOnScreen.y)+1][];
    for (int y=0; y<tmpLightGrid.length; y++) {
      tmpLightGrid[y] = new float[(int) (maxTileOnScreen.x-minTileOnScreen.x)+1];
    }
    
    //fill light grid
    for( AbstractMapObj mo : tmpLightSources ) {
      int tmpRadius = 5;
      Vector moTile = mo.pos.copy().roundAllInst();
      for( int y = (int) moTile.y-tmpRadius; y <= moTile.y+tmpRadius; y++ ) {
        for( int x = (int) moTile.x-tmpRadius; x <= moTile.x+tmpRadius; x++ ) {
          Vector newTile = new Vector(x, y);
          Vector newLightGridTile = Vector.sub( newTile, minTileOnScreen );
          if(isTileOnScreen(newTile)) {
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
    for (int y=0; y<tmpLightGrid.length; y++) {
      for (int x=0; x<tmpLightGrid[0].length; x++) {
        Vector tmpTile = Vector.add( minTileOnScreen, new Vector(x, y) );
        RpgGame.inst.gMap.grid[(int) tmpTile.y][(int) tmpTile.x].currBrightness 
            = RpgGame.inst.gMap.grid[(int) tmpTile.y][(int) tmpTile.x].staticBrightness + tmpLightGrid[y][x];
      }
    }
  }

  //----------

  public void updateMinMaxTilesOnScreen() {
    Vector tmp00Tile = getTilePosByPixPos( new Vector() );
    if (tmp00Tile==null) tmp00Tile=new Vector(0, 0);
    Vector tmp10Tile = getTilePosByPixPos( new Vector(Game.screenPixSize.x, 0) );
    if (tmp10Tile==null) tmp10Tile=new Vector(0, 0);
    Vector tmp01Tile = getTilePosByPixPos( new Vector(0, Game.screenPixSize.y+200) );
    if (tmp01Tile==null) tmp01Tile=new Vector(0, RpgGame.inst.gMap.gridSize.y-1);
    Vector tmp11Tile = getTilePosByPixPos( Game.screenPixSize.copy().addInst(new Vector(200)) );
    if (tmp11Tile==null) tmp11Tile=new Vector(RpgGame.inst.gMap.gridSize.x-1, 0);
    minTileOnScreen = new Vector( tmp00Tile.x, tmp10Tile.y ).roundAllInst();
    maxTileOnScreen = new Vector( tmp11Tile.x, tmp01Tile.y ).roundAllInst();
  }

  //----------

  public boolean isTileOnScreen(Vector newTile) {
    if (newTile.isAllLOE(minTileOnScreen) && newTile.isAllSOE(maxTileOnScreen)) {
      return true;
    } else {
      return false;
    }
  }

  //----------

  public void updateStaticSeeThroughObjects() {
    HashSet<AbstractMapObj> result = new HashSet<AbstractMapObj>();

//    Local TVector tmpTile, MapObj mo, MapObj tmpMo, x%, y%, TVector moTile
//    For mo=EachIn staticMos
//      moTile = mo.Pos.copy().roundAllInst()
//      If mo.objTypeID=Wall.typeID Then 'only walls effected by seethrough
//        For x=-2 To 0
//          For y=-2 To 0
//            If x=0 And y=0 Continue
//            If (x=-2 And y=0) Or (x=0 And y=-2) Continue
//            
//            tmpTile = TVector.add( moTile, TVector.Create(x, y) )
//            If tmpTile.isAllLarger(TVector.createEmpty()) Then
//              For tmpMo=EachIn collGrid[tmpTile.y][tmpTile.x]
//                If tmpMo.objTypeID!=Wall.TypeId And tmpMo.objTypeID!=Door.TypeId Then
//                  result.Insert(mo, mo)
//                  Continue
//                EndIf
//              Next
//            EndIf
//            
//          Next
//        Next
//      EndIf
//    Next

    staticSeeThroughObjects = result;
  }

  //----------

  public void updateSeeThroughObjects() {
    seeThroughObjects.clear();
    for(AbstractMapObj mo : staticSeeThroughObjects) {
      seeThroughObjects.add(mo);
    }

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

    for( AbstractMapObj mo : RpgGame.inst.dynamicMapObjs ) {
      if (!(mo instanceof Selectable)) continue;

      Vector moTile = mo.pos.copy().roundAllInst();
      if( isTileOnScreen( moTile ) ) {
        for( Vector currTile : tilesToTest ) {
          Vector tmpTile = Vector.add( currTile, moTile );
          if( tmpTile.isAllSmaller(RpgGame.inst.gMap.gridSize) ) {
            for( AbstractMapObj tmpMo : RpgGame.inst.gMap.grid[(int) tmpTile.y][(int) tmpTile.x].collList ) {
              if( (tmpMo instanceof AbstractWall || tmpMo instanceof AbstractDoor) && tmpMo.pos.copy().isAllEqual(tmpTile) ) {
                seeThroughObjects.add(tmpMo);
              }
            }
          }
        }
      }
    }
  }

  //----------

  public LinkedList<AbstractMapObj> getMOsAtPixel( Vector newPixel ) { 
    Vector clickedTile = getTilePosByPixPos(newPixel);
    if (clickedTile==null) return null;
    clickedTile.roundAllInst();
    LinkedList<AbstractMapObj> result=new LinkedList<AbstractMapObj>();

    LinkedList<Vector> tilesToTest = new LinkedList<Vector>();
    tilesToTest.addLast( new Vector(0, 0) );
//    tilesToTest.addLast( new Vector(1, 0) ); //TODO
//    tilesToTest.addLast( new Vector(0, 1) );
//    tilesToTest.addLast( new Vector(1, 1) );
//    tilesToTest.addLast( new Vector(2, 1) );
//    tilesToTest.addLast( new Vector(1, 2) );
//    tilesToTest.addLast( new Vector(2, 2) );

    for( Vector currTile : tilesToTest ) {
      currTile.addInst(clickedTile);
      if( currTile.isAllSmaller(RpgGame.inst.gMap.gridSize) ) {
        for( AbstractMapObj tmpMo : RpgGame.inst.gMap.grid[(int) currTile.y][(int) currTile.x].collList ) {
          if( !(tmpMo.image==null || tmpMo instanceof AbstractWall) ) {
            if(!(tmpMo instanceof BreakableMapObj && ((BreakableMapObj) tmpMo).currState!=IntactState.class)) {
              if( !result.contains(tmpMo) ) {
                if( GameUtil.isPixelInMapObj(tmpMo, newPixel) ) {
                  if (tmpMo.pos.copy().roundAllInst().equals(currTile)) { //TODO only while pixel-perfect testing doesn't work
                    result.addLast(tmpMo);
                  }
                }
              }
            }
          }
        }
      }
    }

    if (result.isEmpty()) result=null;
    return result;
  }

  //----------
  
  public void drawAll() {
    batch.begin();
    
//    long time= System.currentTimeMillis();
    
    drawMap();
//    System.out.println(" draw Map: " + (System.currentTimeMillis()-time)); time=System.currentTimeMillis();
    
    drawForms();
//    System.out.println(" draw Forms: " + (System.currentTimeMillis()-time)); time=System.currentTimeMillis();
    
    batch.end();
  }

  //----------
  
  public void drawMap() {
    //center screen on player
    updateOffset();

//    long time= System.currentTimeMillis();
//
    updateSeeThroughObjects();
//    System.out.println("seeThrough: " + (System.currentTimeMillis()-time)); time=System.currentTimeMillis();
//
    updateLightGrid();
//    System.out.println("lightMap: " + (System.currentTimeMillis()-time)); time=System.currentTimeMillis();
//
    drawFloor();
//    System.out.println( "floor: " + (System.currentTimeMillis()-time)) ; time=System.currentTimeMillis();
    
    //draw objects
    RenderMapKey minKeyY = new RenderMapKey(minTileOnScreen, 0);
    RenderMapKey maxKeyY = new RenderMapKey(maxTileOnScreen, 0);
    NavigableMap<RenderMapKey, AbstractMapObj> subMap = renderMap.subMap(minKeyY, true, maxKeyY, true);
    
    Vector newPixPos;
//    int i=0; int j=0;
    for( AbstractMapObj mo : subMap.values() ) {
//      i += 1;
//      time= System.currentTimeMillis();
      Vector moTile = mo.pos.copy().roundAllInst();
      if( isTileOnScreen( moTile ) ) {
//        j += 1;
        boolean isHighlighted=false;
        boolean isTransparent=false;
        float tmpBrightness;

        tmpBrightness = RpgGame.inst.gMap.grid[(int) moTile.y][(int) moTile.x].currBrightness;
        newPixPos = Vector.add( mo.baseImgPixPos, currOffset );

        if( mo==RpgGame.inst.gInput.selectedMO ) {
          isHighlighted=true;
        }
        if( seeThroughObjects.contains(mo) ) {
          isTransparent=true;
        }
        drawBaseImageRect( mo.image, newPixPos, mo.baseImgPixSize, tmpBrightness, mo.currFrame, isTransparent, isHighlighted );
//        if ((System.currentTimeMillis()-time)>1) System.out.println("single mo: " + (System.currentTimeMillis()-time)); time=System.currentTimeMillis();
      }
    }
//    
//    System.out.println(renderMap.size() + " " + i);// + ", " + j);
//    System.out.println("mos: " + (System.currentTimeMillis()-time)); time=System.currentTimeMillis();
//    System.out.println("---");
//
//    for(int x=(int)minTileOnScreen.x; x<maxTileOnScreen.x; x++) {
//      for(int y=(int)minTileOnScreen.y; y<maxTileOnScreen.y; y++) {
////        If (Game.inst.gMap.aStarMap.getvalue(x, y, 0)==1) {
//        if (!Game.inst.gMap.grid[y][x].collList.isEmpty()) {
//          Vector tmpPixPos = Vector.add( Game.inst.gMap.grid[y][x].basePixPos, currOffset );
////          SetColor 255, 0, 0
////          DrawRect( tmpPixPos.x, tmpPixPos.y, 5, 5 )
//          drawText( String.valueOf(Game.inst.gMap.grid[y][x].collList.size()), tmpPixPos );
//        }
//      }
//    }

//    Vector mouseTilePos = getTilePosByPixPos(new Vector(Gdx.input.getX(), Gdx.input.getY()));
//    if (mouseTilePos!=null) {
//      Vector tmpMouseScreenPos = getBasePixPosByTilePos(mouseTilePos).addInst(currOffset);
//      drawTextureRect(new TextureRegion(texture), tmpMouseScreenPos, getPixSizeByTileSize(new Vector(0.1f)));
//    }
    
//    for (int i=0; i<5; i++) {
//      for (int j=0; j<5; j++) {
//        drawTextureRect(new TextureRegion(texture), 
//            getBaseScreenPosByTilePos(new Vector(i, j)).addInst(currOffset), 
//            getScreenSizeByTileSize(new Vector(1, 1.818f*2)));
//      }
//    }
    
//    batch.setColor((float)Math.random(), (float)Math.random(), (float)Math.random(), 1f);
//    drawTextureRect(new TextureRegion(texture), 
//        getBaseScreenPosByTilePos(new Vector(0, 0)).addInst(currOffset), 
//        getScreenSizeByTileSize(new Vector(1, 1.818f*2)));
  }
  
  //----------

  public void drawForms() {
//    long time = System.currentTimeMillis();
    for (AbstractForm currForm : RpgGame.inst.gForms.activeForms) {
      currForm.draw();
    }
//    System.out.println(System.currentTimeMillis()-time);
    
    //mouse
    if( RpgGame.inst.localPlayer.inv.pickedUpItem!=null ) {
      Vector itSize = Vector.mul( RpgGame.inst.localPlayer.inv.pickedUpItem.invSize, new Vector(64) );
      drawTextureRect(ImageData.images[RpgGame.inst.localPlayer.inv.pickedUpItem.imgInv].frames[0],
          RpgGame.inst.gInput.realMousePos.copy().subInst(new Vector(31)),
          new Vector(itSize.x, itSize.y), Color.WHITE);
    }
  }

  //----------

  public void drawFloor() {
    Vector tmpScreenSize;
    Vector tmpScreenOffset;
    Vector tmpScreenPos;
    float tmpBrightness;
    tmpScreenSize = getPixSizeByTileSize( ImageData.defaultImgSize.copy().mulInst(new Vector(1, 2)).mulScalarInst(1) );
    tmpScreenOffset = tmpScreenSize.copy().mulScalarInst(-1);
    tmpScreenOffset.x *= 0.5f;
    tmpScreenOffset.y += getPixSizeByTileSize(new Vector(0.5f)).y;
    tmpScreenOffset.addInst( currOffset );
    for( int x = (int) minTileOnScreen.x; x <= maxTileOnScreen.x; x += 1 ) {
      for( int y = (int) minTileOnScreen.y; y <= maxTileOnScreen.y; y += 1 ) {
        tmpScreenPos = Vector.add( RpgGame.inst.gMap.grid[y][x].basePixPos, tmpScreenOffset );
        if( isPixRectOnScreen( tmpScreenPos, tmpScreenSize ) ) {
          tmpBrightness = RpgGame.inst.gMap.grid[y][x].currBrightness;
          drawBaseImageRect( ImageData.images[ImageData.IMG_MAP_TILE_DEFAULT], 
              tmpScreenPos, tmpScreenSize, tmpBrightness, 0, false, false );
        }
      }
    }
  }
  
}
