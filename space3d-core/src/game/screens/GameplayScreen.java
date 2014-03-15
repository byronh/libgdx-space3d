package game.screens;

import engine.ComponentWorld;
import engine.artemis.managers.GroupManager;
import engine.artemis.managers.PlayerManager;
import engine.artemis.systems.EntitySystem;
import game.Space3DGame;
import game.factories.PlanetFactory;
import game.factories.ShipFactory;
import game.systems.DebugSystem;
import game.systems.HudSystem;
import game.systems.MovementSystem;
import game.systems.PhysicsDebugSystem;
import game.systems.PhysicsSystem;
import game.systems.RenderSystem;
import game.systems.SelectionSystem;
import game.utils.Renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;

public class GameplayScreen extends InputAdapter implements Screen {

	private Space3DGame game;
	private GameState gameState;
	private ComponentWorld world;
	private ShipFactory shipFactory;

	private Renderer renderer;
	private InputMultiplexer inputMultiplexer;
	private PerspectiveCamera cam;
	private CameraInputController camController;

	private class GameState {
		private boolean paused = false;
		private boolean debug = false;
	}

	public GameplayScreen(Space3DGame game) {
		this.game = game;
		this.gameState = new GameState();
	}

	@Override
	public void show() {

		initCameras();
		initSystems();
		initInputs();

		shipFactory = new ShipFactory(world, game.assets);
		for (float x = -4; x <= 4; x += 2) {
			for (float z = -4; z <= 4; z += 2) {
				// for (float y = 4; y >= -4; y -= 2) {
				shipFactory.createShip(x, 0, z);
				// }
			}
		}

		PlanetFactory planetFactory = new PlanetFactory(world, game.assets);
		planetFactory.createPlanet(2, 0, -502);
		planetFactory.createSkybox();
	}

	@Override
	public void render(float delta) {
		Gdx.graphics.setTitle("Space3D - FPS: "
				+ Gdx.graphics.getFramesPerSecond());
		camController.update();

		renderer.begin();
		world.setDelta(delta);
		world.process();
		renderer.end(delta);

		if (gameState.debug) {
			world.getSystem(DebugSystem.class).process();
			world.getSystem(PhysicsDebugSystem.class).process();
		}
	}

	@Override
	public void dispose() {
		world.dispose();
	}

	private void initCameras() {
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		cam.position.set(5.5f, 8.5f, 5.5f);
		cam.lookAt(0, 0, 0);
		cam.near = 0.1f;
		cam.far = 2005f;

		camController = new CameraInputController(cam);
		camController.rotateButton = Input.Buttons.RIGHT;
		camController.translateTarget = false;

		cam.update();
	}

	private void initSystems() {
		world = new ComponentWorld();
		renderer = new Renderer(cam);

		world.setSystem(new MovementSystem());
		world.setSystem(new PhysicsSystem());

		world.setSystem(new RenderSystem(renderer));
		world.setSystem(new SelectionSystem(renderer));
		world.setSystem(new HudSystem(renderer, game.assets));

		if (game.config.devMode) {
			world.setSystem(new DebugSystem(renderer, game.assets), true);
			world.setSystem(new PhysicsDebugSystem(cam), true);
		}

		world.setManager(new PlayerManager());
		world.setManager(new GroupManager());

		world.initialize();
	}

	private void initInputs() {
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(this);
		for (EntitySystem s : world.getSystems()) {
			if (s instanceof InputProcessor) {
				inputMultiplexer.addProcessor((InputProcessor) s);
			}
		}
		inputMultiplexer.addProcessor(renderer.stage);
		inputMultiplexer.addProcessor(camController);
		Gdx.input.setInputProcessor(inputMultiplexer);
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

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.ESCAPE) {
			game.exit();
			return true;
		} else if (keycode == Input.Keys.D && game.config.devMode
				&& Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)
				&& Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
			gameState.debug = !gameState.debug;
			world.getSystem(DebugSystem.class).toggleVisibility();
			return true;
		} else if (keycode == Input.Keys.SPACE) {
			gameState.paused = !gameState.paused;
			if (gameState.paused) {
				world.disableSystem(MovementSystem.class);
				world.disableSystem(PhysicsSystem.class);
			} else {
				world.enableSystem(MovementSystem.class);
				world.enableSystem(PhysicsSystem.class);
			}
		}
		return false;
	}
}
