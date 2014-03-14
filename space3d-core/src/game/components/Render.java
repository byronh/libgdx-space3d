package game.components;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;

import engine.artemis.Component;

public class Render implements Component {

	public ModelInstance instance;
	public Shader shader;

	@Override
	public void reset() {
		instance = null;
		shader = null;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
