package game.systems;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
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
	private Environment environment;
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

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .3f, .3f, .3f, 1f));
//		environment.add(new PointLight().set(1f, 1f, 1f, -15f, 5.5f, 15f, 1000f));
		environment.add(new DirectionalLight().set(1f, 1f, 1f, 3f, -1f, -1f));
		
	}
	
	@Override
	protected void inserted(Entity e) {
		Position position = pm.get(e);
		Render render = rm.get(e);
		render.instance.transform = position.world;
		// Create spatial here. Possibly initialize modelinstance plus material/texture/shader?
	}
	
	@Override
	protected void removed(Entity e) {
		// Remove spatial here
	}

	@Override
	protected void processEntities(Array<Entity> entities) {
		// TODO extract batch so that multiple systems can render? e.g. light source
		batch.begin(cam);
		for (Entity e : entities) {
			Render render = rm.get(e);
			batch.render(render.instance, environment, render.shader);
		}
		batch.end();
	}

}
