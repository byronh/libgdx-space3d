package com.byronh.space3d.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.byronh.space3d.Space3DGame;


public class LoadingScreen extends AbstractScreen {

    private Stage stage;
    private float percent = 0f;

    public LoadingScreen(Space3DGame game) {
        super(game);
    }

    @Override
    public void show() {
    	
    	// Load assets for this loading screen
        //game.manager.load("data/loading_assets.pack", TextureAtlas.class);
        game.manager.finishLoading();
        
        stage = new Stage();
        
        // Load relevant assets for the next game screen here
        game.manager.load("texture-maps/starscape.png", Texture.class);
    }

    @Override
    public void render(float delta) {
    	
    	Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (game.manager.update()) {
        	game.setScreen(new GameplayScreen(game));
        }
        
        percent = Interpolation.linear.apply(percent, game.manager.getProgress(), 0.1f);
        
        stage.act();
        stage.draw();
    }

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
}
