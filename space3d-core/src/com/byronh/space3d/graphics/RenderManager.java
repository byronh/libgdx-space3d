package com.byronh.space3d.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Keys;
import com.byronh.space3d.util.TextFileLoader;


public class RenderManager {
	
	private static final String SHADER_PATH = "com/byronh/space3d/shaders";
	private static int MAX_FRAMEBUFFERS = 10;

	private ObjectMap<String, ShaderProgram> shaders = new ObjectMap<String, ShaderProgram>();
	private ObjectMap<String, FrameBuffer> buffers = new ObjectMap<String, FrameBuffer>();
	private Array<String> openedBuffers = new Array<String>(true, MAX_FRAMEBUFFERS);

	private ShaderProgram currentShader = null;
	private String currentShaderName = null;
	private int currentTextureId = 0;
	private AssetManager am;
	private SpriteBatch spriteBatch = new SpriteBatch();
	
	public RenderManager(AssetManager assetManager) {
		am = assetManager;
		ShaderProgram.pedantic = false;
		am.setLoader(String.class, ".glsl", new TextFileLoader(new InternalFileHandleResolver()));
	}
	
	public void beginRendering() {
		spriteBatch.begin();
	}
	
	public void endRendering() {
		spriteBatch.end();
	}
	
	/**
	 * Call this to start rendering using given shader.
	 * @param shaderName - a shader with this identifier must be loaded earlier
	 * @return this ShaderProgram for chaining
	 */
	public ShaderProgram beginShader(String shaderName) {
		if (currentShader != null)
			throw new IllegalArgumentException("Previous shader '" + currentShaderName + "' not finished! Call end() before another begin().");
		
		ShaderProgram res = getShader(shaderName);
		if (res != null) {
			currentShader = res;
			currentShaderName = shaderName;
			currentTextureId = 0;
			res.begin();
		}
		else {
			throw new IllegalArgumentException("Shader '" + shaderName + "' not found!");
		}
		return res;
	}
	
	/**
	 * Returns given shader from ShaderManager.
	 * @param name - shader identifier
	 * @return requested shader
	 */
	public ShaderProgram getShader(String name) {
		if (shaders.containsKey(name))
			return shaders.get(name);
		throw new GdxRuntimeException("No shader named '" + name + "' in ShaderManager!");
	}
	
	/**
	 * Returns current shader from ShaderManager.
	 * @return requested shader
	 */
	public ShaderProgram getCurrentShader() {
		if (currentShader != null)
			return currentShader;
		throw new GdxRuntimeException("No current shader set in ShaderManager!");
	}

	/**
	 * Call this to finish rendering using current shader.
	 */
	public void endShader() {
		if (currentShader != null) {
			currentShader.end();
			currentShader = null;
			currentShaderName = null;
		}
	}
	
	public void createFB(String bufferName) {
		FrameBuffer fb = buffers.get(bufferName);
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		if (fb == null || fb.getWidth() != width || fb.getHeight() != height) {
		    fb = new FrameBuffer(Format.RGBA8888, width, height, true);
		}
		buffers.put(bufferName, fb);
	}
	
	public void beginFB(String bufferName, Color clearColor) {
		FrameBuffer fb = buffers.get(bufferName);
		if (fb == null) {
			throw new IllegalArgumentException("FrameBuffer must not be null!");
		}
        fb.begin();
        Gdx.graphics.getGL20().glViewport(0, 0, fb.getWidth(), fb.getHeight());
        Gdx.graphics.getGL20().glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.graphics.getGL20().glEnable(GL20.GL_TEXTURE_2D);
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.graphics.getGL20().glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    	openedBuffers.add(bufferName);
    }
	
	/**
	 * Renders given framebuffer onto given Mesh.
	 * @param fb - FrameBuffer to render
	 * @param out - a Mesh to render the framebuffer to
	 * @param textureUniformName - the name of the texture2d uniform parameter in the fragment shader
	 * @param textureId - which texture number should be used
	 */
	public void renderFB(String bufferName, Mesh out, String textureUniformName, int textureId) {
		FrameBuffer fb = buffers.get(bufferName);
		if (fb != null) {
			Texture t = fb.getColorBufferTexture();
			t.bind(textureId);
	        spriteBatch.setShader(getCurrentShader());
	        spriteBatch.draw(t, 0f, 0f);
		}
	}
	
	public void endFB() {
    	if (openedBuffers.size == 0) {
			throw new IllegalArgumentException("No framebuffers to end!");
		}
    	FrameBuffer fb = buffers.get(openedBuffers.pop());
    	if (fb == null) {
			throw new IllegalArgumentException("FrameBuffer must not be null!");
		}
        fb.end();
	}
	
	public void addShader(String shaderName) {
		FileHandle vert = Gdx.files.classpath(SHADER_PATH + "/" + shaderName + ".vert.glsl");
		FileHandle frag = Gdx.files.classpath(SHADER_PATH + "/" + shaderName + ".frag.glsl");
		String vertPath = vert.path();
		String fragPath = frag.path();
		
		if (!vert.exists()) {
			throw new GdxRuntimeException("ShaderManager: shader '" + vertPath + "' does not exist!");
		}
		if (!frag.exists()) {
			throw new GdxRuntimeException("ShaderManager: shader '" + fragPath + "' does not exist!");
		}
		
		if (am.isLoaded(vertPath))
			am.unload(vertPath);
		if (am.isLoaded(fragPath))
			am.unload(fragPath);
		
		am.load(vertPath, String.class);
		am.load(fragPath, String.class);
		am.finishLoading();
		
		String vertSource = am.get(vertPath, String.class);
		String fragSource = am.get(fragPath, String.class);
		
		init(shaderName, vertSource, fragSource);
	}
	
	
	
	public boolean init(String key, String vert, String frag) {
		ShaderProgram sp = new ShaderProgram(vert, frag);
		if (!sp.isCompiled()) {
			Gdx.app.log("ShaderManager", "Error while loading shader '" + key + "':\n" + sp.getLog());
			Gdx.app.log("ShaderManager", "--------------------------------");
			Gdx.app.log("ShaderManager", "Vertex shader source: \n" + vert);
			Gdx.app.log("ShaderManager", "--------------------------------");
			Gdx.app.log("ShaderManager", "Fragment shader source: \n" + frag);
			Gdx.app.log("ShaderManager", "--------------------------------");
			return false;
		}
		shaders.put(key, sp);
		Gdx.app.log("ShaderManager", "Shader '" + key + "' loaded");
		return true;
	}
	
	public void reload() {
		float t = System.currentTimeMillis();
		Keys<String> keys = shaders.keys();
		while (keys.hasNext) {
			String key = (String)keys.next();
			addShader(key);
		}
		Gdx.app.log("ShaderManager", "Shaders reloaded in " + (System.currentTimeMillis() - t) + "ms");
	}
	
	/**
	 * @return current (free) texture id
	 */
	public int getCurrentTextureId() {
		return currentTextureId++;
	}
	
	/** Sets the given attribute
	 * 
	 * @param name the name of the attribute
	 * @param value1 the first value
	 * @param value2 the second value
	 * @param value3 the third value
	 * @param value4 the fourth value */
	public ShaderProgram setAttributef(java.lang.String name, float value1, float value2,
			float value3, float value4) {
		if (currentShader != null) {
			currentShader.setAttributef(name, value1, value2, value3, value4);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	public ShaderProgram setUniform1fv(java.lang.String name, float[] values, int offset,
			int length) {
		if (currentShader != null) {
			currentShader.setUniform1fv(name, values, offset, length);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	public ShaderProgram setUniform2fv(java.lang.String name, float[] values, int offset,
			int length) {
		if (currentShader != null) {
			currentShader.setUniform2fv(name, values, offset, length);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	public ShaderProgram setUniform3fv(java.lang.String name, float[] values, int offset,
			int length) {
		if (currentShader != null) {
			currentShader.setUniform3fv(name, values, offset, length);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	public ShaderProgram setUniform4fv(java.lang.String name, float[] values, int offset,
			int length) {
		if (currentShader != null) {
			currentShader.setUniform4fv(name, values, offset, length);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #endShader()} block.
	 * 
	 * @param name the name of the uniform
	 * @param value the value */
	public ShaderProgram setUniformf(java.lang.String name, float value) {
		if (currentShader != null) {
			currentShader.setUniformf(name, value);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #endShader()} block.
	 * 
	 * @param name the name of the uniform
	 * @param value1 the first value
	 * @param value2 the second value */
	public ShaderProgram setUniformf(java.lang.String name, float value1, float value2) {
		if (currentShader != null) {
			currentShader.setUniformf(name, value1, value2);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #endShader()} block.
	 * 
	 * @param name the name of the uniform
	 * @param value1 the first value
	 * @param value2 the second value
	 * @param value3 the third value */
	public ShaderProgram setUniformf(java.lang.String name, float value1, float value2,
			float value3) {
		if (currentShader != null) {
			currentShader.setUniformf(name, value1, value2, value3);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #endShader()} block.
	 * 
	 * @param name the name of the uniform
	 * @param value1 the first value
	 * @param value2 the second value
	 * @param value3 the third value
	 * @param value4 the fourth value */
	public ShaderProgram setUniformf(java.lang.String name, float value1, float value2,
			float value3, float value4) {
		if (currentShader != null) {
			currentShader.setUniformf(name, value1, value2, value3, value4);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #endShader()} block.
	 * 
	 * @param name the name of the uniform
	 * @param value the value */
	public ShaderProgram setUniformi(java.lang.String name, int value) {
		if (currentShader != null) {
			currentShader.setUniformi(name, value);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #endShader()} block.
	 * 
	 * @param name the name of the uniform
	 * @param value1 the first value
	 * @param value2 the second value */
	public ShaderProgram setUniformi(java.lang.String name, int value1, int value2) {
		if (currentShader != null) {
			currentShader.setUniformi(name, value1, value2);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #endShader()} block.
	 * 
	 * @param name the name of the uniform
	 * @param value1 the first value
	 * @param value2 the second value
	 * @param value3 the third value */
	public ShaderProgram setUniformi(java.lang.String name, int value1, int value2, int value3) {
		if (currentShader != null) {
			currentShader.setUniformi(name, value1, value2, value3);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #endShader()} block.
	 * 
	 * @param name the name of the uniform
	 * @param value1 the first value
	 * @param value2 the second value
	 * @param value3 the third value
	 * @param value4 the fourth value */
	public ShaderProgram setUniformi(java.lang.String name, int value1, int value2, int value3,
			int value4) {
		if (currentShader != null) {
			currentShader.setUniformi(name, value1, value2, value3, value4);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform matrix with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #endShader()} block.
	 * 
	 * @param name the name of the uniform
	 * @param matrix the matrix */
	public ShaderProgram setUniformMatrix(java.lang.String name, Matrix3 matrix) {
		if (currentShader != null) {
			currentShader.setUniformMatrix(name, matrix);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform matrix with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #endShader()} block.
	 * 
	 * @param name the name of the uniform
	 * @param matrix the matrix
	 * @param transpose whether the uniform matrix should be transposed */
	public ShaderProgram setUniformMatrix(java.lang.String name, Matrix3 matrix,
			boolean transpose) {
		if (currentShader != null) {
			currentShader.setUniformMatrix(name, matrix, transpose);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform matrix with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #endShader()} block.
	 * 
	 * @param name the name of the uniform
	 * @param matrix the matrix */
	public ShaderProgram setUniformMatrix(java.lang.String name, Matrix4 matrix) {
		if (currentShader != null) {
			currentShader.setUniformMatrix(name, matrix);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform matrix with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #endShader()} block.
	 * 
	 * @param name the name of the uniform
	 * @param matrix the matrix
	 * @param transpose whether the matrix should be transposed */
	public ShaderProgram setUniformMatrix(java.lang.String name, Matrix4 matrix,
			boolean transpose) {
		if (currentShader != null) {
			currentShader.setUniformMatrix(name, matrix, transpose);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}
	
	/** Sets the uniform with the given name, automatically binding the texture to a number. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #endShader()} block.
	 * 
	 * @param name the name of the uniform */
	public ShaderProgram setUniformTexture(String name, Texture value) {
		if (currentShader != null) {
			int texId = getCurrentTextureId();
			value.bind(texId);
			currentShader.setUniformi(name, texId);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/*
	 * public void setVertexAttribute(java.lang.String name, int size, int type,
	 * boolean normalize, int stride, java.nio.Buffer buffer) { if
	 * (currentShader != null) { currentShader.setVertexAttribute(name, size,
	 * type, normalize, stride, buffer); } else throw new
	 * IllegalArgumentException("Can't set uniform before calling begin()!"); }
	 */
	

	/** Sets the vertex attribute with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #endShader()} block.
	 * 
	 * @param name the attribute name
	 * @param size the number of components, must be >= 1 and <= 4
	 * @param type the type, must be one of GL20.GL_BYTE, GL20.GL_UNSIGNED_BYTE, GL20.GL_SHORT,
	 *           GL20.GL_UNSIGNED_SHORT,GL20.GL_FIXED, or GL20.GL_FLOAT. GL_FIXED will not work on the desktop
	 * @param normalize whether fixed point data should be normalized. Will not work on the desktop
	 * @param stride the stride in bytes between successive attributes
	 * @param offset byte offset into the vertex buffer object bound to GL20.GL_ARRAY_BUFFER. */
	public ShaderProgram setVertexAttribute(java.lang.String name, int size, int type,
			boolean normalize, int stride, int offset) {
		if (currentShader != null) {
			currentShader.setVertexAttribute(name, size, type, normalize,
					stride, offset);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

}
