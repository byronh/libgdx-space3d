package comparchitecture;

import systems.MovementSystem;
import systems.RenderSystem;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import components.MovementComponent;
import components.RenderComponent;
import components.WorldComponent;


public class CBATest implements ApplicationListener {

	private PerspectiveCamera camera;
	private ModelBatch modelBatch;
	private Model boxModel, sphereModel;
	private Environment environment;

	private RenderSystem renderSystem;
	private MovementSystem movementSystem;
	private Entity shape1, shape2;

	@Override
	public void create() {

		camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		camera.position.set(2.5f, 2f, 2.5f);
		camera.lookAt(0f, 0f, 0f);

		camera.near = 0.1f;
		camera.far = 300.0f;

		modelBatch = new ModelBatch();

		ModelBuilder modelBuilder = new ModelBuilder();

		boxModel = modelBuilder.createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(Color.GRAY)), Usage.Position | Usage.Normal);
		sphereModel = modelBuilder.createSphere(1f, 1f, 1f, 50, 50, new Material(ColorAttribute.createDiffuse(Color.GRAY)), Usage.Position
				| Usage.Normal);

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1.0f));
		environment.add(new PointLight().set(1f, 1f, 1f, 10f, 5.5f, 15f, 300f));

		EntityManager manager = new EntityManager();
		renderSystem = new RenderSystem(manager, camera);
		movementSystem = new MovementSystem(manager);

		shape1 = manager.create();
		shape2 = manager.create();

		WorldComponent world1 = new WorldComponent();
		RenderComponent render1 = new RenderComponent();
		render1.environment = environment;
		render1.instance = new ModelInstance(boxModel);
		shape1.add(world1);
		shape1.add(render1);

		WorldComponent world2 = new WorldComponent();
		RenderComponent render2 = new RenderComponent();
		world2.setToTranslation(-2f, 0f, 0f);
		render2.environment = environment;
		render2.instance = new ModelInstance(sphereModel);
		shape2.add(world2);
		shape2.add(render2);
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		boxModel.dispose();
		sphereModel.dispose();
	}

	float time = 0;
	boolean done = false;

	@Override
	public void render() {

		float delta = Gdx.graphics.getDeltaTime();
		time += delta;

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
				| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

		camera.update();

		renderSystem.update(delta);
		movementSystem.update(delta);

		if (time > 1 && !done) {
			MovementComponent movement = new MovementComponent();
			movement.velocity = new Vector3(0f, 0f, -9f);
			shape2.add(movement);
			done = true;
		}

		// Gdx.app.log("FPS", Gdx.graphics.getFramesPerSecond() + "");
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}