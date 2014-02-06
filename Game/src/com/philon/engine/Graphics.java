package com.philon.engine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.philon.engine.util.AnimTextureRegion;
import com.philon.engine.util.Vector;

public class Graphics {
	
	//<libgdx>
  public OrthographicCamera camera;
  public SpriteBatch batch;
  //</libgdx>

	public Graphics() {
	  batch = new SpriteBatch();
	  resizedTrigger(Game.screenPixSize);
	}
	
	public void resizedTrigger(Vector newScreenSize) {
	  camera = new OrthographicCamera(newScreenSize.x, newScreenSize.y);
	  batch.setProjectionMatrix(camera.combined);
	}
	
	public void dispose() {
    batch.dispose();
  }

	/**
   * @param newTextureRegion - {@link TextureRegion}
   * @param newPixPos
   *   - origin is top left facing down, bottom right is (1, 1)
   * @param newPixSize
   *   - full screen/window size is (1, 1)
   *   
   *   - NO THATS WRONG... ALL SIZES IN PIXELS ORIGIN TL
   **/
  public void drawTextureRect(TextureRegion newTexture, Vector newPixPos, Vector newPixSize, Color newColor) {
    Sprite sprite = new Sprite(newTexture);
    Vector tmpSize = newPixSize.copy();//.divInst(Game.screenPixSize);
    Vector tmpPos = newPixPos.copy();//.divInst(Game.screenPixSize);
    tmpPos.x = tmpPos.x - Game.screenPixSize.x*0.5f;
    tmpPos.y = Game.screenPixSize.y*0.5f - tmpPos.y - tmpSize.y;
    sprite.setBounds(tmpPos.x, tmpPos.y, tmpSize.x, tmpSize.y);
    sprite.setColor(newColor);
    sprite.draw(batch);
  }

	public void drawBaseImageRect( AnimTextureRegion newImage, Vector newPixPos, Vector newPixSize, float newBrightness, int newFrame, boolean isTransparent, boolean isHighlighted ) {
	  Color tmpColor;
		if( isHighlighted ) {
		  tmpColor = Color.RED.cpy();
		} else {
		  tmpColor = new Color( newBrightness, newBrightness, newBrightness, 1 );
		}
		if( isTransparent ) tmpColor.a = 0.5f;
		drawTextureRect( newImage.frames[newFrame], newPixPos, newPixSize, tmpColor );
	}

	public void drawText(BitmapFont font, String newText, Vector newPosition) {
	  Vector topLeft = new Vector(Game.screenPixSize.x*-0.5f, Game.screenPixSize.y/2);
	  Vector tmpPos = topLeft.addInst(new Vector(newPosition.x, -newPosition.y));
    font.draw(batch, newText, tmpPos.x, tmpPos.y);
	}

	public void drawTextMultiline(BitmapFont font, String newText, Vector newPosition) {
	  String[] tmpTexts = newText.split("\n");
	  Vector lineOffset = new Vector(0, 25);
    Vector topLeft = new Vector(Game.screenPixSize.x*-0.5f, Game.screenPixSize.y/2);
    Vector basePos = topLeft.addInst(new Vector(newPosition.x, -newPosition.y));
    for (int i=0; i<tmpTexts.length; i++) {
      Vector tmpPos = Vector.add(basePos, Vector.mulScalar(new Vector(lineOffset.x, -lineOffset.y), i));
      font.draw(batch, tmpTexts[i].trim(), tmpPos.x, tmpPos.y);
    }
  }

	public void drawLine(Vector newPosFrom, Vector newPosTo) {
	  batch.end();
	  
	  ShapeRenderer sr = new ShapeRenderer();
//	  sr.setTransformMatrix(camera.combined);
	  sr.begin(ShapeType.Line);
	  sr.setColor(0, 0, 0, 1);
	  sr.line(newPosFrom.x, Game.screenPixSize.y-newPosFrom.y, newPosTo.x, Game.screenPixSize.y-newPosTo.y);
	  sr.end();
	  
	  batch.begin();
	}
	
	public boolean isPixRectOnScreen( Vector newPos, Vector newSize ) {
		if( newPos.isAllSmaller( Game.screenPixSize ) ) {
			if( newPos.copy().addInst(newSize).isAllLarger( new Vector() ) ) {
				return true;
			}
		}
		return false;
	}

}
