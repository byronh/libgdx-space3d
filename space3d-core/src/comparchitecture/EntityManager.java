package comparchitecture;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.IntMap;



public class EntityManager {
	
	private IntMap<Entity> entities = new IntMap<Entity>();
	
	private int id = 0;
	
	public Entity create() {
		Entity entity = new Entity(this, nextId());
		entities.put(entity.id, entity);
		return entity;
	}
	
	public <T extends Component> IntMap<Entity> entitiesWithComponent(Class<T> type) {
		// TODO Pool the data structure
		IntMap<Entity> result = new IntMap<Entity>();
		for (Entity entity : entities.values()) {
			if (entity.hasComponent(type)) {
				result.put(entity.id, entity);
			}
		}
		return result;
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
