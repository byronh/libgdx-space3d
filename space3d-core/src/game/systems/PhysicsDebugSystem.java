package game.systems;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Array;

import engine.artemis.ComponentMapper;
import engine.artemis.Entity;
import engine.artemis.Filter;
import engine.artemis.systems.EntitySystem;
import engine.physics.DebugDrawer;
import game.components.Physics;
import game.components.Position;

public class PhysicsDebugSystem extends EntitySystem {

	ComponentMapper<Position> positionMapper;
	ComponentMapper<Physics> physicsMapper;
	
	private PerspectiveCamera cam;
	private DebugDrawer debugDrawer = null;
	private btDynamicsWorld collisionWorld;

	@SuppressWarnings("unchecked")
	public PhysicsDebugSystem(PerspectiveCamera camera) {
		super(Filter.allComponents(Position.class, Physics.class));
		cam = camera;
	}

	@Override
	public void initialize() {
		positionMapper = world.getMapper(Position.class);
		physicsMapper = world.getMapper(Physics.class);
		
		collisionWorld = world.getSystem(PhysicsSystem.class).getCollisionWorld();
		collisionWorld.setDebugDrawer(debugDrawer = new DebugDrawer());
		debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawWireframe);
	}

	@Override
	protected void processEntities(Array<Entity> entities) {
		debugDrawer.lineRenderer.setProjectionMatrix(cam.combined);
		debugDrawer.begin();
		collisionWorld.debugDrawWorld();
		debugDrawer.end();
	}
}
