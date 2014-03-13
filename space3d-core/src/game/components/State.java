package game.components;

import engine.artemis.Component;


public class State implements Component {
	
	public boolean selected = false;

	@Override
	public void reset() {
		selected = false;
	}

}
