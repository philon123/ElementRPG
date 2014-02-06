package com.philon.rpg.forms;

import com.philon.engine.forms.AbstractButton;
import com.philon.engine.forms.AbstractForm;
import com.philon.engine.forms.AbstractLabel;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;

public class SpellForm extends AbstractForm {
  
  @Override
  public Vector getPosByScreenSize(Vector newScreenSize) {
    return new Vector(newScreenSize.x-size.x, 0);
  }
  
  @Override
  public Vector getSizeByScreenSize(Vector newScreenSize) {
    return new Vector(700, 700);
  }
  
  public class BackgroundLabel extends AbstractLabel {

    public BackgroundLabel() {
      ID = 2;
      pos = new Vector(0.00f, 0.00f) ;
      size = new Vector(700.00f, 700.00f) ;
      img = 228;
      displayText = "";
    }

  }
  
  public class CloseButton extends AbstractButton {
    
    public CloseButton() {
      ID = 2;
      pos = new Vector(0.01f, 0.01f) ;
      size = new Vector(35.00f, 35.00f) ;
      img = 242;
      imgPressed = 243;
    }
    
    @Override
    public void execAction() {
      RpgGame.inst.gForms.removeForm( SpellForm.this );
    }
    
  }

}
