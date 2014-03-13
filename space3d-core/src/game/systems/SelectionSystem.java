package game.systems;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;

import engine.artemis.ComponentMapper;
import engine.artemis.Entity;
import engine.artemis.Filter;
import engine.artemis.systems.EntitySystem;
import game.components.State;


public class SelectionSystem extends EntitySystem implements InputProcessor {

	ComponentMapper<State> sm;

	@SuppressWarnings("unchecked")
	public SelectionSystem() {
		super(Filter.allComponents(State.class));
	}

	@Override
	public void initialize() {
		sm = world.getMapper(State.class);
	}

	@Override
	protected void processEntities(Array<Entity> entities) {
		for (Entity e : entities) {
			State state = sm.get(e);
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
