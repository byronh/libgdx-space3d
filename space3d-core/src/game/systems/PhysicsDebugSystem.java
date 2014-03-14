package game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
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
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import engine.artemis.ComponentMapper;
import engine.artemis.Entity;
import engine.artemis.Filter;
import engine.artemis.systems.EntitySystem;
import engine.physics.DebugDrawer;
import game.components.Physics;
import game.components.Position;

public class PhysicsDebugSystem extends EntitySystem implements InputProcessor {

	ComponentMapper<Position> positionMapper;
	ComponentMapper<Physics> physicsMapper;
	
	final float GRID_MIN = -100f;
	final float GRID_MAX = 100f;
	final float GRID_STEP = 10f;

	private boolean enabled = true;
	private Camera cam;
	private DebugDrawer debugDrawer = null;
	private btDynamicsWorld collisionWorld;
	
	private Model axesModel;
	private ModelInstance axes;
	private ModelBatch batch;
	
	private AssetManager assets;
	private Stage stage;
	private Skin skin;
	private Label fpsLabel, cameraLabel;

	@SuppressWarnings("unchecked")
	public PhysicsDebugSystem(Camera camera, AssetManager assetManager) {
		super(Filter.allComponents(Position.class, Physics.class));
		cam = camera;
		assets = assetManager;
	}

	@Override
	public void initialize() {
		
		// Build Bullet physics debugger
		positionMapper = world.getMapper(Position.class);
		physicsMapper = world.getMapper(Physics.class);
		collisionWorld = world.getSystem(PhysicsSystem.class).getCollisionWorld();
		collisionWorld.setDebugDrawer(debugDrawer = new DebugDrawer());
		debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawWireframe);
		
		// Build grid and axes
		batch = new ModelBatch();
		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();
		MeshPartBuilder builder = modelBuilder.part("grid", GL20.GL_LINES, Usage.Position | Usage.Color, new Material(new BlendingAttribute(0.4f)));
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
		skin = assets.get("ui/Holo-dark-hdpi.json", Skin.class);
		fpsLabel = new Label("0", skin);
		fpsLabel.setPosition(10, 50);
		stage.addActor(fpsLabel);
		cameraLabel = new Label("", skin);
		cameraLabel.setPosition(10, 25);
		stage.addActor(cameraLabel);
	}

	@Override
	protected void processEntities(Array<Entity> entities) {
		if (enabled) {
			
			// Render grid and axes
			batch.begin(cam);
			batch.render(axes);
			batch.end();
			
			// Render Bullet physics bodies
			debugDrawer.lineRenderer.setProjectionMatrix(cam.combined);
			debugDrawer.begin();
			collisionWorld.debugDrawWorld();
			debugDrawer.end();
			
			// Render debug UI
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
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.SPACE) {
			enabled = !enabled;
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

}
