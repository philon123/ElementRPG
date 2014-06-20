package com.philon;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Game";
		cfg.useGL20 = false;
		cfg.width = 480;
		cfg.height = 320;
		cfg.foregroundFPS = 60;

		new LwjglApplication(new com.philon.rpg.RpgGame(), cfg);
	}
}
