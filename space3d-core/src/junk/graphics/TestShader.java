package junk.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;


public class TestShader implements Shader {
	
	private String vertex, fragment;
	
	protected ShaderProgram program;

	protected int u_projTrans;
	protected int u_viewTrans;
	protected int u_worldTrans;
	
	protected static String shaderDir = "b3d/shaders";

	public TestShader(String vertex, String fragment) {
		this.vertex = vertex;
		this.fragment = fragment;
	}

	@Override
	public void init() {
		
		String vert = Gdx.files.classpath(shaderDir + "/" + vertex + ".vert.glsl").readString();
		String frag = Gdx.files.classpath(shaderDir + "/" + fragment + ".frag.glsl").readString();
		
		program = new ShaderProgram(vert, frag);
		if (program.isCompiled()) {
			Gdx.app.log(this.getClass().getSimpleName(), "Compile successful.");
		} else {
			Gdx.app.log(this.getClass().getSimpleName(), "Compile failed.");
			throw new GdxRuntimeException(program.getLog());
		}
		
		u_projTrans = program.getUniformLocation("u_projTrans");
		u_viewTrans = program.getUniformLocation("u_viewTrans");
		u_worldTrans = program.getUniformLocation("u_worldTrans");
	}
	
	@Override
	public void dispose() {
		program.dispose();
	}

	@Override
	public void begin(Camera camera, RenderContext context) {
		program.begin();
		program.setUniformMatrix(u_projTrans, camera.combined);
		program.setUniformMatrix(u_viewTrans, camera.view);
//		context.setDepthTest(GL20.GL_LEQUAL);
//		context.setCullFace(GL20.GL_BACK);
	}

	@Override
	public void render(Renderable renderable) {
		program.setUniformMatrix(u_worldTrans, renderable.worldTransform);
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
	public boolean canRender(Renderable instance) {
		return true;
	}

}
