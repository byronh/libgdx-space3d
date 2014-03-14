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
 * A lot of your text concerns rendering, and that is a tricky thing indeed. Not
 * only in Artemis, but just generally. Not sure if you're doing 2D or 3D, but
 * since I'm mostly familiar with 2D I'll explain my approach.
 * 
 * You essentially have a RenderSystem. That system picks up any Spatial
 * components and creates a SpatialForm for it. This is already demonstrated in
 * the StarWarrior demo I believe.
 * 
 * The tricky part is the "painters algorithm problem":
 * http://en.wikipedia.org/wiki/Painter%27s_algorithm
 * 
 * Example of this would be a entity in a RTS game like a tank that was composed
 * of the track belts, the tank base, the tower, the barrel, health bar,
 * selection highlighting. We need to ensure all these are rendered in correct
 * order, not only so that the tower is on top of the base, but also that all
 * towers are on top of all bases for all tanks, and all healthbars are rendered
 * after all the tanks to ensure healthbars are above everything.
 * 
 * You cannot solve this problem by rendering entities, you need to break this
 * down into rendering of "spatials", whereas each entity can have multiple
 * spatials.
 * 
 * Your game needs to be rendered in layers, terrain first, tank belts next,
 * then tank base, then barrels, then towers, then healthbars and selection
 * highlighting. Your RenderSystem needs to have buckets, each bucket being the
 * layer and can contain any number of spatials.
 * 
 * Then you can attach one spatial node called TankSpatial to your tank entity,
 * and that TankSpatial node contains children spatials that are
 * TrackBeltsSpatial, TankBaseSpatial, TankTowerAndBarrelSpatial,
 * GenericHealthbarSpatial, GenericSelectionHighlightSpatial. For each of those
 * spatials you assign a layer, and the RenderSystem would put each spatial into
 * the correct bucket according to that layer.
 * 
 * This way you can logically think of and code your entities as a single thing,
 * but the RenderingSystem splits the spatials it up into layers.
 * 
 * I already do this in one of my game and it works great!
 * 
 * 
 * 
 * Another approach would be to have a special HealthbarRenderingSystem,
 * SelectionHighlightRenderingSystem, and that may be what you need if you can
 * zoom in and out in your game without wanting the healthbars and selection
 * highlight to scale. But for most ingame entities you need layering.
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