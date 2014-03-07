package com.byronh.space3d.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.byronh.space3d.Simulation;
import com.byronh.space3d.Space3DGame;
import com.byronh.space3d.entities.GameObject;


public class Renderer3D {

	private final String shaderDir = "com/byronh/space3d/shaders/";
	private final String[] shaders = { "default", "planet" };

	private Space3DGame game;

	private ObjectMap<GameObject, ModelInstance> objects = new ObjectMap<GameObject, ModelInstance>();
	private Array<ShaderProvider> shaderProviders = new Array<ShaderProvider>();

	private ModelBatch modelBatch;

	public Renderer3D(Space3DGame game) {
		this.game = game;
	}

	public void init() {
		for (String shaderName : shaders) {
			shaderProviders.add(new DefaultShaderProvider(Gdx.files.classpath(shaderDir + shaderName + ".vert.glsl").readString(), Gdx.files
					.classpath(shaderDir + shaderName + ".frag.glsl").readString()));
		}
	}
	
	public void render(Simulation sim) {
		
	}

}
