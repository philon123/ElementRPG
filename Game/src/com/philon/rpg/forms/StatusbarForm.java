package com.philon.rpg.forms;

import java.util.List;

import com.philon.engine.forms.AbstractButton;
import com.philon.engine.forms.AbstractForm;
import com.philon.engine.forms.AbstractLabel;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.spell.SpellData;
import com.philon.rpg.stat.StatsObj.StatM1Stype;
import com.philon.rpg.stat.StatsObj.StatM2Stype;

public class StatusbarForm extends AbstractForm {
  
  @Override
  public Vector getPosByScreenSize(Vector newScreenSize) {
    return new Vector(0, newScreenSize.y-150);//+75);
  }
  
  @Override
  public Vector getSizeByScreenSize(Vector newScreenSize) {
    return new Vector(1600, 150);
  }
  
  @Override
  public void draw() {
//  if( fID==FormData.STATUSBAR ) { //TODO health orbs
//  float orbFill;
//  //draw health orb
//  if( Game.inst.localPlayer.stats.values[StatData.HEALTH]==0 ) {
//    orbFill=0;
//  } else {
//    orbFill = Game.inst.localPlayer.stats.values[StatData.HEALTH] / Game.inst.localPlayer.stats.values[StatData.MAXHEALTH];
//  }
//  currTemp = 42; //orb health label
//  tmpPos = Vector.mul( LabelData.pos[currTemp], size ).addInst( new Vector(30*1.5f, 8*1.5f).addInst( pos ) );
//  tmpSize = new Vector( 80*1.5f, 80*1.5f );
//  setColor(255, 60, 60);
//  drawOval( tmpPos.x, tmpPos.y, tmpSize.x, tmpSize.y );
//  setColor(255, 255, 255);
//  drawRect( tmpPos.x, tmpPos.y, tmpSize.x, tmpSize.y*(1-orbFill) );
//
//  //draw mana orb
//  if( Game.inst.localPlayer.stats.values[StatData.MANA]==0 ) {
//    orbFill=0;
//  } else {
//    orbFill = Game.inst.localPlayer.stats.values[StatData.MANA] / Game.inst.localPlayer.stats.values[StatData.MAXMANA];
//  }
//  currTemp = 43; //orb mana label
//  tmpPos = Vector.mul( LabelData.pos[currTemp], size ).addInst( new Vector(4*1.5f, 12*1.5f).addInst( pos ) );
//  tmpSize = new Vector( 82*1.5f, 80*1.5f );
//  setColor(60, 60, 255);
//  drawOval( tmpPos.x, tmpPos.y, tmpSize.x, tmpSize.y );
//  setColor(255, 255, 255);
//  drawRect( tmpPos.x, tmpPos.y, tmpSize.x, tmpSize.y*(1-orbFill) );
//}
    
    super.draw();
    
  }
  
  public class Spell1Button extends AbstractButton {
    
    public Spell1Button() {
      ID = 8;
      pos = new Vector(0.11f, 0.56f) ;
      size = new Vector(66.00f, 66.00f) ;
      img = 0;
      imgPressed = 0;
    }
    
    @Override
    public void execAction() {
      RpgGame.inst.gInput.isSelectingLeft=true;
      RpgGame.inst.localPlayer.spellSelectForm.toggle();
    }
    
    @Override
    public void update() {
      if (RpgGame.inst.localPlayer==null) return;
      int tmpSpell = (Integer)RpgGame.inst.localPlayer.stats.getStatValue(StatM1Stype.class);
      img = SpellData.iconImg[tmpSpell];
      imgPressed = SpellData.iconImg[tmpSpell];
    }
    
  }

  public class Spell2Button extends AbstractButton {
    
    public Spell2Button() {
      ID = 9;
      pos = new Vector(0.85f, 0.56f) ;
      size = new Vector(66.00f, 66.00f) ;
      img = 0;
      imgPressed = 0;
    }
    
    @Override
    public void execAction() {
      RpgGame.inst.gInput.isSelectingLeft=false;
      RpgGame.inst.localPlayer.spellSelectForm.toggle();
    }
    
    @Override
    public void update() {
      if (RpgGame.inst.localPlayer==null) return;
      int tmpSpell = (Integer)RpgGame.inst.localPlayer.stats.getStatValue(StatM2Stype.class);
      img = SpellData.iconImg[tmpSpell];
      imgPressed = SpellData.iconImg[tmpSpell];
    }
    
  }
  
  public class BackgroundLabel extends AbstractLabel {

    public BackgroundLabel() {
      ID = 41;
      pos = new Vector(0.00f, 0.00f) ;
      size = new Vector(1600.00f, 150.00f) ;
      img = 239;
      displayText = "";
    }

  }

  public class OrbManaLabel extends AbstractLabel {

    public OrbManaLabel() {
      ID = 43;
      pos = new Vector(0.89f, 0.00f) ;
      size = new Vector(174.00f, 150.00f) ;
      img = 238;
      displayText = "";
    }

  }

  public class OrbHealthLabel extends AbstractLabel {

    public OrbHealthLabel() {
      ID = 42;
      pos = new Vector(0.00f, 0.00f) ;
      size = new Vector(174.00f, 150.00f) ;
      img = 237;
      displayText = "";
    }

  }

  public class BeltGridLabel extends AbstractItemGridLabel {

    public BeltGridLabel() {
      ID = 44;
      pos = new Vector(0.30f, 0.56f) ;
      size = new Vector(630.00f, 66.00f) ;
      img = 297;
      displayText = "";
      
      gridSize = new Vector(10, 1);
      cellSize = Vector.div(size, gridSize);
    }
    
    @Override
    public boolean dropPickupToCell(Vector newCell) {
      return RpgGame.inst.localPlayer.inv.beltGrid.addPickup(newCell, true);
    }

    @Override
    public AbstractItem getItemByCell(Vector newCell) {
      return RpgGame.inst.localPlayer.inv.beltGrid.getItemByCell(newCell);
    }
    
    @Override
    public List<AbstractItem> getItemsForDraw() {
      return RpgGame.inst.localPlayer.inv.beltGrid.itemList;
    }

  }

  
}
