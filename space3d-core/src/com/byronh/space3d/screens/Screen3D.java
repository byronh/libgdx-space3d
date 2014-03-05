package com.byronh.space3d.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.byronh.space3d.Space3DGame;
import com.byronh.space3d.graphics.MultiPassShader;
import com.byronh.space3d.graphics.ShaderLoader;


public class Screen3D extends AbstractScreen {

	/**
	 * Desktop only: Set this to an absolute path to load the shader files from
	 * an alternative location.
	 */
	final static String hotLoadFolder = null;
	/**
	 * Desktop only: Set this to an absolute path to save the generated shader
	 * files.
	 */
	final static String tempFolder = null;//"C:\\dev\\shaders";

	protected TestShaderProvider shaderProvider;
	protected FileHandle shaderRoot;
	protected ModelBatch shaderBatch;

	public Screen3D(Space3DGame game) {
		super(game);
	}

	public static class TestShaderProvider extends DefaultShaderProvider {

		public boolean error = false;
		public String name = "default";

		public void clear() {
			for (final Shader shader : shaders)
				shader.dispose();
			shaders.clear();
		}

		public boolean revert() {
			if (config.vertexShader == null || config.fragmentShader == null)
				return false;
			config.vertexShader = null;
			config.fragmentShader = null;
			clear();
			return true;
		}

		@Override
		public Shader getShader(Renderable renderable) {
			try {
				return super.getShader(renderable);
			} catch (Throwable e) {
				if (tempFolder != null && Gdx.app.getType() == ApplicationType.Desktop)
					Gdx.files.absolute(tempFolder).child(name + ".log.txt").writeString(e.getMessage(), false);
				if (!revert()) {
					Gdx.app.error("ShaderCollectionTest", e.getMessage());
					throw new GdxRuntimeException("Error creating shader, cannot revert to default shader", e);
				}
				error = true;
				Gdx.app.error("ShaderTest", "Could not create shader, reverted to default shader.", e);
				return super.getShader(renderable);
			}
		}

		@Override
		protected Shader createShader(Renderable renderable) {
			if (config.vertexShader != null && config.fragmentShader != null && tempFolder != null && Gdx.app.getType() == ApplicationType.Desktop) {
				String prefix = DefaultShader.createPrefix(renderable, config);
				Gdx.files.absolute(tempFolder).child(name + ".vertex.glsl").writeString(prefix + config.vertexShader, false);
				Gdx.files.absolute(tempFolder).child(name + ".fragment.glsl").writeString(prefix + config.fragmentShader, false);
			}
			BaseShader result = new MultiPassShader(renderable, config);
			if (tempFolder != null && Gdx.app.getType() == ApplicationType.Desktop)
				Gdx.files.absolute(tempFolder).child(name + ".log.txt").writeString(result.program.getLog(), false);
			return result;
		}
	}
	
	@Override
	public void show() {
		super.show();

		shaderProvider = new TestShaderProvider();
		shaderBatch = new ModelBatch(shaderProvider);

		shaderRoot = (hotLoadFolder != null && Gdx.app.getType() == ApplicationType.Desktop) ? Gdx.files.absolute(hotLoadFolder) : Gdx.files
				.internal("shaders");
	}
	
	public void setShader(String name) {
		shaderProvider.error = false;
		if (name.equals("<default>")) {
			shaderProvider.config.vertexShader = null;
			shaderProvider.config.fragmentShader = null;
			shaderProvider.name = "default";
		} else {
			ShaderLoader loader = new ShaderLoader(shaderRoot);
			shaderProvider.config.vertexShader = loader.load(name+".glsl:VS");
			shaderProvider.config.fragmentShader = loader.load(name+".glsl:FS");
			shaderProvider.name = name;
		}
		shaderProvider.clear();
	}

	@Override
	public void dispose() {
		shaderBatch.dispose();
		shaderBatch = null;
		shaderProvider = null;
		super.dispose();
	}

}
