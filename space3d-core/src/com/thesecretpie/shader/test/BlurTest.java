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

public class BlurTest extends InputAdapter implements ApplicationListener {
	
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
		
		ShaderProgram.pedantic = true;
		sm = new ShaderManager("shaders", am);
		sm.add("vert_blur", "default.vert", "vertical_blur.frag");
		sm.add("horiz_blur", "default.vert", "horizontal_blur.frag");
		sm.createFB("base_fb");
		sm.createFB("horiz_fb");
		
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
		
		sm.beginFB("base_fb");
			sm.begin("empty");
			sm.setUniformMatrix("u_worldView", combined);
			cube.render(sm.getCurrent(), GL20.GL_TRIANGLES);
			sm.end();
		sm.endFB();
		
		sm.beginFB("horiz_fb");
			sm.begin("horiz_blur");
			sm.renderFB("base_fb");
			sm.end();
		sm.endFB();
		
		sm.begin("vert_blur");
		//sm.begin("default");
		sm.renderFB("horiz_fb");
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
