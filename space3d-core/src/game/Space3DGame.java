package game;

import game.screens.GameplayScreen;
import game.screens.LoadingScreen;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;


/**
 * Top level class for the core game.
 * @author Byron
 * 
 */
public class Space3DGame extends Game {

	public GameConfig config = new GameConfig();

	public AssetManager assets;

	public LoadingScreen loadingScreen;
	public GameplayScreen gameplayScreen;

	@Override
	public void create() {

		loadConfig();
		
		log("OpenGL version " + Gdx.gl20.glGetString(GL20.GL_VERSION));

		assets = new AssetManager();

		loadingScreen = new LoadingScreen(this);
		gameplayScreen = new GameplayScreen(this);
		
		setScreen(loadingScreen);
	}
	
	@Override
	public void render() {
		try {
			super.render();
		} catch (Exception e) {
			e.printStackTrace();
			throw new GdxRuntimeException(e);
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
		assets.dispose();
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
			Gdx.app.log(Space3DGame.class.getSimpleName()/* + "[" + time() + "]"*/, message);
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
	public static class GameConfig {
		
		public boolean devMode;
	}

	/**
	 * Loads the game's JSON configuration file.
	 */
	private void loadConfig() {
		FileHandle handle = Gdx.files.internal("config/game.json");
		String fileContent = handle.readString();
		Json json = new Json();

		json.setElementType(GameConfig.class, "devMode", Boolean.class);

		config = json.fromJson(GameConfig.class, fileContent);
		log("Configuration successfully loaded");
	}

}