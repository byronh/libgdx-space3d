package game.components;

import com.badlogic.gdx.math.Vector3;

import engine.artemis.Component;


public class Movement implements Component {
	
	public Vector3 velocity;
	public Vector3 acceleration;
	
	public Movement() {
		velocity = new Vector3();
		acceleration = new Vector3();
	}

	@Override
	public void reset() {
		velocity = null;
		acceleration = null;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
