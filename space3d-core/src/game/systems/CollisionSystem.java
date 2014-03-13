package game.systems;

import com.badlogic.gdx.utils.Array;

import engine.artemis.Entity;
import engine.artemis.Filter;
import engine.artemis.systems.EntitySystem;


public class CollisionSystem extends EntitySystem {

	public CollisionSystem(Filter filter) {
		super(filter);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void processEntities(Array<Entity> entities) {
		// TODO Auto-generated method stub

	}
	
//	for each possible collision
//    check for collision, record it if a collision occurs
//
//for each found collision
//    handle collision, record the collision response (delete object, ignore, etc.)
//
//for each collision response
//    modify collision world according to response

}
