package game.systems;

import com.badlogic.gdx.utils.Array;

import engine.artemis.ComponentMapper;
import engine.artemis.Entity;
import engine.artemis.Filter;
import engine.artemis.systems.EntitySystem;
import game.components.Movement;
import game.components.Position;


public class MovementSystem extends EntitySystem {
	
	ComponentMapper<Position> pm;
	ComponentMapper<Movement> mm;

	@SuppressWarnings("unchecked")
	public MovementSystem() {
		super(Filter.allComponents(Position.class, Movement.class));
	}
	
	@Override
	public void initialize() {
		pm = world.getMapper(Position.class);
		mm = world.getMapper(Movement.class);
	}

	@Override
	protected void processEntities(Array<Entity> entities) {
		for (Entity e : entities) {
			Position position = pm.get(e);
			Movement movement = mm.get(e);
			movement.velocity.add(movement.acceleration);
			position.world.translate(movement.velocity.cpy().scl(world.getDelta()));
		}
	}

}
