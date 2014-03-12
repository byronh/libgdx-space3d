package zjunk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;


public class Entity {

	public final int id;

	private ObjectMap<Class<? extends Component>, Component> components = new ObjectMap<Class<? extends Component>, Component>();
	
	private EntityManager manager;

	public Entity(EntityManager manager, int id) {
		this.manager = manager;
		this.id = id;
	}

	public <T extends Component> void add(T component) {
		if (components.containsKey(component.getClass())) {
			throw new IllegalArgumentException(this.getDescription() + " can not contain more than one " + component.getClass().getSimpleName());
		}
		components.put(component.getClass(), component);
	}
	
	public <T extends Component> boolean hasComponent(Class<T> type) {
		return components.containsKey(type);
	}

	public <T extends Component> T getComponent(Class<T> type) {
		if (!components.containsKey(type)) {
			throw new IllegalArgumentException(this.getDescription() + " does not contain a " + type.getSimpleName());
		}
		return type.cast(components.get(type));
	}
	
	public <T extends Component> T remove(Class<T> type) {
		if (!components.containsKey(type)) {
			throw new IllegalArgumentException(this.getDescription() + " can not remove " + type.getSimpleName());
		}
		return type.cast(components.remove(type));
	}
	
	public void debug() {
		for (Component component : components.values()) {
			Gdx.app.log(this.getDescription() + component.getClass().getSimpleName() + "\n", component.debug());
		}
	}

	private String getDescription() {
		return getClass().getSimpleName() + "[" + id + "]";
	}
}
