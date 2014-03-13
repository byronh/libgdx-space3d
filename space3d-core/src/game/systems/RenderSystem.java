package game.systems;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.Array;

import engine.artemis.ComponentMapper;
import engine.artemis.Entity;
import engine.artemis.Filter;
import engine.artemis.systems.EntitySystem;
import game.components.Position;
import game.components.Render;

public class RenderSystem extends EntitySystem {

	ComponentMapper<Position> pm;
	ComponentMapper<Render> rm;

	private PerspectiveCamera cam;
	private ModelBatch batch;

	@SuppressWarnings("unchecked")
	public RenderSystem(PerspectiveCamera camera) {
		super(Filter.allComponents(Position.class, Render.class));
		cam = camera;
	}

	@Override
	public void initialize() {
		pm = world.getMapper(Position.class);
		rm = world.getMapper(Render.class);
		batch = new ModelBatch();
	}

	@Override
	protected void processEntities(Array<Entity> entities) {
		batch.begin(cam);
		for (Entity e : entities) {
			Position position = pm.get(e);
			Render render = rm.get(e);
			render.instance.transform = position.world;
			batch.render(render.instance, render.environment, render.shader);
		}
		batch.end();
	}
	
}
