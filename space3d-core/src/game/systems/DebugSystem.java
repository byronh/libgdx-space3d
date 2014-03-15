package game.systems;

import game.utils.Renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class DebugSystem extends BaseUISystem {
	
	final float GRID_MIN = -100f;
	final float GRID_MAX = 100f;
	final float GRID_STEP = 10f;
	
	private Renderer renderer;
	private AssetManager assets;
	
	private Model axesModel;
	private ModelInstance axes;
	
	private Skin skin;
	private Label fpsLabel, cameraLabel;

	public DebugSystem(Renderer renderer, AssetManager assetManager) {
		super();
		this.renderer = renderer;
		assets = assetManager;
	}

	@Override
	public void initialize() {
		
		// Build grid and axes
		renderer.modelBuilder.begin();
		MeshPartBuilder builder = renderer.modelBuilder.part("grid", GL20.GL_LINES, Usage.Position | Usage.Color, new Material(new BlendingAttribute(0.4f)));
		builder.setColor(Color.LIGHT_GRAY);
		for (float t = GRID_MIN; t <= GRID_MAX; t += GRID_STEP) {
			builder.line(t, 0, GRID_MIN, t, 0, GRID_MAX);
			builder.line(GRID_MIN, 0, t, GRID_MAX, 0, t);
		}
		builder = renderer.modelBuilder.part("axes", GL20.GL_LINES, Usage.Position | Usage.Color, new Material());
		builder.setColor(Color.RED);
		builder.line(0, 0, 0, 100, 0, 0);
		builder.setColor(Color.GREEN);
		builder.line(0, 0, 0, 0, 100, 0);
		builder.setColor(Color.BLUE);
		builder.line(0, 0, 0, 0, 0, 100);
		axesModel = renderer.modelBuilder.end();
		axes = new ModelInstance(axesModel);

		// Build debug interface
		skin = assets.get("ui/Holo-dark-hdpi.json", Skin.class);
		fpsLabel = new Label("", skin);
		fpsLabel.setPosition(10, 50);
		root.addActor(fpsLabel);
		cameraLabel = new Label("", skin);
		cameraLabel.setPosition(10, 25);
		root.addActor(cameraLabel);
		
		renderer.stage.addActor(root);
		this.hide();
	}

	@Override
	protected void processSystem() {
		
		// Render grid and axes
		renderer.modelBatch.render(axes);
		
		// Render debug UI
		if (Gdx.graphics.getFramesPerSecond() < 59)
			fpsLabel.setColor(Color.RED);
		else
			fpsLabel.setColor(Color.WHITE);
		fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
		cameraLabel.setText("Cam: (" + Math.round(renderer.cam.position.x) + "," + Math.round(renderer.cam.position.y) + "," + Math.round(renderer.cam.position.z) + ")");
	}

}
