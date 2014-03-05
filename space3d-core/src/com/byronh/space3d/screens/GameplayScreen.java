package com.byronh.space3d.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.byronh.space3d.Space3DGame;
import com.byronh.space3d.input.KeyboardController;


public class GameplayScreen extends Screen3D {

	Stage stage;
	Image image;
	private InputMultiplexer inputMultiplexer;
	private PerspectiveCamera cam;
	private CameraInputController camController;
	private Environment environment;
	private ModelBatch modelBatch;
	private Model sphere;
	private ModelInstance planet;
	private Skin skin;

	public GameplayScreen(Space3DGame game) {
		super(game);
	}

	@Override
	public void show() {
		super.show();
		
		game.log("Initializing 3D game world");

		inputMultiplexer = new InputMultiplexer();

		modelBatch = new ModelBatch();

		skin = game.manager.get("ui/Holo-dark-hdpi.json", Skin.class);

		stage = new Stage();
		image = new Image(game.manager.get("texture-maps/starscape.png", Texture.class));
		// stage.addActor(image);

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .10f, .10f, .10f, 1f));
		environment.add(new DirectionalLight().set(1f, 1f, 1f, -1f, 0f, -0.2f));

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(2f, 2f, 2f);
		cam.lookAt(0, 0, 0);
		cam.near = 0.1f;
		cam.far = 2000f;
		cam.update();
		
		KeyboardController keyboardController = new KeyboardController(game);

		camController = new CameraInputController(cam);
		camController.scrollFactor = -0.05f;

		ModelBuilder modelBuilder = new ModelBuilder();

		sphere = modelBuilder.createSphere(2f, 2f, 2f, 40, 40, new Material(), Usage.Position | Usage.Normal | Usage.TextureCoordinates);
		planet = new ModelInstance(sphere);

		final TextButton button = new TextButton("Click me", skin, "default");
		button.setPosition(100, 100);

		button.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				button.setText("Clicked!");
			}
		});

		stage.addActor(button);
		
		inputMultiplexer.addProcessor(keyboardController);
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(camController);
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		//"<default>", "depth", "gouraud", "phong", "normal", "fur", "cubemap", "reflect", "test"
		setShader("phong");
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
				| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

		camController.update();

//		modelBatch.begin(cam);
//		modelBatch.render(planet, environment);
//		modelBatch.end();
		
		shaderBatch.begin(cam);
		shaderBatch.render(planet, environment);
		shaderBatch.end();

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void dispose() {
		super.dispose();
		skin.dispose();
		skin = null;
		modelBatch.dispose();
		sphere.dispose();
		stage.dispose();
		// game.manager.unload("texture-maps/starscape.png");
	}
}
