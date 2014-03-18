package game.components;

import com.badlogic.gdx.math.Vector3;

import engine.artemis.Component;

public class Steering implements Component {
	
	public float maxVelocity;
	public Vector3 targetPos;
	public Vector3 steeringForce;
	
	public Steering() {
		reset();
	}

	@Override
	public void reset() {
		targetPos = new Vector3();
		maxVelocity = 1f;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

}
