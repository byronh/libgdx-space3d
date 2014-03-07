package com.byronh.space3d.screens;

import com.badlogic.gdx.Screen;
import com.byronh.space3d.Space3DGame;

public abstract class AbstractScreen implements Screen {

    protected Space3DGame game;

    public AbstractScreen(Space3DGame game) {
        this.game = game;
    }

    public void pause() {
    }

    public void resume() {
    }

    public void dispose() {
    }
    
	public void render(float delta) {
	}

	public void resize(int width, int height) {
		game.log("Resizing " + this.getClass().getSimpleName() + " to (" + width + ", " + height + ")");
	}

	public void show() {
	}

	public void hide() {
	}
}
