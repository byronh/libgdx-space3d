package components;

import zjunk.Component;

import com.badlogic.gdx.math.Matrix4;


public class WorldComponent extends Matrix4 implements Component {

	private static final long serialVersionUID = 1L;

	@Override
	public String debug() {
		return this.toString();
	}

}
