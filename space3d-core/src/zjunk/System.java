package zjunk;


public abstract class System {
	
	protected EntityManager manager;
	
	public System(EntityManager entityManager) {
		manager = entityManager;
	}

	public abstract void update(float delta);

}
