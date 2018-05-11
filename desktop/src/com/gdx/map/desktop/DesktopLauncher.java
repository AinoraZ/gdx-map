package com.gdx.map.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gdx.map.GDXMap;

public class DesktopLauncher {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Mapper";
		cfg.width = 1200;
        cfg.height = 720;
		new LwjglApplication(new GDXMap(), cfg);
	}
}
