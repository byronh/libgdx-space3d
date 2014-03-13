package game.components;

import com.badlogic.gdx.graphics.PerspectiveCamera;

import engine.artemis.Component;


public class Camera implements Component {
	
	public PerspectiveCamera perspective;

	@Override
	public void reset() {
		perspective = null;
	}

}
