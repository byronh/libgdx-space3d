package com.byronh.space3d.graphics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.byronh.space3d.Space3DGame;
import com.byronh.space3d.entities.DesertPlanet;
import com.byronh.space3d.entities.Entity;
import com.byronh.space3d.entities.Planet;
import com.byronh.space3d.entities.SimulationListener;
import com.byronh.space3d.entities.TerranPlanet;


public class Renderer3D implements SimulationListener {

	final String shaderDir = "com/byronh/space3d/shaders/";
	final String[] shaders = { "default", "planet" };

	Space3DGame game;
	AssetManager assets;

	Array<ModelInstance> planets = new Array<ModelInstance>();
	DefaultShaderProvider defaultShaderProvider;
	Environment lights;

	ModelBatch batch;
	Model sphere;
	ModelInstance space;

	TextureAttribute desertTexture, terranTexture;

	Model cube;
	ModelInstance effectsSphere;

	public Renderer3D(Space3DGame game) {
		this.game = game;
		this.assets = game.assets;
	}

	public void init() {

		// Initialize lights
		lights = new Environment();
		lights.set(new ColorAttribute(ColorAttribute.AmbientLight, .1f, .1f, .1f, 1f));
		lights.add(new PointLight().set(1f, 1f, 1f, -15f, -5.5f, 15f, 300f));

		// Initialize shaders
		defaultShaderProvider = new DefaultShaderProvider();

		// Initialize textures
		TextureAttribute spaceTexture = TextureAttribute.createDiffuse(assets.get("texture-maps/galaxy_starfield.png", Texture.class));
		desertTexture = TextureAttribute.createDiffuse(assets.get("texture-maps/venus.gif", Texture.class));
		terranTexture = TextureAttribute.createDiffuse(assets.get("texture-maps/earth1.jpg", Texture.class));

		// Initialize models
		batch = new ModelBatch(defaultShaderProvider);
		ModelBuilder modelBuilder = new ModelBuilder();
		ColorAttribute spec = ColorAttribute.createSpecular(0.7f, 0.7f, 0.5f, 1f);
		FloatAttribute shine = FloatAttribute.createShininess(8.0f);
		sphere = modelBuilder.createSphere(1f, 1f, 1f, 50, 50, new Material(spec, shine), Usage.Normal | Usage.Position | Usage.TextureCoordinates);
//		cube = modelBuilder.createBox(1f, 1f, 1f, new Material(new BlendingAttribute(0.5f)),
//				Usage.Normal | Usage.Position);

		// Initialize model instances
		space = new ModelInstance(sphere);
		space.transform.scl(-1000f);
		space.materials.first().set(spaceTexture);
		effectsSphere = new ModelInstance(sphere);
		effectsSphere.transform.translate(0f, 0.5f, 0f);

		// Initialize post-processing effects
	}

	public void render(Camera cam) {

		batch.begin(cam);
		for (ModelInstance planet : planets) {
			batch.render(planet, lights);
		}
		batch.flush();
		batch.render(space);
		batch.render(effectsSphere);
		batch.end();

	}

	@Override
	public void onEntityAdded(Entity entity) {
		if (entity instanceof Planet) {
			ModelInstance planet = new ModelInstance(sphere, entity.worldTransform);
			if (entity instanceof DesertPlanet) {
				planet.materials.first().set(desertTexture);
			} else if (entity instanceof TerranPlanet) {
				planet.materials.first().set(terranTexture);
			}
			planets.add(planet);
		}
	}

	@Override
	public void onEntityRemoved(Entity entity) {
		// TODO Auto-generated method stub
	}

}
