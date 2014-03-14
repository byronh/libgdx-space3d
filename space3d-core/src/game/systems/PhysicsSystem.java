package game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Array;

import engine.artemis.ComponentMapper;
import engine.artemis.Entity;
import engine.artemis.Filter;
import engine.artemis.systems.EntitySystem;
import engine.physics.DebugDrawer;
import game.components.Physics;
import game.components.Position;


public class PhysicsSystem extends EntitySystem {

	ComponentMapper<Position> positionMapper;
	ComponentMapper<Physics> physicsMapper;

	private Camera cam;

	private btCollisionConfiguration collisionConfiguration;
	private btCollisionDispatcher dispatcher;
	private btBroadphaseInterface broadphase;
	private btConstraintSolver solver;
	private btDynamicsWorld collisionWorld;

	private DebugDrawer debugDrawer = null;

	private Vector3 tempVector = new Vector3();

	@SuppressWarnings("unchecked")
	public PhysicsSystem(Camera camera) {
		super(Filter.allComponents(Position.class, Physics.class));
		cam = camera;
	}

	@Override
	public void initialize() {

		positionMapper = world.getMapper(Position.class);
		physicsMapper = world.getMapper(Physics.class);

		// Create bullet world
		Bullet.init();
		collisionConfiguration = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfiguration);
		broadphase = new btDbvtBroadphase();
		solver = new btSequentialImpulseConstraintSolver();
		collisionWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
		collisionWorld.setGravity(new Vector3());

		// TODO refactor debug into its own system
		collisionWorld.setDebugDrawer(debugDrawer = new DebugDrawer());

		debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawWireframe);

	}

	@Override
	protected void inserted(Entity e) {
		Position position = positionMapper.get(e);
		Physics physics = physicsMapper.get(e);
		
		btCollisionShape shape = new btBoxShape(physics.getExtents());
		btRigidBodyConstructionInfo info = new btRigidBodyConstructionInfo(physics.getMass(), null, shape, Vector3.Zero);
		btDefaultMotionState motionState = new btDefaultMotionState();
		
		info.setLinearSleepingThreshold(0.1f);
		motionState.setWorldTransform(position.world);
		
		btRigidBody body = new btRigidBody(info);
		body.setMotionState(motionState);
		collisionWorld.addRigidBody(body);
		
		physics.setBody(body);
		physics.setMotionState(motionState);
	}

	@Override
	protected void removed(Entity e) {
		collisionWorld.removeRigidBody(physicsMapper.get(e).getBody());
		// TODO cleanup C++ objects
	}
	
	float elapsed = 0;
	boolean done = false;

	@Override
	protected void processEntities(Array<Entity> entities) {
		
		float delta = Gdx.graphics.getDeltaTime();
		elapsed += delta;
		
		btRigidBody body = physicsMapper.get(entities.first()).getBody();
		if (elapsed > 1.5f && !done) {
			body.applyCentralImpulse(tempVector.set(2, 0, 2));
			done = true;
		}
		
		((btDynamicsWorld) collisionWorld).stepSimulation(delta, 5);

		for (Entity e : entities) {
			physicsMapper.get(e).getMotionState().getWorldTransform(positionMapper.get(e).world);
		}

		debugDrawer.lineRenderer.setProjectionMatrix(cam.combined);
		debugDrawer.begin();
		collisionWorld.debugDrawWorld();
		debugDrawer.end();
	}

}
