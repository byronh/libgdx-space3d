package com.byronh.space3d;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.byronh.space3d.screens.GameplayScreen;
import com.byronh.space3d.screens.LoadingScreen;


/**
 * Top level class for the core game.
 * @author Byron
 * 
 */
public class Space3DGame extends Game {

	Config config = new Config();

	public AssetManager manager;

	public LoadingScreen loadingScreen;
	public GameplayScreen gameplayScreen;

	@Override
	public void create() {

		loadConfig();

		manager = new AssetManager();

		loadingScreen = new LoadingScreen(this);
		gameplayScreen = new GameplayScreen(this);

		setScreen(loadingScreen);
	}

	/**
	 * Gets the current time on the local machine.
	 * @return the time in HH:mm:ss.SSS format
	 */
	public String time() {
		return new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
	}

	/**
	 * Logs a message to the console if the game is in development mode.
	 * @param message
	 *            a String to write to the console
	 */
	public void log(String message) {
		if (config.devMode) {
			Gdx.app.log(Space3DGame.class.getSimpleName() + "[" + time() + "]", message);
		}
	}

	/**
	 * Exits to desktop.
	 */
	public void exit() {
		log("Exiting game");
		Gdx.app.exit();
	}

	
	/**
	 * Data structure that stores the game's configuration loaded from a JSON file.
	 * @author Byron
	 *
	 */
	private static class Config {
		
		private boolean devMode;
	}

	/**
	 * Loads the game's JSON configuration file.
	 */
	private void loadConfig() {
		FileHandle handle = Gdx.files.internal("config/game.json");
		String fileContent = handle.readString();
		Json json = new Json();

		json.setElementType(Config.class, "devMode", Boolean.class);

		config = json.fromJson(Config.class, fileContent);
		log("Configuration successfully loaded");
	}

}