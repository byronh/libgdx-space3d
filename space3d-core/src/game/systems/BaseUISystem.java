package game.systems;

import com.badlogic.gdx.scenes.scene2d.Group;

import engine.artemis.systems.VoidEntitySystem;

public abstract class BaseUISystem extends VoidEntitySystem {
	
	public final Group root = new Group();

	public void show() {
		root.setVisible(true);
	}
	
	public void hide() {
		root.setVisible(false);
	}
	
	public void toggleVisibility() {
		root.setVisible(!root.isVisible());
	}

}
