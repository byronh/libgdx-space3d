package com.byronh.space3d.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.utils.GdxRuntimeException;


/**
 * @author Byron
 */
public class PlanetShader implements Shader {

	ShaderProgram program;
	Camera camera;
	RenderContext context;
	
	Matrix3 temp = new Matrix3();
	
	int u_cameraDirection;
	
	int u_projTrans;
	int u_viewTrans;
	int u_worldTrans;
	int u_normalMatrix;
	
	int u_viewPos;
	
	int u_diffuseTexture;

	private String data = "com/byronh/space3d/shaders";

	@Override
	public void init() {
		String vert = Gdx.files.classpath(data + "/planet.vert.glsl").readString();
		String frag = Gdx.files.classpath(data + "/planet.frag.glsl").readString();
		program = new ShaderProgram(vert, frag);
		if (!program.isCompiled())
			throw new GdxRuntimeException(program.getLog());
		
		u_projTrans = program.getUniformLocation("u_projTrans");
		u_viewTrans = program.getUniformLocation("u_viewTrans");
		u_worldTrans = program.getUniformLocation("u_worldTrans");
		u_normalMatrix = program.getUniformLocation("u_normalMatrix");
		
		u_viewPos = program.getUniformLocation("u_viewPos");
		
		u_diffuseTexture = program.getUniformLocation("u_diffuseTexture");
	}

	@Override
	public void dispose() {
		program.dispose();
	}

	@Override
	public void begin(Camera camera, RenderContext context) {
		this.camera = camera;
		this.context = context;
		program.begin();
		program.setUniformMatrix(u_projTrans, camera.combined);
		program.setUniformMatrix(u_viewTrans, camera.view);
		program.setUniformf(u_viewPos, camera.position);
		context.setDepthTest(GL20.GL_LEQUAL);
		context.setCullFace(GL20.GL_BACK);
	}

	@Override
	public void render(Renderable renderable) {
		program.setUniformMatrix(u_worldTrans, renderable.worldTransform);
		program.setUniformMatrix(u_normalMatrix, temp.set(renderable.worldTransform).inv().transpose());
		program.setUniformi(u_diffuseTexture,
				context.textureBinder.bind(((TextureAttribute) renderable.material.get(TextureAttribute.Diffuse)).textureDescription.texture));
		renderable.mesh.render(program, renderable.primitiveType, renderable.meshPartOffset, renderable.meshPartSize);
	}

	@Override
	public void end() {
		program.end();
	}

	@Override
	public int compareTo(Shader other) {
		return 0;
	}

	@Override
	public boolean canRender(Renderable renderable) {
		return true;
	}
}