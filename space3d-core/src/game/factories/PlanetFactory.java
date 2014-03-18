package game.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import engine.EntityFactory;
import engine.artemis.Entity;
import engine.artemis.World;
import game.components.Position;
import game.components.Render;


/**
 * WARNING: DO NOT CREATE COMPONENTS USING A CONSTRUCTOR. Use
 * world.createComponent(MyComponent.class) instead to pool Components.
 * 
 * @author Byron
 */
public class PlanetFactory extends EntityFactory {

	Model sphereModel;
	TextureAttribute venus, space;

	public PlanetFactory(World world, AssetManager assets) {
		super(world, assets);
	}

	@Override
	protected void init() {
		ModelBuilder builder = new ModelBuilder();
		ColorAttribute spec = ColorAttribute.createSpecular(0.7f, 0.7f, 0.5f, 1f);
		FloatAttribute shine = FloatAttribute.createShininess(8.0f);
		venus = TextureAttribute.createDiffuse(assets.get("texture-maps/venus.gif", Texture.class));
//		space = TextureAttribute.createDiffuse(assets.get("texture-maps/galaxy_starfield.png", Texture.class));
		sphereModel = builder.createSphere(1f, 1f, 1f, 60, 60, new Material(spec, shine), Usage.Normal | Usage.Position | Usage.TextureCoordinates);
//		sphereModel = builder.createSphere(1f, 1f, 1f, 60, 60, GL20.GL_LINES, new Material(spec, shine), Usage.Normal | Usage.Position
//				| Usage.TextureCoordinates);
	}

	public Entity createPlanet(float x, float y, float z) {
		Entity planet = world.createEntity();
		Position position = world.createComponent(Position.class);
		position.world.setToTranslation(x, y, z);
		position.world.scl(500f);
		Render render = world.createComponent(Render.class);
		ModelInstance sphere = new ModelInstance(sphereModel);
		sphere.materials.first().set(venus);
		render.instance = sphere;
		planet.addComponents(position, render);
		planet.addToWorld();
		return planet;
	}

	public Entity createSkybox() {
		Entity skybox = world.createEntity();
		Position position = world.createComponent(Position.class);
		position.world.scl(-1000f);
		Render render = world.createComponent(Render.class);
		ModelInstance sphere = new ModelInstance(sphereModel);
		sphere.materials.first().set(new Material(space));
		render.instance = sphere;
		skybox.addComponents(position, render);
		skybox.addToWorld();
		return skybox;
	}

}
