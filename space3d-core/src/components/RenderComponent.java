package components;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;
import comparchitecture.Component;


public class RenderComponent extends Component {
	
	public ModelInstance instance;
	public Environment lights;
	public Shader shader;

	@Override
	public String debug() {
		return instance.toString() + "," + lights.toString() + "," + shader.toString();
	}

}
