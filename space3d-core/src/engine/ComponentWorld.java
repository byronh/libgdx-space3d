package engine;

import engine.artemis.World;
import engine.artemis.managers.ComponentManager;
import engine.artemis.managers.EntityManager;
import engine.artemis.systems.EntitySystem;


public class ComponentWorld extends World {

	public ComponentWorld() {
		super();
	}

	public ComponentWorld(ComponentManager cm, EntityManager em) {
		super(cm, em);
	}
	
	public <T extends EntitySystem> void enableSystem(Class<T> type) {
		this.getSystem(type).setPassive(false);
	}
	
	public <T extends EntitySystem> void disableSystem(Class<T> type) {
		this.getSystem(type).setPassive(true);
	}

}
