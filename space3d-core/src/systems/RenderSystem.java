package systems;

import zjunk.Component;
import zjunk.Entity;
import zjunk.EntityManager;
import zjunk.System;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.IntMap;

import components.RenderComponent;
import components.WorldComponent;


public class RenderSystem extends System {
	
	private PerspectiveCamera cam;
	private ModelBatch batch;

	public RenderSystem(EntityManager entityManager, PerspectiveCamera camera) {
		super(entityManager);
		cam = camera;
		batch = new ModelBatch();
	}

	@Override
	public void update(float delta) {
		// TODO Replace with bit mask system, this is really ugly and doesn't even work
		IntMap<Entity> entities = manager.entitiesWithComponent(Component.Render);
		for (Entity entity : entities.values()) {
			WorldComponent world = entity.getComponent(Component.World);
			RenderComponent render = entity.getComponent(Component.Render);
			
			render.instance.transform = world;
			
			batch.begin(cam);
			batch.render(render.instance, render.environment, render.shader);
			batch.end();
		}
	}

}
