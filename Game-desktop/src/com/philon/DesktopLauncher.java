package com.philon;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.philon.rpg.RpgGame;

public class DesktopLauncher {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Game";
		cfg.useGL20 = false;
		cfg.width = 480;
		cfg.height = 320;

		new LwjglApplication(new RpgGame(), cfg);
	}
}
