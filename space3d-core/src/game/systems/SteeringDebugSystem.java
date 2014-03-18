package game.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.utils.Array;

import engine.artemis.ComponentMapper;
import engine.artemis.Entity;
import engine.artemis.Filter;
import engine.artemis.systems.EntitySystem;
import game.components.Steering;
import game.utils.Renderer;

public class SteeringDebugSystem extends EntitySystem {

	ComponentMapper<Steering> sm;

	private Renderer renderer;
	private Model targetModel;
	private ModelInstance target;

	@SuppressWarnings("unchecked")
	public SteeringDebugSystem(Renderer renderer) {
		super(Filter.allComponents(Steering.class));
		this.renderer = renderer;
	}

	@Override
	public void initialize() {
		sm = world.getMapper(Steering.class);
		targetModel = renderer.modelBuilder.createBox(1f, 1f, 1f,
				GL20.GL_LINES,
				new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				Usage.Position);
		target = new ModelInstance(targetModel);
	}

	@Override
	protected void processEntities(Array<Entity> entities) {
		for (Entity e : entities) {
			Steering steering = sm.get(e);
			target.transform.setTranslation(steering.targetPos);
			renderer.modelBatch.render(target);
		}
	}

}
