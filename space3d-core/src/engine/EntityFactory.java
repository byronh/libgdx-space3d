package engine;

import com.badlogic.gdx.assets.AssetManager;

import engine.artemis.World;


/**
 * WARNING: DO NOT CREATE COMPONENTS USING A CONSTRUCTOR. Use
 * world.createComponent(MyComponent.class) instead to pool Components.
 * 
 * @author Byron
 */
public abstract class EntityFactory {

	protected World world;
	protected AssetManager assets;

	public EntityFactory(World world, AssetManager assets) {
		this.world = world;
		this.assets = assets;
		init();
	}

	protected abstract void init();

}
