package components;

import com.badlogic.gdx.math.Matrix4;
import comparchitecture.Component;


public class WorldComponent extends Component {

	public Matrix4 worldTransform = new Matrix4();

	@Override
	public String debug() {
		return worldTransform.toString();
	}

}
