package game.screens;

import engine.ComponentWorld;
import engine.artemis.managers.GroupManager;
import engine.artemis.managers.PlayerManager;
import game.Space3DGame;
import game.factories.PlanetFactory;
import game.factories.ShipFactory;
import game.systems.MovementSystem;
import game.systems.RenderSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class GameplayScreen implements Screen, InputProcessor {

	private Space3DGame game;
	private ComponentWorld world;
	private ShipFactory shipFactory;

	private InputMultiplexer inputMultiplexer;
	private PerspectiveCamera cam;
	private CameraInputController camController;

	private Stage stage;
	private Skin skin;
	private TextButton button;

	public GameplayScreen(Space3DGame game) {
		this.game = game;
	}

	@Override
	public void show() {

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(5.5f, 8.5f, 5.5f);
		cam.lookAt(0, 0, 0);
		cam.near = 0.1f;
		cam.far = 2005f;
		cam.update();

		camController = new CameraInputController(cam);
		camController.translateTarget = false;

		stage = new Stage();
		skin = game.assets.get("ui/Holo-dark-hdpi.json", Skin.class);
		button = new TextButton("Click me", skin, "default");
		button.setPosition(10, Gdx.graphics.getHeight() - 100);
		button.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				button.setText("Clicked!");
			}
		});
		stage.addActor(button);

		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(this);
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(camController);
		Gdx.input.setInputProcessor(inputMultiplexer);

		world = new ComponentWorld();

		world.setSystem(new MovementSystem());
		world.setSystem(new RenderSystem(cam));
		
		world.setManager(new PlayerManager());
		world.setManager(new GroupManager());

		world.initialize();

		shipFactory = new ShipFactory(world, game.assets);
		shipFactory.createShip(-2, 0, -2);
		shipFactory.createShip(0, 0, 0);
		shipFactory.createShip(2, 0, -2);

		PlanetFactory planetFactory = new PlanetFactory(world, game.assets);
		planetFactory.createPlanet(2, 0, -502);
		planetFactory.createSkybox();

	}

	@Override
	public void render(float delta) {
		Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
				| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

		camController.update();

		world.setDelta(delta);
		world.process();

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void dispose() {
		world.dispose();
		skin.dispose();
		stage.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.ESCAPE) {
			game.exit();
			return true;
		} else if (keycode == Input.Keys.SPACE) {
			game.config.devMode = !game.config.devMode;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Input.Buttons.LEFT) {
			Gdx.app.log("Input", "Mouse left-clicked at (" + screenX + "," + screenY + ")");
			// Ray ray = cam.getPickRay(screenX, screenY);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}
}
