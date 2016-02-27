package com.philon.rpg.forms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.philon.engine.Data;
import com.philon.engine.event.ButtonInputListener;
import com.philon.engine.event.JoystickInputListener;
import com.philon.engine.forms.GuiElement;
import com.philon.engine.input.AbstractController.Joystick;
import com.philon.engine.input.Controller.JoystickLeft;
import com.philon.engine.input.Controller.JoystickRight;
import com.philon.engine.input.Controller.MouseButton1;
import com.philon.engine.input.Controller.MouseCursor;
import com.philon.engine.input.Controller.SelectButton;
import com.philon.engine.input.Controller.StartButton;
import com.philon.engine.input.User;
import com.philon.engine.input.XBox360Controller.RealButton1;
import com.philon.engine.util.AnimImage;
import com.philon.engine.util.Util;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.RpgUser;
import com.philon.rpg.map.RpgMap;
import com.philon.rpg.map.RpgMapSaveData;
import com.philon.rpg.map.generator.MapGenerator;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.map.mo.UpdateMapObj;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.mos.player.AbstractChar;
import com.philon.rpg.mos.player.CharAmazon;
import com.philon.rpg.mos.player.CharBarbarian;
import com.philon.rpg.mos.player.CharSorcerer;
import com.philon.rpg.mos.player.AbstractChar.ControllerAI;
import com.philon.rpg.mos.shot.AbstractShot;
import com.philon.rpg.mos.wall.CellarMapStylye;
import com.philon.rpg.util.RpgUtil;

/**
 * Needs to implement getConfiguredSize()
 */
public class MapScreen extends GuiElement {
  public RpgMap gMap;
  public RpgUtil gUtil;

  public RpgMapSaveData levelData[];
  public int numLevels=10;
  public int currLevel = -1;

  public Vector tileScreenSize;
  public float squashFactor;

  public Vector currOffset = new Vector();

  public Vector minTileOnScreen = new Vector();
  public Vector maxTileOnScreen = new Vector();

  public MapScreen() {

    addInputListener(StartButton.class, new ButtonInputListener() {
      @Override
      protected boolean execDown() {
        Gdx.app.exit();
        return true;
      }
    });

    addInputListener(SelectButton.class, new ButtonInputListener() {
      CharacterForm charForm = new CharacterForm();
      InventoryForm invForm = new InventoryForm();
      StatusbarForm statbarForm = new StatusbarForm();

      @Override
      protected boolean execDown() {
        if(RpgGame.inst.exclusiveUser==null) {
          RpgGame.inst.startExclusiveSession(RpgGame.inst.activeUser, charForm, invForm, statbarForm);
        } else {
          RpgGame.inst.endExclusiveSession();
          removeElementByClass(ItemPopupForm.class); //FIXME remove remaining item popups
        }
        return true;
      }
    });

    addInputListener(JoystickLeft.class, new JoystickInputListener() {
      @Override
      protected boolean execChanged(Joystick source) {
        if(RpgGame.inst.exclusiveUser==null) {
          Vector newDir = Vector.rotateDeg(source.pos, -45);
          ((ControllerAI)RpgGame.inst.getActiveUser().character.getAI()).setMovementDir(newDir);
        }
        return true;
      }
    });

    addInputListener(JoystickRight.class, new JoystickInputListener() {
      @Override
      protected boolean execChanged(Joystick source) {
        Vector newDir = source.pos.copy();
        ((ControllerAI)RpgGame.inst.getActiveUser().character.getAI()).setCastingDir(newDir);
        return true;
      }
    });

    addInputListener(RealButton1.class, new ButtonInputListener() {
      @Override
      protected boolean execDown() {
        if(RpgGame.inst.getExclusiveUser()==null) {
          RpgUser activeUser = RpgGame.inst.getActiveUser();
          Vector searchPos = Vector.add( activeUser.character.pos, Vector.mulScalar(activeUser.character.orientation, 0.5f) );

          activeUser.character.interact(
              RpgUtil.selectObjectByTilePos( searchPos, new Vector(0.6f), activeUser.character)
              );
        }
        return true;
      }
    });

    addInputListener(MouseButton1.class, new ButtonInputListener() {
      boolean isMouseActionPerformed = false;

      @Override
      protected boolean execDown() {
        RpgUser activeUser = RpgGame.inst.getActiveUser();
        Vector mouseAbsPos = activeUser.controller.getElementByClass(MouseCursor.class).pos;
        Vector mouseTilePos = RpgUtil.getTilePosByScreenPos(Vector.sub(mouseAbsPos, absPos));

        if(activeUser.character.inv.pickedUpItem==null) {
          RpgMapObj currObj = RpgUtil.selectObjectByTilePos(mouseTilePos, new Vector(0.1f), activeUser.character);
          if(currObj!=null && 1 > Vector.getDistance(currObj.pos, activeUser.character.pos)) {
            activeUser.character.interact(currObj);
            isMouseActionPerformed = true;
          }
          return true;
        } else {
          activeUser.character.inv.dropPickup();
          isMouseActionPerformed = true;
          return true;
        }
      }
      @Override
      protected boolean execPressed() {
        if(isMouseActionPerformed) return true;

        RpgUser activeUser = RpgGame.inst.getActiveUser();
        Vector mouseAbsPos = activeUser.controller.getElementByClass(MouseCursor.class).pos;
        Vector mouseTilePos = RpgUtil.getTilePosByScreenPos( Vector.sub(mouseAbsPos, absPos) );
        if(mouseTilePos==null) mouseTilePos = activeUser.character.pos.copy();

        Vector newDir = Vector.sub(mouseTilePos, activeUser.character.pos).normalizeInst();
        ((ControllerAI)activeUser.character.getAI()).setCastingDir(newDir);
        return true;
      }
      @Override
      protected boolean execUp() {
        isMouseActionPerformed = false;
        ((ControllerAI)RpgGame.inst.getActiveUser().character.getAI()).setCastingDir(new Vector());
        return true;
      }
    });

    gUtil = new RpgUtil(this);
    levelData = new RpgMapSaveData[numLevels];
  }

  @Override
  protected boolean isStrechable() {
    return true;
  }
  @Override
  protected Vector getConfiguredSize() {
    return new Vector(1);
  }

  @Override
  public void setContainerTransform(Vector containerAbsPos, Vector containerAbsSize) {
    super.setContainerTransform(containerAbsPos, containerAbsSize);

    float tileScreenY = 0.075f;
    float tileScreenXYRatio = 1f / 0.57f;
    tileScreenSize = new Vector(tileScreenY*tileScreenXYRatio, tileScreenY);

    if(currLevel==-1) {
      changeLevel(0);
      //RpgData.souMusic.play();
    }
  }

  private void updateOffset() {
    Vector centeredPos = new Vector();
    for(User currUser : RpgGame.inst.users) {
      centeredPos.addInst( ((RpgUser)currUser).character.pos );
    }
    centeredPos = Vector.divScalar( centeredPos, RpgGame.inst.users.size() );

    currOffset = Vector.add( new Vector(-0.5f*xyRatio, -0.5f), RpgUtil.getBaseScreenPosByTilePos(centeredPos) );
  }

  private void updateMinMaxTilesOnScreen() {
    Vector gridSize = gMap.gridSize;
    Vector minXTile = RpgUtil.getTilePosByScreenPos( new Vector() );
    if (minXTile==null) minXTile=new Vector(0, 0);
    Vector minYTile = RpgUtil.getTilePosByScreenPos( new Vector(xyRatio, 0) );
    if (minYTile==null) minYTile=new Vector(0, 0);
    Vector maxYTile = RpgUtil.getTilePosByScreenPos( new Vector(0, 1.3f) );
    if (maxYTile==null) maxYTile=new Vector(0, gridSize.y-1);
    Vector maxXTile = RpgUtil.getTilePosByScreenPos( new Vector(xyRatio + 0.35f, 1) );
    if (maxXTile==null) maxXTile=new Vector(gridSize.x-1, 0);
    minTileOnScreen = new Vector( minXTile.x, minYTile.y ).roundAllInst();
    maxTileOnScreen = new Vector( maxXTile.x, maxYTile.y ).roundAllInst();
  }


  @Override
  protected void execRender(SpriteBatch batch, float deltaTime) {
    super.execRender(batch, deltaTime);

    updateMinMaxTilesOnScreen();
    updateOffset();
    updateMapObjs(deltaTime);
    gMap.updateSeeThroughObjects();
    gMap.updateLightGrid(minTileOnScreen, maxTileOnScreen);
    drawFloor(batch);
    drawMapObjects(batch);
  }

  private void updateMapObjs(float deltaTime) {
    for (int i=0; i<gMap.dynamicMapObjs.size(); i++) {
      UpdateMapObj currMO = gMap.dynamicMapObjs.get(i);
      if( RpgUtil.isTileOnScreen( currMO.pos.copy().roundAllInst() ) || currMO instanceof AbstractShot ) {
        if(!currMO.update(deltaTime)) RpgUtil.removeMapObj(currMO);
      }
    }
  }

  private void drawMapImage(Batch batch, AnimImage newImage, Vector newPos, int newFrame) {
    newImage.draw(batch, newPos, tileScreenSize.x, newFrame);
  }

  @Override
  protected void execDrawOverChildren(SpriteBatch batch) {
    //item as cursor
    RpgUser exclusiveUser = RpgGame.inst.getExclusiveUser();
    if(exclusiveUser!=null) {
      if(exclusiveUser.character.inv.pickedUpItem==null) {
        Vector mousePos = exclusiveUser.controller.getElementByClass(MouseCursor.class).pos;
        drawNormalized(batch, Data.textures.get(344), mousePos, new Vector(0.05f), 0);
      } else {
        AbstractItem pickedUpItem = exclusiveUser.character.inv.pickedUpItem;
        Vector itSize = Vector.mul( pickedUpItem.getInvSize(), new Vector(0.05f) );
        itSize.x = itSize.x;
        Vector mousePos = exclusiveUser.controller.getElementByClass(MouseCursor.class).pos;
        drawNormalized(batch, Data.textures.get(pickedUpItem.getImgInv()), mousePos, itSize, 0);
      }
    }
  }

  private void drawMapObjects(SpriteBatch batch) {
    for( RpgMapObj mo : gMap.generateRenderMap(minTileOnScreen, maxTileOnScreen).values() ) {
      Vector moTile = mo.pos.copy().roundAllInst();
      if( RpgUtil.isTileOnScreen( moTile ) ) {
        Vector newScreenPos = Vector.sub( mo.baseScreenPos, currOffset );

        Color tmpColor = null;
        for(User currUser : RpgGame.inst.users) {
          if(mo == ((RpgUser)currUser).selectedMO) {
            tmpColor = Color.RED.cpy();
            break;
          }
        }
        if(tmpColor==null) {
          float tmpBrightness = gMap.grid[(int)moTile.y][(int)moTile.x].currBrightness;
          tmpColor = new Color( tmpBrightness, tmpBrightness, tmpBrightness, 1 );
        }
        Color charColor = Color.WHITE;
        if(mo instanceof AbstractChar) {
          if(mo instanceof CharAmazon) {
            charColor = Color.GREEN;
          } else if(mo instanceof CharBarbarian) {
            charColor = Color.RED;
          } else if(mo instanceof CharSorcerer) {
            charColor = Color.BLUE;
          }
        }
        tmpColor.mul(charColor);
        if( gMap.seeThroughObjects.contains(mo) ) {
          tmpColor.a = 0.75f;
        }
        batch.setColor(tmpColor);
        drawMapImage(batch, mo.animation.image, newScreenPos, mo.animation.getCurrFrame());
      }
    }
    batch.setColor(Color.WHITE);
  }

  private void drawFloor(SpriteBatch batch) {
    for( int x = (int) minTileOnScreen.x; x <= maxTileOnScreen.x; x++ ) {
      for( int y = (int) minTileOnScreen.y; y <= maxTileOnScreen.y; y++ ) {
        Vector tmpScreenPos = Vector.sub( gMap.grid[y][x].baseScreenPos, currOffset );
        float tmpBrightness = gMap.grid[y][x].currBrightness;
        Color tmpColor = new Color(tmpBrightness, tmpBrightness, tmpBrightness, 1);
        batch.setColor(tmpColor);
        drawMapImage(batch, Data.textures.get(298), tmpScreenPos, 0);
      }
    }
    batch.setColor(Color.WHITE);
  }

  public void changeLevel( int newLevel ) {
    if(newLevel<0) return;
    int prevLevel = currLevel;
    currLevel = newLevel;

    //save and delete current level
    if( gMap!=null ) {
      levelData[prevLevel] = gMap.save();
      for(User currUser : RpgGame.inst.users) {
        ((RpgUser)currUser).charSaveData = ((RpgUser)currUser).character.save();
      }
      gMap = null;
    }

    //load level or create new
    if( levelData[currLevel]==null ) {
      gMap = new RpgMap( new MapGenerator().generateRpgMap(new Vector(100), new CellarMapStylye()) );
    } else {
      gMap = new RpgMap( levelData[currLevel] );
    }

    //determine spawn tile
    Vector newPos;
    if (prevLevel-currLevel<=0) { //level has increased -> coming down
      newPos = gMap.spawnComingDown.copy();
    } else { //coming up
      newPos = gMap.spawnComingUp.copy();
    }

    //create or load players
    for(User currUser : RpgGame.inst.users) {
      RpgUser rpgUser = (RpgUser)currUser;

      AbstractChar newChar;
      Vector nextFreeTile = RpgUtil.getNextFreeTile(newPos, true, true, false, true);
      if( rpgUser.charSaveData==null ) { //create new char
        Class<? extends AbstractChar> newCharClass = null;
        double rnd = Math.random();
        if(rnd<0.33f) {
          newCharClass = CharAmazon.class;
        } else if (rnd<0.66f) {
          newCharClass = CharBarbarian.class;
        } else {
          newCharClass = CharSorcerer.class;
        }
        newChar = Util.instantiateClass(newCharClass);
      } else { //load char
        newChar = rpgUser.charSaveData.load();
      }
      newChar.setPosition(nextFreeTile);
      gMap.insertMapObj(newChar);
      rpgUser.character = newChar;
    }
  }

}
