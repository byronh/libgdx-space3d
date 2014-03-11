package com.byronh.space3d.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.byronh.space3d.Space3DGame;
import com.byronh.space3d.entities.DesertPlanet;
import com.byronh.space3d.entities.Entity;
import com.byronh.space3d.entities.IcePlanet;
import com.byronh.space3d.entities.Planet;
import com.byronh.space3d.entities.SimulationListener;
import com.byronh.space3d.entities.TerranPlanet;


@SuppressWarnings("deprecation")
public class Renderer3D implements Renderer, SimulationListener {

	final String shaderDir = "com/byronh/space3d/shaders/";
	final String[] shaders = { "default", "planet" };

	Space3DGame game;
	AssetManager assets;

	Array<ModelInstance> planets = new Array<ModelInstance>();
	DefaultShaderProvider defaultShaderProvider;
	Environment lights;
	DirectionalShadowLight shadowLight;
	Bloom bloom;

	ModelBatch mainBatch, shadowBatch, effectBatch;
	Model sphere, cube;
	ModelInstance space, effectsSphere, box;

	TextureAttribute desertTexture, terranTexture, iceTexture;

	Shader planetShader;

	public Renderer3D(Space3DGame game) {
		this.game = game;
		this.assets = game.assets;
	}

	public void init() {

		// Initialize lights
		lights = new Environment();
		lights.set(new ColorAttribute(ColorAttribute.AmbientLight, .1f, .1f, .1f, 1f));
		// lights.add(new PointLight().set(1f, 1f, 1f, -15f, -5.5f, 15f, 300f));
		lights.add((shadowLight = new DirectionalShadowLight(1024 * 2, 1024 * 2, 10f, 10f, 1f, 200f)).set(1f, 1f, 1f, -1f, -.8f, 0f));
		lights.shadowMap = shadowLight;

		// Initialize shaders
		defaultShaderProvider = new DefaultShaderProvider();

		// Initialize textures
		TextureAttribute spaceTexture = TextureAttribute.createDiffuse(assets.get("texture-maps/galaxy_starfield.png", Texture.class));
		desertTexture = TextureAttribute.createDiffuse(assets.get("texture-maps/venus.gif", Texture.class));
		terranTexture = TextureAttribute.createDiffuse(assets.get("texture-maps/earth1.jpg", Texture.class));
		iceTexture = TextureAttribute.createDiffuse(assets.get("texture-maps/planet1.png", Texture.class));

		// Initialize models
		mainBatch = new ModelBatch(defaultShaderProvider);
		shadowBatch = new ModelBatch(new DepthShaderProvider());
		effectBatch = new ModelBatch();

		ModelBuilder modelBuilder = new ModelBuilder();
		ColorAttribute spec = ColorAttribute.createSpecular(0.7f, 0.7f, 0.5f, 1f);
		FloatAttribute shine = FloatAttribute.createShininess(8.0f);
		sphere = modelBuilder.createSphere(1f, 1f, 1f, 60, 60, new Material(spec, shine), Usage.Normal | Usage.Position | Usage.TextureCoordinates);
		cube = modelBuilder.createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(Color.GRAY)), Usage.Normal | Usage.Position | Usage.TextureCoordinates);

		// Initialize model instances
		space = new ModelInstance(sphere);
		space.transform.scl(-1000f);
		space.materials.first().set(spaceTexture);
		effectsSphere = new ModelInstance(sphere);
		// effectsSphere.materials.first().set(new BlendingAttribute(0.5f), new
		// DepthTestAttribute(GL20.GL_NONE));
		// effectsSphere.transform.translate(0f, 0f, 0f);//.scl(1.02f);
		box = new ModelInstance(cube);
		box.transform.setToTranslationAndScaling(-1.5f, -1.5f, -1f, 5f, 0.5f, 1.5f);

		// Initialize
		planetShader = new ToonShader();
		planetShader.init();

		// Initialize post-processing effects
//		bloom = new Bloom();
//		bloom.setBloomIntesity(0.5f);
	}

	public void render(Camera cam) {

		// Render shadows
		// shadowLight.update(cam);
		shadowLight.begin(Vector3.Zero, cam.direction);
		shadowBatch.begin(shadowLight.getCamera());
		for (ModelInstance planet : planets) {
			shadowBatch.render(planet);
		}
		shadowBatch.render(box);
		shadowBatch.end();
		shadowLight.end();

		Gdx.gl.glClearColor(0, 0, 0, 1);

		// Render models with basic lighting
//		 bloom.capture();
		mainBatch.begin(cam);
		for (ModelInstance planet : planets) {
			mainBatch.render(planet, lights);
		}

		mainBatch.render(box, lights);

		mainBatch.flush();
		mainBatch.render(space);
		effectsSphere.transform = planets.first().transform;
		mainBatch.render(effectsSphere, lights, planetShader);
		mainBatch.end();

//		 bloom.render();
	}

	@Override
	public void dispose() {
		mainBatch.dispose();
		shadowBatch.dispose();
		sphere.dispose();
		cube.dispose();
	}

	@Override
	public void onEntityAdded(Entity entity) {
		if (entity instanceof Planet) {
			ModelInstance planet = new ModelInstance(sphere, entity.worldTransform);
			if (entity instanceof DesertPlanet) {
				planet.materials.first().set(desertTexture);
			} else if (entity instanceof TerranPlanet) {
				planet.materials.first().set(terranTexture);
			} else if (entity instanceof IcePlanet) {
				planet.materials.first().set(iceTexture);
			}
			planets.add(planet);
		}
	}

	@Override
	public void onEntityRemoved(Entity entity) {
		// TODO Auto-generated method stub
	}

}
