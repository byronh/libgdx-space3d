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

	/*
	 * for each possible collision check for collision, record it if a collision
	 * occurs
	 * 
	 * for each found collision handle collision, record the collision response
	 * (delete object, ignore, etc.)
	 * 
	 * for each collision response modify collision world according to response
	 * 
	 * use separate system for collision detection, then collision response?
	 * pass a component in between with overlap data
	 * 
	 * @arielsan - I like the idea of a specialized data structure for collision
	 * detection, I used something similar in a previous game - to store how
	 * much the object overlaps, and what it is overlapping with. I will poke
	 * around in your code a bit more and see if I can't find a way to mesh it
	 * with what I'm working with. Thank you for sharing!
	 */

}
