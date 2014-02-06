package com.philon.engine;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.philon.engine.forms.AbstractForm;
import com.philon.engine.util.Vector;

public class Game implements ApplicationListener {
  public static Vector screenPixSize = new Vector(800, 800);
  public static float ratioHW;
  public static float fps = 60;
  public static int currFrame;
  
  public Forms gForms;
	
	@Override
	public void create() {		
	  Gdx.graphics.setDisplayMode((int)screenPixSize.x, (int)screenPixSize.y, false);
	  ratioHW = screenPixSize.y/screenPixSize.x;
	  Texture.setEnforcePotImages(false);
	  
	  gForms = new Forms();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {		
	  if (Gdx.input.isKeyPressed(Keys.ESCAPE)) Gdx.app.exit();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		currFrame += 1;
	}
	
	@Override
	public void resize(int width, int height) {
	  screenPixSize = new Vector(width, height);
	  ratioHW = screenPixSize.y/screenPixSize.x;
	  
    for (AbstractForm currForm : gForms.activeForms) {
      currForm.screenSizeChangedTrigger(screenPixSize);
    }
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	public static Vector getScreenPixSize() {
	  return screenPixSize.copy();
	}
	
}
