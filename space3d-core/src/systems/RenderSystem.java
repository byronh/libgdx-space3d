package systems;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.IntMap;
import comparchitecture.Component;
import comparchitecture.Entity;
import comparchitecture.EntityManager;
import comparchitecture.System;
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
