package systems;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import components.Movement;
import components.Position;
import engine.artemis.ComponentMapper;
import engine.artemis.Entity;
import engine.artemis.Filter;
import engine.artemis.systems.EntitySystem;


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
			movement.acceleration = position.world.cpy().inv().getTranslation(Vector3.Zero);
			movement.velocity.add(movement.acceleration);
			position.world.translate(movement.velocity.cpy().scl(world.getDelta()));
		}
	}

}
