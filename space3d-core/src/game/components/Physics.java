package game.components;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;

import engine.artemis.Component;


public class Physics implements Component {

	private boolean active = false;
	private float mass;
	private Vector3 hitboxExtents;

	private btRigidBody body;

	private btDefaultMotionState motionState;

	public void init(float mass, Vector3 hitboxExtents) {
		this.mass = mass;
		this.hitboxExtents = hitboxExtents;
		active = true;
	}

	public boolean isActive() {
		return active;
	}

	public float getMass() {
		return mass;
	}

	public Vector3 getExtents() {
		return hitboxExtents;
	}

	public btRigidBody getBody() {
		return body;
	}

	public void setBody(btRigidBody body) {
		this.body = body;
	}

	public btDefaultMotionState getMotionState() {
		return motionState;
	}

	public void setMotionState(btDefaultMotionState motionState) {
		this.motionState = motionState;
	}

	@Override
	public void reset() {
		// TODO Dispose C++ objects
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
