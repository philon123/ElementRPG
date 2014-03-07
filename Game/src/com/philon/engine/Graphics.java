package com.philon.engine;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.philon.engine.util.Vector;
import com.philon.rpg.RpgGame;

public class Graphics {

  public OrthographicCamera camera;
  public SpriteBatch batch;

	public Graphics() {
	  batch = new SpriteBatch();
	  resizedTrigger(PhilonGame.screenPixSize);
	}

	public void resizedTrigger(Vector newScreenSize) {
	  camera = new OrthographicCamera(newScreenSize.x/newScreenSize.y, 1);
	  batch.setProjectionMatrix(camera.combined);
	}

	public void dispose() {
    batch.dispose();
  }

	/**
   * @param newTextureRegion - {@link TextureRegion}
   * @param newPixPos
   *   - origin is top left facing down (bottom right is PhilonGame.screenPixSize)
   * @param newPixSize
   *   - full screen/window size is PhilonGame.screenPixSize
   **/
  public void drawTextureRect(TextureRegion newTexture, Vector newPixPos, Vector newPixSize, Color newColor) {
    Sprite sprite = new Sprite(newTexture);
    Vector[] newPosAndSize = getScreenPosAndSize(newPixPos, newPixSize);
    sprite.setBounds(newPosAndSize[0].x, newPosAndSize[0].y, newPosAndSize[1].x, newPosAndSize[1].y);
    sprite.setColor(newColor);
    sprite.draw(batch);
  }

  public Vector[] getScreenPosAndSize(Vector newPixPos, Vector newPixSize) { //returns [newScreenPos, newScreenSize]
    //fix proportion
    Vector tmpSize = newPixSize.copy().divInst(new Vector(PhilonGame.screenPixSize.y));
    Vector tmpPos = newPixPos.copy().divInst(new Vector(PhilonGame.screenPixSize.y));

    //align to center of screen
    tmpPos.x = tmpPos.x - (RpgGame.ratioXY/2);
    tmpPos.y = 0.5f - (tmpPos.y + tmpSize.y);

    return new Vector[]{tmpPos, tmpSize};
  }

	//TODO use BitMapFontCache for static Texts
	//TODO cache all texts drawn draw together at end of frame (how to handle overlaps of forms?)
	public void drawText(BitmapFont font, String newText, Vector newPixPosition) {
	  Vector topLeft = new Vector(PhilonGame.screenPixSize.x*-0.5f, PhilonGame.screenPixSize.y/2);
	  Vector tmpPos = topLeft.addInst(new Vector(newPixPosition.x, -newPixPosition.y));

	  batch.end();
	  Camera tmpCamera = new OrthographicCamera(PhilonGame.screenPixSize.x, PhilonGame.screenPixSize.y);
    batch.setProjectionMatrix(tmpCamera.combined);
    batch.begin();
    font.draw(batch, newText, tmpPos.x, tmpPos.y);
    batch.end();
    batch.setProjectionMatrix(camera.combined);
    batch.begin();
	}

	public void drawTextMultiline(BitmapFont font, String newText, Vector newPosition) {
	  Vector topLeft = new Vector(PhilonGame.screenPixSize.x*-0.5f, PhilonGame.screenPixSize.y/2);
    Vector tmpPos = topLeft.addInst(new Vector(newPosition.x, -newPosition.y));

    batch.end();
    Camera tmpCamera = new OrthographicCamera(PhilonGame.screenPixSize.x, PhilonGame.screenPixSize.y);
    batch.setProjectionMatrix(tmpCamera.combined);
    batch.begin();
    font.drawWrapped(batch, newText, tmpPos.x, tmpPos.y, PhilonGame.screenPixSize.y/2);
    batch.end();
    batch.setProjectionMatrix(camera.combined);
    batch.begin();
	}

	public void drawLine(Vector newPosFrom, Vector newPosTo) {
	  batch.end();

	  ShapeRenderer sr = new ShapeRenderer();
//	  sr.setTransformMatrix(camera.combined);
	  sr.begin(ShapeType.Line);
	  sr.setColor(0, 0, 0, 1);
	  sr.line(newPosFrom.x, PhilonGame.screenPixSize.y-newPosFrom.y, newPosTo.x, PhilonGame.screenPixSize.y-newPosTo.y);
	  sr.end();

	  batch.begin();
	}

	public boolean isPixRectOnScreen( Vector newPos, Vector newSize ) {
		if( newPos.isAllSmaller( PhilonGame.screenPixSize ) ) {
			if( newPos.copy().addInst(newSize).isAllLarger( new Vector() ) ) {
				return true;
			}
		}
		return false;
	}

}
