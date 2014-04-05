package com.philon.engine;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.philon.engine.util.Vector;

public class Graphics {
  protected PhilonGame<?> game;

	public Graphics(PhilonGame<?> newGame) {
	  game = newGame;
	}

	//TODO drawText(): use BitMapFontCache for static Texts,
	//cache all texts drawn draw together at end of frame (how to handle overlaps of forms?)
	public void drawText(SpriteBatch batch, BitmapFont font, String newText, Vector newRelPos) {
	  //text MUST be drawn in pixel coordinates
	  Vector newPixPosition = Vector.mul(newRelPos, new Vector(game.screenPixSize.y));
	  Vector topLeft = new Vector(game.screenPixSize.x*-0.5f, game.screenPixSize.y*0.5f);
    Vector tmpPos = topLeft.addInst(new Vector(newPixPosition.x, -newPixPosition.y));

    batch.end();
    Camera tmpCamera = new OrthographicCamera(game.screenPixSize.x, game.screenPixSize.y);
    batch.setProjectionMatrix(tmpCamera.combined);
    batch.begin();
    font.draw(batch, newText, tmpPos.x, tmpPos.y);
    batch.end();
    batch.setProjectionMatrix(PhilonGame.inst.camera.combined);
    batch.begin();
  }

	public void drawTextMultiline(SpriteBatch batch, BitmapFont font, String newText, Vector newRelPos) {
    //text MUST be drawn in pixel coordinates
	  Vector topLeft = new Vector(game.screenPixSize.x*-0.5f, game.screenPixSize.y*0.5f);
    Vector newPixPosition = Vector.mul(newRelPos, new Vector(game.screenPixSize.y));
    newPixPosition = topLeft.addInst(new Vector(newPixPosition.x, -newPixPosition.y));

    batch.end();
    batch.setProjectionMatrix(new OrthographicCamera(game.screenPixSize.x, game.screenPixSize.y).combined);
    batch.begin();
    font.drawWrapped(batch, newText, newPixPosition.x, newPixPosition.y, game.screenPixSize.y/2);
    batch.end();
    batch.setProjectionMatrix(PhilonGame.inst.camera.combined);
    batch.begin();
	}

	public void drawLine(SpriteBatch batch, Vector newPosFrom, Vector newPosTo) { //TODO drawing lines, this doesnt work
    //text MUST be drawn in pixel coordinates
	  Vector topLeft = new Vector(game.screenPixSize.x*-0.5f, game.screenPixSize.y*0.5f);
    Vector newPixFrom = Vector.mul(newPosFrom, new Vector(game.screenPixSize.y));
    Vector newPixTo = Vector.mul(newPosTo, new Vector(game.screenPixSize.y));
    newPixFrom = topLeft.addInst(new Vector(newPixFrom.x, -newPixFrom.y));
    newPixTo = topLeft.addInst(new Vector(newPixTo.x, -newPixTo.y));

	  batch.end();

	  ShapeRenderer sr = new ShapeRenderer();
	  sr.setProjectionMatrix(new OrthographicCamera(game.screenPixSize.x, game.screenPixSize.y).combined);
	  sr.begin(ShapeType.Line);
	  sr.setColor(0, 0, 0, 1);
	  sr.line(newPixFrom.x, game.screenPixSize.y-newPixFrom.y, newPixTo.x, game.screenPixSize.y-newPixTo.y);
	  sr.end();

	  batch.begin();
	}

}
