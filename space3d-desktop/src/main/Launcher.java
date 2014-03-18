package main;

import game.Space3DGame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Launcher {
	public static void main(String[] args) {
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		
		cfg.title = "Space3D";
		cfg.width = 1536;
		cfg.height = 864;
//		cfg.vSyncEnabled = true;
		cfg.resizable = false;
		cfg.samples = 8;
		
		new LwjglApplication(new Space3DGame(), cfg);
	}
}
