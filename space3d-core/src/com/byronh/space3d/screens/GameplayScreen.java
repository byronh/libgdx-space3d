package com.byronh.space3d.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.byronh.space3d.Space3DGame;
import com.byronh.space3d.entities.Simulation;
import com.byronh.space3d.graphics.DebugRenderer;
import com.byronh.space3d.graphics.Renderer3D;
import com.byronh.space3d.input.KeyboardController;


public class GameplayScreen extends AbstractScreen {

	Simulation sim;
	Renderer3D renderer;
	DebugRenderer debugRenderer;

	private InputMultiplexer inputMultiplexer;
	private PerspectiveCamera cam;
	private CameraInputController camController;

	private Stage stage;
	private Skin skin;
	private TextButton button;

	public GameplayScreen(Space3DGame game) {
		super(game);
	}

	@Override
	public void show() {
		super.show();

		game.log("Initializing 3D game world");

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(2.5f, 0.5f, 2.5f);
		cam.lookAt(0, 0, 0);
		cam.near = 0.1f;
		cam.far = 1005f;
		cam.update();

		renderer = new Renderer3D(game);
		debugRenderer = new DebugRenderer(game);
		
		sim = new Simulation();
		sim.addListener(renderer);
		sim.addListener(debugRenderer);

		renderer.init();
		debugRenderer.init();
		sim.init();

		game.log("Initializing game controller");

		KeyboardController keyboardController = new KeyboardController(game);
		camController = new CameraInputController(cam);
		camController.scrollFactor = -0.05f;

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
		inputMultiplexer.addProcessor(keyboardController);
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(camController);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		sim.mainLoop(delta);

		Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
				| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

		camController.update();
		renderer.render(cam);

		stage.act(delta);
		stage.draw();
		
		debugRenderer.render(cam);
	}

	@Override
	public void dispose() {
		super.dispose();
		skin.dispose();
		stage.dispose();
	}
}
