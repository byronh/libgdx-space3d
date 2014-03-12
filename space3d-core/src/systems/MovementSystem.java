package systems;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;

import comparchitecture.Component;
import comparchitecture.Entity;
import comparchitecture.EntityManager;
import comparchitecture.System;
import components.MovementComponent;
import components.WorldComponent;


public class MovementSystem extends System {

	public MovementSystem(EntityManager manager) {
		super(manager);
	}

	@Override
	public void update(float delta) {
		IntMap<Entity> entities = manager.entitiesWithComponent(Component.Movement);
		for (Entity entity : entities.values()) {
			WorldComponent world = entity.getComponent(Component.World);
			MovementComponent movement = entity.getComponent(Component.Movement);
			movement.acceleration = world.cpy().inv().getTranslation(Vector3.Zero);
			movement.velocity.add(movement.acceleration);
			world.translate(movement.velocity.cpy().scl(delta));
		}
	}

}
