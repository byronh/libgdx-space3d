package components;

import com.badlogic.gdx.math.Vector3;

import comparchitecture.Component;


public class MovementComponent implements Component {
	
	public Vector3 velocity = new Vector3();
	public Vector3 acceleration = new Vector3();

	@Override
	public String debug() {
		return "Velocity: " + velocity.toString() + ", Acceleration: " + acceleration.toString() + "\n";
	}

}
