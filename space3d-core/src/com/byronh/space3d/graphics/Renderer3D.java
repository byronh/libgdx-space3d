package com.byronh.space3d.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.byronh.space3d.Space3DGame;
import com.byronh.space3d.entities.DesertPlanet;
import com.byronh.space3d.entities.Entity;
import com.byronh.space3d.entities.Planet;
import com.byronh.space3d.entities.SimulationListener;
import com.byronh.space3d.entities.TerranPlanet;


public class Renderer3D implements SimulationListener {
	
	private final String shaderDir = "com/byronh/space3d/shaders/";
	private final String[] shaders = { "default", "planet" };

	private ObjectMap<String, Array<ModelInstance>> modelInstances = new ObjectMap<String, Array<ModelInstance>>();
	private ObjectMap<String, ShaderProvider> shaderProviders = new ObjectMap<String, ShaderProvider>();
	private ObjectMap<String, Environment> environments = new ObjectMap<String, Environment>();
	
	private Space3DGame game;
	private ModelBatch modelBatch;
	private SpriteBatch spriteBatch;
	
	private Model sphere;
	
	TextureAttribute desertTexture, terranTexture;

	public Renderer3D(Space3DGame game) {
		this.game = game;
	}

	public void init() {
		
		// Initialize environments
		Environment planetLighting = new Environment();
		planetLighting.set(new ColorAttribute(ColorAttribute.AmbientLight, .1f, .1f, .1f, 1f));
		planetLighting.add(new PointLight().set(1f, 1f, 1f, -15f, -5.5f, 15f, 300f));
		environments.put("planet", planetLighting);
		
		// Initialize shaders
		for (String shaderName : shaders) {
			shaderProviders.put(shaderName, new DefaultShaderProvider(Gdx.files.classpath(shaderDir + shaderName + ".vert.glsl").readString(), Gdx.files
					.classpath(shaderDir + shaderName + ".frag.glsl").readString()));
		}
		
		// Initialize models
		ModelBuilder modelBuilder = new ModelBuilder();
		ColorAttribute spec = ColorAttribute.createSpecular(0.7f, 0.7f, 0.5f, 1f);
		FloatAttribute shine = FloatAttribute.createShininess(8.0f);
		sphere = modelBuilder.createSphere(3f, 3f, 3f, 50, 50, new Material(spec, shine), Usage.Normal | Usage.Position | Usage.TextureCoordinates);
		
		// Initialize textures
		desertTexture = TextureAttribute.createDiffuse(game.assets.get("texture-maps/venus.gif", Texture.class));
		terranTexture = TextureAttribute.createDiffuse(game.assets.get("texture-maps/earth1.jpg", Texture.class));
		
		// Initialize other stuff
		modelBatch = new ModelBatch();
		spriteBatch = new SpriteBatch();
	}
	
	public void render(Camera cam) {
		
		modelBatch.begin(cam);
		for (String shaderName : shaders) {
			for (ModelInstance mi : modelInstances.get(shaderName)) {
				Renderable r = new Renderable();
				mi.getRenderable(r);
				modelBatch.render(mi, environments.get(shaderName), shaderProviders.get(shaderName).getShader(r));
			}
		}
		modelBatch.end();
		
		spriteBatch.begin();
		spriteBatch.end();
		
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
			if (modelInstances.containsKey("planet")) {
				modelInstances.put("planet", new Array<ModelInstance>());
			}
			modelInstances.get("planet").add(planet);
			game.log("Adding planet");
		}
	}

	@Override
	public void onEntityRemoved(Entity entity) {
		// TODO Auto-generated method stub
		
	}

}
