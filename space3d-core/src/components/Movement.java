package components;

import com.badlogic.gdx.math.Vector3;

import engine.artemis.Component;


public class Movement implements Component {
	
	public Vector3 velocity = new Vector3();
	public Vector3 acceleration = new Vector3();

	@Override
	public void reset() {
		velocity.set(0, 0, 0);
		acceleration.set(0, 0, 0);
	}

}
