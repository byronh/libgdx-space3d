package game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Renderer {
	
	public final PerspectiveCamera cam;
	
	public final ModelBatch modelBatch;
	public final ModelBuilder modelBuilder;
	public final ShapeRenderer shapeRenderer;
	public final Stage stage;
	
	public Renderer(PerspectiveCamera camera) {
		cam = camera;
		modelBatch = new ModelBatch();
		modelBuilder = new ModelBuilder();
		shapeRenderer = new ShapeRenderer();
		stage = new Stage();
	}
	
	public void begin() {
		Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
				| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
		
		modelBatch.begin(cam);
	}
	
	public void end(float delta) {
		modelBatch.end();
		stage.act(delta);
		stage.draw();
	}

}
