package zjunk;

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
import components.Movement;
import components.Position;
import components.Render;

import engine.artemis.Entity;
import engine.artemis.World;

public class CBATest implements ApplicationListener {

	private PerspectiveCamera camera;
	private ModelBatch modelBatch;
	private Model boxModel, sphereModel;
	private Environment environment;

	private World world;
	private Entity shape1, shape2;

	@Override
	public void create() {

		camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		camera.position.set(2.5f, 2f, 2.5f);
		camera.lookAt(0f, 0f, 0f);

		camera.near = 0.1f;
		camera.far = 300.0f;

		modelBatch = new ModelBatch();

		ModelBuilder modelBuilder = new ModelBuilder();

		boxModel = modelBuilder.createBox(1f, 1f, 1f, new Material(
				ColorAttribute.createDiffuse(Color.GRAY)), Usage.Position
				| Usage.Normal);
		sphereModel = modelBuilder.createSphere(1f, 1f, 1f, 50, 50,
				new Material(ColorAttribute.createDiffuse(Color.GRAY)),
				Usage.Position | Usage.Normal);

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f,
				0.8f, 0.8f, 1.0f));
		environment.add(new PointLight().set(1f, 1f, 1f, 10f, 5.5f, 15f, 300f));

		world = new World();
		world.setSystem(new MovementSystem());
		world.setSystem(new RenderSystem(camera));
		world.initialize();

		shape1 = world.createEntity();
		Position world1 = world.createComponent(Position.class);
		world1.world.setToTranslation(1f,1f,1f);
		shape1.addComponent(world1);
		Movement movement1 = world.createComponent(Movement.class);
		movement1.velocity = new Vector3(-2f,0f,0f);
		shape1.addComponent(movement1);
		Render render1 = world.createComponent(Render.class);
		render1.environment = environment;
		render1.instance = new ModelInstance(boxModel);
		shape1.addComponent(render1);
		shape1.addToWorld();

		// shape2 = world.createEntity();
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

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT
				| GL20.GL_DEPTH_BUFFER_BIT
				| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV
						: 0));

		camera.update();

		world.setDelta(delta);
		world.process();

		// if (time > 1 && !done) {
		// Movement movement = new Movement();
		// movement.velocity = new Vector3(0f, 0f, -9f);
		// shape2.add(movement);
		// done = true;
		// }

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