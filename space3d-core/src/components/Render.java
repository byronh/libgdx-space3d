package components;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;

import engine.artemis.Component;

public class Render implements Component {

	public ModelInstance instance;
	public Environment environment;
	public Shader shader;

	@Override
	public void reset() {
		instance = null;
		environment = null;
		shader = null;
	}

}
