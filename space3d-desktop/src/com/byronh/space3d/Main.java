package com.byronh.space3d;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		
		cfg.title = "Space3D";
		cfg.width = 1536;
		cfg.height = 864;
		cfg.samples = 4;
		cfg.vSyncEnabled = true;
		
		new LwjglApplication(new Space3DGame(), cfg);
	}
}
