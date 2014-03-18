package game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import engine.artemis.ComponentMapper;
import engine.artemis.Entity;
import engine.artemis.Filter;
import engine.artemis.systems.EntitySystem;
import game.components.Physics;
import game.components.Render;
import game.components.Steering;

public class SteeringSystem extends EntitySystem {

	ComponentMapper<Physics> pm;
	ComponentMapper<Steering> sm;

	private Vector3 tempVector = new Vector3();
	private Vector3 bodyTranslation = new Vector3();
	private Matrix4 bodyTransform = new Matrix4();
	private Quaternion tempQuat = new Quaternion();
	private Vector3 desiredVelocity = new Vector3();
	private Vector3 currentVelocity = new Vector3();

	@SuppressWarnings("unchecked")
	public SteeringSystem() {
		super(Filter.allComponents(Physics.class, Steering.class));
	}

	@Override
	public void initialize() {
		pm = world.getMapper(Physics.class);
		sm = world.getMapper(Steering.class);
	}

	@Override
	protected void processEntities(Array<Entity> entities) {
		for (Entity e : entities) {
			Physics physics = pm.get(e);
			Steering steering = sm.get(e);
			
			bodyTransform = physics.getBody().getWorldTransform();
			bodyTransform.getTranslation(bodyTranslation);
			
			// Move the ship
			if (bodyTranslation.dst(steering.targetPos) > 10) {
				currentVelocity = physics.getBody().getLinearVelocity();
				desiredVelocity = steering.targetPos.cpy().sub(bodyTranslation)
						.nor().scl(steering.maxVelocity);
				steering.steeringForce = desiredVelocity.sub(currentVelocity);
				physics.getBody().setGravity(steering.steeringForce);
			} else {
				physics.getBody().setGravity(Vector3.Zero);
				physics.getBody().setLinearVelocity(Vector3.Zero);
			}

			// Rotate the ship
			Vector3 rotationAxis = currentVelocity.cpy().crs(desiredVelocity);
			float rotationAngle = currentVelocity.dot(desiredVelocity);
			Gdx.app.log("", rotationAngle + "");
			
			tempVector.set(rotationAxis).nor().scl(rotationAngle*world.getDelta());
			physics.getBody().setAngularVelocity(tempVector);
			
//			tempQuat = new Quaternion(rotationAxis, rotationAngle);
//			Quaternion bodyQuat = physics.getBody().getOrientation();

		}
	}

}
