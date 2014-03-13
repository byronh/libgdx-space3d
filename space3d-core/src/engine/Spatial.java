package engine;

import engine.artemis.Entity;
import engine.artemis.World;


/**
 * The spatial contains all necessary rendering-logic and rendering-state. You
 * have to make a distinction between entity data/state and system-specific
 * state on that entity. A entity data/state is used in multiple systems, while
 * a system-specific state is only used within that system. Spatial allows you
 * to encapsulate a system-specific state per entity.Imagine a TankSpatial. The
 * tank has tracks that animate according to the movement of the tank. This is
 * irrelevant information for all other systems, and is used only in rendering.
 * You can maintain and update the state of track animation in the TankSpatial.
 * The usage of Spatial in the RenderingSystem is an example of how you can do
 * things in other systems, although it's only necessary for rendering purposes
 * in that demo. You don't want to inline every possible rendering in the
 * RenderingSystem, so you defer it to the Spatials.
 * 
 * @author Byron
 * 
 */
public abstract class Spatial {

	protected World world;
	protected Entity owner;

	public Spatial(World world, Entity owner) {
		this.world = world;
		this.owner = owner;
	}

	public abstract void initalize();

	public abstract void render();

}