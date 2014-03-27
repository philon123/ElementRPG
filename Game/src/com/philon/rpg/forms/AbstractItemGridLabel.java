package com.philon.rpg.forms;

import java.util.List;

import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;
import com.philon.rpg.mos.item.AbstractItem;

public abstract class AbstractItemGridLabel extends AbstractItemDropLabel {
  public Vector gridSize;
  public Vector cellSize;

  public Vector getCellByPixel( Vector newPixel ) {
    return Vector.div( newPixel, cellSize ).floorAllInst();
  }

  @Override
  public void dropPickupToPixel(Vector newPixel) {
    dropPickupToCell(getCellByPixel( newPixel ));
  }

  public abstract void dropPickupToCell(Vector newCell);

  @Override
  public AbstractItem getItemByPixel(Vector newPixel) {
    Vector newCell = getCellByPixel( newPixel );
    return getItemByCell(newCell);
  }

  public abstract AbstractItem getItemByCell(Vector newCell);

  public abstract List<AbstractItem> getItemsForDraw();

  @Override
  public void draw(Vector containerPos, Vector containerSize) {
    super.draw(containerPos, containerSize);

    Vector offset = containerPos.copy().addInst(containerSize.copy().mulInst(pos));
    drawGrid(offset);
    for( AbstractItem tmpItem : getItemsForDraw() ) {
      if (tmpItem!=null) drawInvItem( tmpItem, Vector.mul( tmpItem.pos, cellSize ).addInst(offset) );
    }

  }

  private void drawGrid(Vector absPos) {
    for( int x = 0; x <= gridSize.x; x++ ) {
      RpgGame.inst.gGraphics.drawLine(
          new Vector(x*cellSize.x, 0).addInst(absPos),
          new Vector(x*cellSize.x, gridSize.y*cellSize.y).addInst(absPos) );
    }
    for( int y = 0; y <= gridSize.y; y++ ) {
      RpgGame.inst.gGraphics.drawLine(
          new Vector(0, y*cellSize.y).addInst(absPos),
          new Vector(gridSize.x*cellSize.x, y*cellSize.y).addInst(absPos));
    }
  }

}
