package com.byronh.space3d;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.byronh.space3d.screens.LoadingScreen;

public class Space3DGame extends Game {
	
	public AssetManager manager;

	@Override
	public void create() {	
		Gdx.app.log("INFO", "Launching game.");
		manager = new AssetManager();
		setScreen(new LoadingScreen(this));
	}
	
}