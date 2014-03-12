package comparchitecture;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.IntMap;



public class EntityManager {
	
	private IntMap<Entity> entities = new IntMap<Entity>();
	
	private int id = 0;
	private Entity current;
	
	public EntityManager() {
		begin();
	}
	
	public EntityManager begin() {
		return this;
	}
	
	public Entity build() {
		return current;
	}
	
	public EntityManager world(float x, float y, float z) {
		
		return this;
	}
	
	private int nextId() {
		if (id < Integer.MAX_VALUE) {
			return id++;
		}
		for (int i=0; i < Integer.MAX_VALUE; i++) {
			if (!entities.containsKey(i)) return i;
		}
		throw new GdxRuntimeException("Exceeded maximum number of entities");
	}

}
