package com.philon.rpg.forms;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.philon.engine.forms.GuiElement;
import com.philon.engine.util.Util.Order;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.mos.item.AbstractItem;
import com.philon.rpg.util.RpgUtil;

@Order(20)
public class ItemPopupForm extends GuiElement {
  AbstractItem item;
  String text = "";

  public AbstractItem getItem() {
    return item;
  }
  public void setItem(AbstractItem newItem) {
    item = newItem;
    text = item.getDisplayTextBody();
    setParent(parent); //update container shape for new item
  }
  private int getNumLines() {
    return text.split("\n").length;
  }

  @Override
  protected boolean isStrechable() {
    return false;
  }
  @Override
  protected float getConfiguredXYRatio() {
    return 0.5f / getConfiguredScale();
  }
  @Override
  protected float getConfiguredScale() {
    return getNumLines()/25f;
  }
  @Override
  protected int getConfiguredBackground() {
    return 231;
  }
  @Override
  protected boolean getConfiguredIsSelectable() {
    return false;
  }

  @Override
  protected void execRender(SpriteBatch batch, float deltaTime) {
    Vector screenSize = new Vector(RpgGame.inst.xyRatio, 1);
    if(absPos.x+absSize.x > screenSize.x) absPos.x = screenSize.x-absSize.x;
    if(absPos.y+absSize.y >  screenSize.y) absPos.y =  screenSize.y-absSize.y;

    super.execRender(batch, deltaTime);

    RpgGame.inst.graphics.drawTextMultiline( batch, RpgUtil.font, text, Vector.add(absPos, new Vector(0.03f)) );
  }

}
