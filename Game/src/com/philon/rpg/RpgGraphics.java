package com.philon.rpg;

import java.util.TreeMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.philon.engine.Graphics;
import com.philon.engine.PhilonGame;
import com.philon.engine.forms.AbstractForm;
import com.philon.engine.util.Vector;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.util.RenderMapKey;

public class RpgGraphics extends Graphics {
  public Vector squashFactor;
  public Vector tileScreenSize;

  public Vector baseOffset = new Vector();
  public Vector currOffset = new Vector();

  public Vector minTileOnScreen=new Vector();
  public Vector maxTileOnScreen=new Vector();

  public RpgMapObj centeredMo;
  public BitmapFont font;

  public RpgGraphics() {
    super();

    font = new BitmapFont(Gdx.files.internal("assets/" + "data/Media/font/centaur25.fnt"), false);
    font.setColor(new Color(1, 1, 1, 1));
//    font.setScale(0.8f);

    squashFactor = new Vector(1, 0.57f);
    tileScreenSize = new Vector(64f);
  }

  public void updateOffset() {
    if( centeredMo!=null ) {
      Vector tmpOffset = getPixDirByTileDir(centeredMo.pos).mulScalarInst(-1);
      currOffset = baseOffset.copy().addInst(tmpOffset);
    } else {
      currOffset = baseOffset.copy();
    }
  }

  @Override
  public void resizedTrigger(Vector newScreenSize) {
    super.resizedTrigger(newScreenSize);

    baseOffset = newScreenSize.copy().mulInst(new Vector(0.5f));
  }

  public Vector getBasePixPosByTilePos( Vector newTilePos ) { //does not include global offset, allows baking static positions
    return newTilePos.copy().rotateDegInst(45).mulInst(tileScreenSize).mulInst(squashFactor);
  }

  public Vector getTilePosByPixPos( Vector newPixel ) {
    Vector result = newPixel.copy().subInst(currOffset).divInst(squashFactor).divInst(tileScreenSize).rotateDegInst(-45);
    if (result.isEitherSmaller(new Vector()) || result.isEitherLarger(Vector.sub(RpgGame.inst.gMap.gridSize, new Vector(1)))) return null;
    return result;
  }

  public Vector getPixSizeByTileSize( Vector newTileSize ) {
    return newTileSize.copy().mulInst(tileScreenSize).mulScalarInst(1.4142f).mulInst(squashFactor);
  }

  public Vector getPixDirByTileDir( Vector newTileDir ) {
    return newTileDir.copy().rotateDegInst(45).mulInst(tileScreenSize).mulInst(squashFactor);
  }

  //----------

  public void updateMinMaxTilesOnScreen() {
    Vector minXTile = getTilePosByPixPos( new Vector() );
    if (minXTile==null) minXTile=new Vector(0, 0);
    Vector minYTile = getTilePosByPixPos( new Vector(PhilonGame.screenPixSize.x, 0) );
    if (minYTile==null) minYTile=new Vector(0, 0);
    Vector maxYTile = getTilePosByPixPos( new Vector(0, PhilonGame.screenPixSize.y) );
    if (maxYTile==null) maxYTile=new Vector(0, RpgGame.inst.gMap.gridSize.y-1);
    Vector maxXTile = getTilePosByPixPos( PhilonGame.screenPixSize );
    if (maxXTile==null) maxXTile=new Vector(RpgGame.inst.gMap.gridSize.x-1, 0);
    minTileOnScreen = new Vector( minXTile.x, minYTile.y ).roundAllInst();
    maxTileOnScreen = new Vector( maxXTile.x, maxYTile.y ).roundAllInst();
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

    RpgGame.inst.gMap.updateSeeThroughObjects();
//    System.out.println("seeThrough: " + (System.currentTimeMillis()-time)); time=System.currentTimeMillis();

    RpgGame.inst.gMap.updateLightGrid();
//    System.out.println("lightMap: " + (System.currentTimeMillis()-time)); time=System.currentTimeMillis();

    drawFloor();
//    System.out.println( "floor: " + (System.currentTimeMillis()-time)) ; time=System.currentTimeMillis();

    //draw objects
    TreeMap<RenderMapKey, RpgMapObj> renderMap = RpgGame.inst.gMap.generateRenderMap();

    Vector newPixPos;
//    int dbgCounter=0;
    for( RpgMapObj mo : renderMap.values() ) {
      Vector moTile = mo.pos.copy().roundAllInst();
      if( isTileOnScreen( moTile ) ) {
//        dbgCounter += 1;
        newPixPos = Vector.add( mo.baseImgPixPos, currOffset );

        Color tmpColor;
        if( mo==RpgGame.inst.gInput.selectedMO ) {
          tmpColor = Color.RED.cpy();
        } else {
          float tmpBrightness = RpgGame.inst.gMap.grid[(int)moTile.y][(int)moTile.x].currBrightness;
          tmpColor = new Color( tmpBrightness, tmpBrightness, tmpBrightness, 1 );
        }
        if( RpgGame.inst.gMap.seeThroughObjects.contains(mo) ) {
          tmpColor.a = 0.5f;
        }
        drawTextureRect( mo.animation.image.frames[mo.animation.getCurrFrame()], newPixPos, mo.baseImgPixSize, tmpColor );
      }
    }
//
//    System.out.println("mos: " + (System.currentTimeMillis()-time)); time=System.currentTimeMillis();
//    System.out.println("all: " + renderMap.size() + ", drawn: " + dbgCounter);
//    System.out.println("---");
  }

  //----------

  public void drawForms() {
//    long time = System.currentTimeMillis();
    for (AbstractForm currForm : PhilonGame.gForms.activeForms) {
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
    TextureRegion floorImg = ImageData.images[ImageData.IMG_MAP_TILE_DEFAULT].frames[0];
    for( int x = (int) minTileOnScreen.x; x <= maxTileOnScreen.x; x++ ) {
      for( int y = (int) minTileOnScreen.y; y <= maxTileOnScreen.y; y++ ) {
        tmpScreenPos = Vector.add( RpgGame.inst.gMap.grid[y][x].basePixPos, tmpScreenOffset );
        if( isPixRectOnScreen( tmpScreenPos, tmpScreenSize ) ) {
          tmpBrightness = RpgGame.inst.gMap.grid[y][x].currBrightness;
          Color tmpColor = new Color(tmpBrightness, tmpBrightness, tmpBrightness, 1);
          drawTextureRect(floorImg, tmpScreenPos, tmpScreenSize, tmpColor);
        }
      }
    }
  }

}
