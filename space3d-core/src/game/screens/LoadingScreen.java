package game.screens;

import game.Space3DGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class LoadingScreen implements Screen {
	
	private Space3DGame game;
    private Stage stage;
    private float percent = 0f;

    public LoadingScreen(Space3DGame game) {
        this.game = game;
    }

    @Override
    public void show() {
    	
    	// Load assets for this loading screen
        //game.manager.load("data/loading_assets.pack", TextureAtlas.class);
        game.assets.finishLoading();
        
        stage = new Stage();
        
        // Load relevant assets for the next game screen here
        game.assets.load("ui/Holo-dark-hdpi.json", Skin.class);
        game.assets.load("texture-maps/venus.gif", Texture.class);
//        game.assets.load("texture-maps/earth1.jpg", Texture.class);
//        game.assets.load("texture-maps/planet1.png", Texture.class);
//        game.assets.load("texture-maps/galaxy_starfield.png", Texture.class);
        game.assets.load("models/ship.g3db", Model.class);
        
    }

    @Override
    public void render(float delta) {
    	
    	Gdx.gl20.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (game.assets.update()) {
        	game.setScreen(game.gameplayScreen);
        }
        
        percent = Interpolation.linear.apply(percent, game.assets.getProgress(), 0.1f);
        
        stage.act();
        stage.draw();
    }
    
    @Override
    public void dispose() {
    	stage.dispose();
    }

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
}
