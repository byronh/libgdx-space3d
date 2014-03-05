package com.thesecretpie.shader.test;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.thesecretpie.shader.ShaderManager;

public class FramebufferTest extends InputAdapter implements ApplicationListener {
	
	ShaderManager sm;
	Mesh cube;
	AssetManager am = new AssetManager();
	Matrix4 projection = new Matrix4();
	Matrix4 view = new Matrix4();
	Matrix4 model = new Matrix4();
	Matrix4 combined = new Matrix4();
	Vector3 axis = new Vector3(1, 0, 1).nor();
	float angle = 45;

	@Override
	public void create() {
		Gdx.input.setInputProcessor(this);
		
		ShaderProgram.pedantic = false;
		sm = new ShaderManager("assets/shaders", am);
		sm.add("bloom", "default.vert", "bloom.frag");
		sm.add("vert_blur", "default.vert", "vertical_blur.frag");
		sm.add("horiz_blur", "default.vert", "horizontal_blur.frag");
		sm.add("greenmess", "default.vert", "greenmess.frag");
		sm.add("multiply", "default.vert", "multiply_textures.frag");
		sm.createFB("fb1");
		sm.createFB("fb2");
		sm.createFB("fb3");
		sm.createFB("fb4");
		sm.createFB("fb5");
		sm.createFB("fb6");
		
		cube = Shapes.genCube();
	}

	@Override
	public void dispose() {
		sm.dispose();
		am.dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		angle += Gdx.graphics.getDeltaTime() * 40.0f;
		float aspect = Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight();
		projection.setToProjection(1.0f, 20.0f, 60.0f, aspect);
		view.idt().trn(0, 0, -2.0f);
		model.setToRotation(axis, angle);
		combined.set(projection).mul(view).mul(model);

		Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		sm.beginFB("fb1");
			sm.begin("empty");
			sm.setUniformMatrix("u_worldView", combined);
			cube.render(sm.getCurrent(), GL20.GL_TRIANGLES);
			sm.end();
		sm.endFB();
		
		sm.beginFB("fb2");
			sm.begin("greenmess");
			sm.renderFB("fb1");
			sm.end();
		sm.endFB();
		
		sm.beginFB("fb3");
			sm.begin("horiz_blur");
			sm.renderFB("fb2");
			sm.end();
		sm.endFB();
		
		sm.beginFB("fb4");
			sm.begin("vert_blur");
			sm.renderFB("fb3");
			sm.end();
		sm.endFB();
		
		sm.beginFB("fb5");
			sm.begin("multiply");
			sm.setUniformMatrix("u_worldView", combined);
			sm.setUniformTexture("u_texture1", sm.getFBTexture("fb1"));
			sm.renderFB("fb4", "u_texture2", sm.getCurrentTextureId());
			sm.end();
		sm.endFB();
		
		sm.begin("bloom");
		sm.renderFB("fb5");
		sm.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.R:
			sm.reload(); break;
		}
		return false;
	}

}
