package com.byronh.space3d.screens;

import com.badlogic.gdx.Screen;
import com.byronh.space3d.Space3DGame;

public abstract class AbstractScreen implements Screen {

    protected Space3DGame game;
    
    public AbstractScreen() {
    	
    }

    public AbstractScreen(Space3DGame game) {
        this.game = game;
        game.log("Switching to " + this.getClass().getSimpleName());
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
