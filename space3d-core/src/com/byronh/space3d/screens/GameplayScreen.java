package com.byronh.space3d.screens;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.byronh.space3d.Space3DGame;


public class GameplayScreen extends AbstractScreen {

	Space3DGame game;

	Stage stage;

	Image image;

	public PerspectiveCamera cam;

	public CameraInputController camController;

	public Environment environment;

	public ModelBatch modelBatch;

	public Model sphere;

	public ModelInstance planet;

	public GameplayScreen(Space3DGame game) {

		this.game = game;

		modelBatch = new ModelBatch();

		stage = new Stage();
		image = new Image(game.manager.get("texture-maps/starscape.png", Texture.class));
		//stage.addActor(image);

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .10f, .10f, .10f, 1f));
		environment.add(new DirectionalLight().set(1f, 1f, 1f, -1f, 0f, -0.2f));

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(2f, 2f, 2f);
		cam.lookAt(0, 0, 0);
		cam.near = 0.1f;
		cam.far = 2000f;
		cam.update();

		camController = new CameraInputController(cam);
		camController.scrollFactor = -0.05f;
		Gdx.input.setInputProcessor(camController);

		ModelBuilder modelBuilder = new ModelBuilder();

		sphere = modelBuilder.createSphere(2f, 2f, 2f, 40, 40, new Material(), Usage.Position | Usage.Normal | Usage.TextureCoordinates);
		planet = new ModelInstance(sphere);
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
				| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

		camController.update();
		
		modelBatch.begin(cam);
		modelBatch.render(planet, environment);
		modelBatch.end();

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		sphere.dispose();
		stage.dispose();
		game.manager.unload("texture-maps/starscape.png");
	}
}