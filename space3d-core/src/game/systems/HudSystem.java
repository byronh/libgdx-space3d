package game.systems;

import game.utils.Renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class HudSystem extends BaseUISystem {
	
	private Renderer renderer;
	private AssetManager assets;
	
	private Skin skin;
	private TextButton button;
	
	public HudSystem(Renderer renderer, AssetManager assetManager) {
		super();
		this.renderer = renderer;
		assets = assetManager;
	}
	
	@Override
	public void initialize() {
		skin = assets.get("ui/Holo-dark-hdpi.json", Skin.class);
		button = new TextButton("Click me", skin, "default");
		button.setPosition(10, Gdx.graphics.getHeight() - 100);
		button.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				button.setText("Clicked!");
			}
		});
		root.addActor(button);
		
		renderer.stage.addActor(root);
	}

	@Override
	protected void processSystem() {
		
	}

}
