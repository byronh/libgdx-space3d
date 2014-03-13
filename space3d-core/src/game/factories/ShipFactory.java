package game.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;

import engine.EntityFactory;
import engine.artemis.Entity;
import engine.artemis.World;
import game.components.Movement;
import game.components.Position;
import game.components.Render;
import game.components.State;


/**
 * WARNING: DO NOT CREATE COMPONENTS USING A CONSTRUCTOR. Use
 * world.createComponent(MyComponent.class) instead to pool Components.
 * 
 * @author Byron
 */
public class ShipFactory extends EntityFactory {

	Model shipModel;

	public ShipFactory(World world, AssetManager assets) {
		super(world, assets);
	}

	@Override
	protected void init() {
		ColorAttribute spec = ColorAttribute.createSpecular(0.7f, 0.7f, 0.5f, 1f);
		FloatAttribute shine = FloatAttribute.createShininess(8.0f);
		shipModel = assets.get("models/ship.g3db", Model.class);
		shipModel.materials.first().set(spec, shine);
	}

	// TODO Remove the need for boilerplate code
	public Entity createShip(float x, float y, float z) {
		
		Entity ship = world.createEntity();
		
		Position position = world.createComponent(Position.class);
		Movement movement = world.createComponent(Movement.class);
		Render render = world.createComponent(Render.class);
		State state = world.createComponent(State.class);
		
		position.world.setToTranslation(x, y, z);
		movement.velocity.set(0, 0, 0.5f);
		render.instance = new ModelInstance(shipModel);
		if (x == 0 && y == 0 && z == 0) state.selected = true;
		
		ship.addComponents(position, movement, render, state);
		ship.addToWorld();
		
		return ship;
	}

}
