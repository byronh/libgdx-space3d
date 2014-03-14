package game.components;

import engine.artemis.Component;


public class Select implements Component {
	
	public boolean selected = false;

	@Override
	public void reset() {
		selected = false;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
