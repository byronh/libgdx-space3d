package game.components;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import engine.artemis.Component;


public class Position implements Component {
	
	public Matrix4 world;
	
	public Position() {
		world = new Matrix4();
	}

	public Position(float x, float y, float z) {
		world = new Matrix4();
		world.setToTranslation(x, y, z);
	}
	
	public Position(Vector3 translation) {
		world = new Matrix4();
		world.setToTranslation(translation);
	}
	
	public Position(Matrix4 transform) {
		world = transform;
	}

	@Override
	public void reset() {
		world = null;
	}

}
