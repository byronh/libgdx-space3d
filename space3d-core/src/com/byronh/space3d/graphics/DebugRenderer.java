package com.byronh.space3d.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.byronh.space3d.Space3DGame;
import com.byronh.space3d.entities.Entity;
import com.byronh.space3d.entities.SimulationListener;


public class DebugRenderer implements SimulationListener {

	final float GRID_MIN = -10f;
	final float GRID_MAX = 10f;
	final float GRID_STEP = 1f;

	private Space3DGame game;
	private Model axesModel;
	private ModelInstance axes;
	private ModelBatch batch;

	private Stage stage;
	private Skin skin;
	private Label fpsLabel, cameraLabel;

	public DebugRenderer(Space3DGame game) {
		this.game = game;
	}

	public void init() {

		// Build grid and axes

		batch = new ModelBatch();
		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();

		MeshPartBuilder builder = modelBuilder.part("grid", GL20.GL_LINES, Usage.Position | Usage.Color, new Material(new BlendingAttribute(0.2f)));
		builder.setColor(Color.LIGHT_GRAY);
		for (float t = GRID_MIN; t <= GRID_MAX; t += GRID_STEP) {
			builder.line(t, 0, GRID_MIN, t, 0, GRID_MAX);
			builder.line(GRID_MIN, 0, t, GRID_MAX, 0, t);
		}
		builder = modelBuilder.part("axes", GL20.GL_LINES, Usage.Position | Usage.Color, new Material());
		builder.setColor(Color.RED);
		builder.line(0, 0, 0, 100, 0, 0);
		builder.setColor(Color.GREEN);
		builder.line(0, 0, 0, 0, 100, 0);
		builder.setColor(Color.BLUE);
		builder.line(0, 0, 0, 0, 0, 100);

		axesModel = modelBuilder.end();
		axes = new ModelInstance(axesModel);

		// Build debug interface

		stage = new Stage();
		skin = game.assets.get("ui/Holo-dark-hdpi.json", Skin.class);
		fpsLabel = new Label("0", skin);
		fpsLabel.setPosition(10, 50);
		stage.addActor(fpsLabel);

		cameraLabel = new Label("", skin);
		cameraLabel.setPosition(10, 25);
		stage.addActor(cameraLabel);
	}

	public void render(Camera cam) {
		if (game.config.devMode) {
			batch.begin(cam);
			batch.render(axes);
			batch.end();

			if (Gdx.graphics.getFramesPerSecond() < 59)
				fpsLabel.setColor(Color.RED);
			else
				fpsLabel.setColor(Color.WHITE);
			fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
			cameraLabel.setText("Cam: (" + Math.round(cam.position.x) + "," + Math.round(cam.position.y) + "," + Math.round(cam.position.z) + ")");

			stage.act();
			stage.draw();
		}
	}

	@Override
	public void onEntityAdded(Entity entity) {
	}

	@Override
	public void onEntityRemoved(Entity entity) {
	}

}
