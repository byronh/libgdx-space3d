package components;

import zjunk.Component;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;


public class RenderComponent implements Component {
	
	public ModelInstance instance;
	public Environment environment;
	public Shader shader;

	@Override
	public String debug() {
		return instance.toString() + "," + environment.toString() + "," + shader.toString();
	}

}
