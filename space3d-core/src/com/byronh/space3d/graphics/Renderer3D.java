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
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.utils.ObjectMap;
import com.byronh.space3d.Simulation;
import com.byronh.space3d.Space3DGame;
import com.byronh.space3d.entities.GameObject;


public class Renderer3D {
	
	private final String shaderDir = "com/byronh/space3d/shaders/";
	private final String[] shaders = { "default", "planet" };

	private ObjectMap<GameObject, ModelInstance> objects = new ObjectMap<GameObject, ModelInstance>();
	private ObjectMap<String, ShaderProvider> shaderProviders = new ObjectMap<String, ShaderProvider>();
	private ObjectMap<String, Environment> environments = new ObjectMap<String, Environment>();
	
	private Space3DGame game;
	private Simulation sim;
	private RenderContext renderContext;
	private ModelBatch modelBatch;
	private SpriteBatch spriteBatch;
	
	private Model sphere;

	public Renderer3D(Space3DGame game) {
		this.game = game;
		game.log("Initializing 3D renderer");
	}

	public void init() {
		
		// Initialize environments
		Environment planetLighting = new Environment();
		planetLighting.set(new ColorAttribute(ColorAttribute.AmbientLight, .1f, .1f, .1f, 1f));
		planetLighting.add(new PointLight().set(1f, 1f, 1f, -15f, -5.5f, 15f, 300f));
		environments.put("planet", planetLighting);
		
		// Initialize shaders
		renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));
		for (String shaderName : shaders) {
			shaderProviders.put(shaderName, new DefaultShaderProvider(Gdx.files.classpath(shaderDir + shaderName + ".vert.glsl").readString(), Gdx.files
					.classpath(shaderDir + shaderName + ".frag.glsl").readString()));
		}
		
		// Initialize models
		ModelBuilder modelBuilder = new ModelBuilder();
		Texture texture = game.assets.get("texture-maps/venus.gif", Texture.class);
		TextureAttribute venus = TextureAttribute.createDiffuse(texture);
		ColorAttribute spec = ColorAttribute.createSpecular(0.7f, 0.7f, 0.5f, 1f);
		FloatAttribute shine = FloatAttribute.createShininess(8.0f);
		sphere = modelBuilder.createSphere(3f, 3f, 3f, 50, 50, new Material(venus, spec, shine), Usage.Normal | Usage.Position | Usage.TextureCoordinates);
		
		// Initialize other stuff
		//modelBatch = new ModelBatch();
		spriteBatch = new SpriteBatch();
	}
	
	public void render(Simulation sim, Camera cam) {
		
		renderContext.begin();
		for (String shaderName : shaders) {
			
			Shader s = shaderProviders.get(shaderName).getShader(new Renderable());
			//renderContext.
			
		}
		renderContext.end();
		
		spriteBatch.begin();
		spriteBatch.end();
		
	}

}
