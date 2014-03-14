package game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;

import engine.artemis.ComponentMapper;
import engine.artemis.Entity;
import engine.artemis.Filter;
import engine.artemis.systems.EntitySystem;
import game.components.Position;
import game.components.Render;
import game.components.Select;


public class SelectionSystem extends EntitySystem implements InputProcessor {

	ComponentMapper<Select> sm;
	ComponentMapper<Position> pm;

	private Camera cam;

	private ShapeRenderer shapeRenderer;

	@SuppressWarnings("unchecked")
	public SelectionSystem(Camera camera) {
		super(Filter.allComponents(Position.class, Select.class, Render.class));
		cam = camera;
	}

	@Override
	public void initialize() {
		sm = world.getMapper(Select.class);
		pm = world.getMapper(Position.class);

		shapeRenderer = new ShapeRenderer();
	}

	@Override
	protected void processEntities(Array<Entity> entities) {

		Vector3 screenPos = new Vector3();

		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.GREEN);

		for (Entity e : entities) {
			Select select = sm.get(e);
			Position position = pm.get(e);
			if (select.selected) {
				screenPos = position.world.getTranslation(screenPos);
				cam.project(screenPos);
				shapeRenderer.circle(screenPos.x, screenPos.y, 50f);
			}
		}
		shapeRenderer.end();
	}

	protected void deselectAll() {
		Array<Select> selects = world.getComponentManager().getComponents(Select.class);
		for (Select select : selects) {
			if (select == null)
				continue;
			select.selected = false;
		}
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button == Input.Buttons.LEFT) {

			if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && !Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
				deselectAll();
			}

			Ray ray = cam.getPickRay(screenX, screenY);
			Vector3 intersection = new Vector3();
			for (Entity e : actives) {
				Vector3 position = new Vector3();
				pm.get(e).world.getTranslation(position);
				if (Intersector.intersectRaySphere(ray, position, 0.5f, intersection)) {
					sm.get(e).selected = !sm.get(e).selected;
					return false;
				}
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
