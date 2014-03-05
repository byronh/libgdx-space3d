package com.byronh.space3d.screens;

import com.badlogic.gdx.Screen;
import com.byronh.space3d.Space3DGame;

public abstract class AbstractScreen implements Screen {

    protected Space3DGame game;

    public AbstractScreen(Space3DGame game) {
        this.game = game;
        game.log("Instantiating " + this.getClass().getSimpleName());
    }

    public void pause() {
    	game.log("Pausing " + this.getClass().getSimpleName());
    }

    public void resume() {
    	game.log("Resuming " + this.getClass().getSimpleName());
    }

    public void dispose() {
    	game.log("Disposing " + this.getClass().getSimpleName());
    }
    
	public void render(float delta) {
	}

	public void resize(int width, int height) {
		game.log("Resizing " + this.getClass().getSimpleName() + " to (" + width + ", " + height + ")");
	}

	public void show() {
		game.log("Showing " + this.getClass().getSimpleName());
	}

	public void hide() {
		game.log("Hiding " + this.getClass().getSimpleName());
	}
}
