package com.philon.rpg;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.philon.engine.Input;
import com.philon.engine.util.Vector;
import com.philon.rpg.map.mo.BreakableMapObj;
import com.philon.rpg.map.mo.CombatMapObj;
import com.philon.rpg.map.mo.CombatMapObj.StateHit;
import com.philon.rpg.map.mo.RpgMapObj;
import com.philon.rpg.map.mo.UpdateMapObj.StateDying;
import com.philon.rpg.map.mo.UpdateMapObj.StateMovingStraight;
import com.philon.rpg.mos.enemy.AbstractEnemy;
import com.philon.rpg.mos.player.AbstractChar;
import com.philon.rpg.stat.StatsObj.StatM1Stype;
import com.philon.rpg.stat.StatsObj.StatM2Stype;

public class RpgInput extends Input {
  public static final int invHotkey = Keys.I;
  public static final int speHotkey = Keys.L; //TODO change back
  public static final int chaHotkey = Keys.C;

  public RpgMapObj selectedMO;

  public RpgInput() {
    keysWatched.addLast(invHotkey);
    keysWatched.addLast(chaHotkey);
    keysWatched.addLast(speHotkey);
  }

  @Override
  public void handleUserInput() {
    super.handleUserInput();

    if (keysIsReleased.get(invHotkey)) RpgGame.inst.localPlayer.invForm.toggle();
    if (keysIsReleased.get(speHotkey)) RpgGame.inst.localPlayer.spellForm.toggle();
    if (keysIsReleased.get(chaHotkey)) RpgGame.inst.localPlayer.charForm.toggle();

    //keyboard
    Vector newPDir = new Vector();
    if( Gdx.input.isKeyPressed(Keys.W) ) {
      newPDir.x += -1;
      newPDir.y += -1;
    }
    if( Gdx.input.isKeyPressed(Keys.S) ) {
      newPDir.x += +1;
      newPDir.y += +1;
    }
    if( Gdx.input.isKeyPressed(Keys.A) ) {
       newPDir.x += -1;
       newPDir.y += +1;
    }
    if( Gdx.input.isKeyPressed(Keys.D) ) {
       newPDir.x += +1;
       newPDir.y += -1;
    }
    RpgGame.inst.localPlayer.newDir = newPDir;
    if (newPDir.equals(new Vector())) {
      RpgGame.inst.localPlayer.isKeyMovement = false;
      if (RpgGame.inst.localPlayer.isKeyMovement && RpgGame.inst.localPlayer.currState==StateMovingStraight.class) {
        RpgGame.inst.localPlayer.changeState(AbstractChar.StateIdle.class);
      }
    } else {
      newPDir.normalizeInst();
      RpgGame.inst.localPlayer.isKeyMovement = true;
    }
  }

  public void handleMouseOverGame( Vector mousePixPos, boolean isM1down, boolean isM2down ) {
    Vector mouseTilePos = RpgGame.inst.gGraphics.getTilePosByPixPos(mousePixPos);
    if (mouseTilePos==null) mouseTilePos=RpgGame.inst.localPlayer.pos.copy();

    //selectedMO
    selectedMO = selectObjectByTilePos(mouseTilePos);
    RpgGame.inst.localPlayer.inv.hoveredOverItem = null;

    boolean m1Click = isM1down && !m1actionPerformed;
    boolean m2Click = isM2down && !m2actionPerformed;
    if (m1Click) m1actionPerformed=true;
    if (m2Click) m2actionPerformed=true;
    if (!(m1Click || m2Click)) return;

    if (isM1down || isM2down) {
      if( selectedMO==null ) {
        RpgGame.inst.localPlayer.setTarget( null, mouseTilePos.copy() );
      } else {
        RpgGame.inst.localPlayer.setTarget( selectedMO );
      }
    }

    if (!(RpgGame.inst.localPlayer.currState==StateHit.class || RpgGame.inst.localPlayer.currState==StateDying.class)) {
      boolean isShiftDown = Gdx.input.isKeyPressed(Keys.SHIFT_LEFT);
      if( m1Click ) {
        RpgGame.inst.localPlayer.currSelectedSpell = (Integer) RpgGame.inst.localPlayer.stats.getStatValue(StatM1Stype.class);
        if(isShiftDown) { //force attack
          RpgGame.inst.localPlayer.changeState( CombatMapObj.StateAttacking.class );
          return;
        }
        if( RpgGame.inst.localPlayer.inv.pickedUpItem!=null ) {
          RpgGame.inst.localPlayer.inv.dropPickup();
          return;
        }
        if( selectedMO==null) {
          RpgGame.inst.localPlayer.changeState( CombatMapObj.StateMovingTarget.class );
          return;
        }
        if( selectedMO instanceof AbstractEnemy || selectedMO instanceof BreakableMapObj ) {
          RpgGame.inst.localPlayer.changeState( CombatMapObj.StateAttacking.class );
          return;
        }
        RpgGame.inst.localPlayer.changeState( CombatMapObj.StateInteracting.class );
      }
      if( m2Click ) {
        RpgGame.inst.localPlayer.currSelectedSpell =  (Integer) RpgGame.inst.localPlayer.stats.getStatValue(StatM2Stype.class);
        RpgGame.inst.localPlayer.changeState( CombatMapObj.StateAttacking.class );
        return;
      }
    }

  }

  public RpgMapObj selectObjectByTilePos(Vector newTilePos) {
    Vector mouseTile = Vector.floorAll( newTilePos );
    if( !RpgGame.inst.gMap.isTileOnMap(mouseTile) ) return null;

    LinkedList<RpgMapObj> selectedList = RpgGame.inst.gMap.selectMOsAtPixel( realMousePos );
    selectedList.remove(RpgGame.inst.localPlayer);
    if (selectedList.isEmpty()) return null;

    //prioritize enemies to be selected
    for( RpgMapObj tmpMO : selectedList ) {
      if(tmpMO instanceof AbstractEnemy) {
        return tmpMO;
      }
    }

    //if no enemy found -> other selectable
    return selectedList.getFirst();
  }

}
