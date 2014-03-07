package com.byronh.space3d.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.byronh.space3d.Space3DGame;
import com.byronh.space3d.graphics.MultiPassShader;
import com.byronh.space3d.input.KeyboardController;


public class GameScreen extends AbstractScreen {

	Stage stage;
	Image image;
	private InputMultiplexer inputMultiplexer;
	private PerspectiveCamera cam;
	private CameraInputController camController;
	private Environment environment;
	private ModelBatch modelBatch;
	private Model sphere;
	public Array<ModelInstance> instances = new Array<ModelInstance>();
	private Skin skin;

	private Model starScape;

	private Renderable renderable;
	private Shader shader1, shader2;

	private FrameBuffer fb1, fb2;
	private SpriteBatch spriteBatch;
	private Texture pass1, pass2;
	private TextureRegion region1, region2;

	TextButton button;

	public GameScreen(Space3DGame game) {
		super(game);
	}

	@Override
	public void show() {
		super.show();

		game.log("Initializing 3D game world");

		inputMultiplexer = new InputMultiplexer();

		modelBatch = new ModelBatch();
		spriteBatch = new SpriteBatch(1);

		skin = game.assets.get("ui/Holo-dark-hdpi.json", Skin.class);

		stage = new Stage();
		image = new Image(game.assets.get("texture-maps/starscape.png", Texture.class));
		// stage.addActor(image);

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .1f, .1f, .1f, 1f));
		environment.add(new PointLight().set(1f, 1f, 1f, -15f, -5.5f, 15f, 300f));
		// environment.add(new DirectionalLight().set(0.5f, 0.5f, 0.5f, -1f, 0f,
		// -0.2f));

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(7.5f, 1.5f, 7.5f);
		cam.lookAt(0, 0, 0);
		cam.near = 0.1f;
		cam.far = 2000f;
		cam.update();

		KeyboardController keyboardController = new KeyboardController(game);
		camController = new CameraInputController(cam);
		camController.scrollFactor = -0.05f;

		button = new TextButton("Click me", skin, "default");
		button.setPosition(10, 10);
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

		// ///

		ModelBuilder modelBuilder = new ModelBuilder();
		Texture texture = game.assets.get("texture-maps/venus.gif", Texture.class);
		// texture.setFilter(TextureFilter.Nearest,
		// TextureFilter.MipMapLinearNearest);
		// texture.unsafeSetFilter(TextureFilter.MipMapNearestLinear,
		// TextureFilter.Nearest);
		TextureAttribute venus = TextureAttribute.createDiffuse(texture);
		ColorAttribute spec = ColorAttribute.createSpecular(0.7f, 0.7f, 0.5f, 1f);
		FloatAttribute shine = FloatAttribute.createShininess(8.0f);
		sphere = modelBuilder.createSphere(3f, 3f, 3f, 60, 60, new Material(venus, spec, shine), Usage.Normal | Usage.Position | Usage.TextureCoordinates);

		for (int x = -20; x <= 20; x += 20) {
			for (int z = -20; z <= 20; z += 20) {
				ModelInstance instance = new ModelInstance(sphere, x, 0, z);
				instances.add(instance);
			}
		}

		renderable = new Renderable();
		NodePart blockPart = instances.first().nodes.first().parts.first();
		blockPart.setRenderable(renderable);
		renderable.environment = environment;
		renderable.worldTransform.idt();

		String data = "com/byronh/space3d/shaders";
		String vert1 = Gdx.files.classpath(data + "/planet.vert.glsl").readString();
		String frag1 = Gdx.files.classpath(data + "/planet.frag.glsl").readString();
		shader1 = new MultiPassShader(renderable, new DefaultShader.Config(vert1, frag1));
		shader1.init();

//		 DefaultShaderProvider provider = new 
//		 DefaultShaderProvider(Gdx.files.classpath(data +
//		 "/planet.vert.glsl").readString(), Gdx.files.classpath(
//		 data + "/planet.frag.glsl").readString());

		// shader2 = new PlanetShader();
		// shader2.init();

		fb1 = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		fb2 = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
				| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
		modelBatch.begin(cam);

		button.setText(Integer.toString(Gdx.graphics.getFramesPerSecond()));

		camController.update();

		for (ModelInstance instance : instances) {
			// Interpolation.linear.apply(start, end, a)
			instance.transform.rotate(Vector3.Y, 2.5f * delta);
			modelBatch.render(instance, environment, shader1);
		}
		modelBatch.end();

//		fb2.begin();
//		{
//			Gdx.gl.glClearColor(1, 0, 0, 1);
//			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
//			modelBatch.begin(cam);
//			for (ModelInstance instance : instances) {
//				modelBatch.render(instance, environment, shader1);
//			}
//			modelBatch.end();
//		}
//		fb2.end();
//
//		pass2 = fb2.getColorBufferTexture();
//
//		Sprite s = new Sprite(new TextureRegion(pass2));
//
//		spriteBatch.begin();
//		spriteBatch.setColor(1f, 1f, 1f, 0.5f);
//		spriteBatch.draw(s, 300f, 100f, 100f, -100f);
//		spriteBatch.end();

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void dispose() {
		super.dispose();
		shader2.dispose();
		skin.dispose();
		skin = null;
		modelBatch.dispose();
		sphere.dispose();
		stage.dispose();
	}
}
