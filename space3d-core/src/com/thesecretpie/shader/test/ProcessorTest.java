package com.thesecretpie.shader.test;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.thesecretpie.shader.Processor;
import com.thesecretpie.shader.ShaderManager;

public class ProcessorTest extends InputAdapter implements ApplicationListener {
	
	ShaderManager sm;
	AssetManager am = new AssetManager();
	Matrix4 projection = new Matrix4();
	Matrix4 view = new Matrix4();
	Matrix4 model = new Matrix4();
	Matrix4 combined = new Matrix4();
	Vector3 axis = new Vector3(1, 0, 1).nor();
	float angle = 45;
	
	SpriteBatch batch;
	Processor canvas;
	Texture tex;
	Color currentColor;
	Vector2 mousePos = new Vector2(), prevMousePos = new Vector2();
	float brushSize = 10f;
	int frame = 0;
	boolean smear = false;
	boolean renderHelp = true;
	
	BitmapFont font;

	@Override
	public void create() {
		Gdx.input.setInputProcessor(this);
		
		ShaderProgram.pedantic = false;
		sm = new ShaderManager("assets/shaders", am);
		sm.add("noise", "processor.vert", "noise.frag");
		sm.add("brush", "processor.vert", "brush.frag");
		sm.add("smear", "processor.vert", "smear.frag");
		
		canvas = new Processor(sm, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Format.RGBA8888, false, false);
		//canvas.run("noise");

		currentColor = Shapes.randomColor();
		mousePos.set(-99, -99);
		batch = new SpriteBatch();
		font = new BitmapFont();
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
		
		Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		canvas.setUniform("u_brush_col", currentColor);
		canvas.setUniform("u_brush_pos", mousePos);
		canvas.setUniform("u_prev_brush_pos", prevMousePos);
		canvas.setUniform("u_brush_size", brushSize);
		canvas.run("brush");
		
		if (smear) {
			canvas.setUniform("u_sample_count", MathUtils.random(8) + 4);
			canvas.run("smear");
		}
			
		canvas.renderDefault();

		if (renderHelp) {
			batch.begin();
			font.draw(batch, "Draw with mouse", 10, 80);
			font.draw(batch, "Mouse wheel changes brush size", 10, 65);
			font.draw(batch, "Press S to toggle Wet Paint", 10, 50);
			font.draw(batch, "Press C to clear", 10, 35);
			font.draw(batch, "Press H to toggle help text", 10, 20);
			batch.end();
		}
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
		case Keys.D:
			canvas.dump("../processor.png");
		case Keys.G:
			int x = MathUtils.random(1, canvas.getWidth());
			int y = MathUtils.random(1, canvas.getHeight());
			Color col = canvas.getValue(x, y);
			System.out.println(x + ", " + y + ": " + col.toString());
		case Keys.Z:
			brushSize = MathUtils.clamp(brushSize + 2, 2, 20); break;
		case Keys.X:
			brushSize = MathUtils.clamp(brushSize - 2, 2, 20); break;
		case Keys.S:
			smear = !smear; break;
		case Keys.H:
			renderHelp = !renderHelp; break;
		case Keys.C:
			canvas.clear(); break;
		}
		return false;
	}
	
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		currentColor = Shapes.randomColor();
		prevMousePos.set(mousePos);
		mousePos.set(screenX, Gdx.graphics.getHeight() - screenY);
		return false;
	}

	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		mousePos.set(-99, -99);
		prevMousePos.set(mousePos);
		return false;
	}

	public boolean touchDragged (int screenX, int screenY, int pointer) {
		//mousePos.set(2*screenX/Gdx.graphics.getWidth()-1, 2*screenY/Gdx.graphics.getHeight()-1);
		prevMousePos.set(mousePos);
		mousePos.set(screenX, Gdx.graphics.getHeight() - screenY);
		return false;
	}
	
	public boolean scrolled (int amount) {
		brushSize = MathUtils.clamp(brushSize - amount, 2, 20);
		return false;
	}

}
