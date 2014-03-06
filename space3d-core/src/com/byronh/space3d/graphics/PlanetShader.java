package com.byronh.space3d.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;


/**
 * See: http://blog.xoppa.com/creating-a-shader-with-libgdx
 * 
 * @author Xoppa
 */
public class PlanetShader implements Shader {

//	public static class DoubleColorAttribute extends Attribute {
//
//		public final static String DiffuseUVAlias = "diffuseUVColor";
//		public final static long DiffuseUV = register(DiffuseUVAlias);
//
//		public final Color color1 = new Color();
//		public final Color color2 = new Color();
//
//		public DoubleColorAttribute(long type, Color c1, Color c2) {
//			super(type);
//			color1.set(c1);
//			color2.set(c2);
//		}
//
//		@Override
//		public Attribute copy() {
//			return new DoubleColorAttribute(type, color1, color2);
//		}
//
//		@Override
//		protected boolean equals(Attribute other) {
//			DoubleColorAttribute attr = (DoubleColorAttribute) other;
//			return type == other.type && color1.equals(attr.color1) && color2.equals(attr.color2);
//		}
//	}

	ShaderProgram program;
	Camera camera;
	RenderContext context;
	
	int u_projTrans;
	int u_worldTrans;
	int u_diffuseTexture;

	private String data = "com/byronh/space3d/shaders";

	@Override
	public void init() {
		String vert = Gdx.files.classpath(data + "/test.vert").readString();
		String frag = Gdx.files.classpath(data + "/test.frag").readString();
		program = new ShaderProgram(vert, frag);
		if (!program.isCompiled())
			throw new GdxRuntimeException(program.getLog());
		u_projTrans = program.getUniformLocation("u_projTrans");
		u_worldTrans = program.getUniformLocation("u_worldTrans");
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
		context.setDepthTest(GL20.GL_LEQUAL);
		context.setCullFace(GL20.GL_BACK);
	}

	@Override
	public void render(Renderable renderable) {
		program.setUniformMatrix(u_worldTrans, renderable.worldTransform);
//		DoubleColorAttribute attribute = ((DoubleColorAttribute) renderable.material.get(DoubleColorAttribute.DiffuseUV));
//		program.setUniformf(u_colorU, attribute.color1.r, attribute.color1.g, attribute.color1.b);
//		program.setUniformf(u_colorV, attribute.color2.r, attribute.color2.g, attribute.color2.b);
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
		return true;//renderable.material.has(DoubleColorAttribute.DiffuseUV);
	}
}