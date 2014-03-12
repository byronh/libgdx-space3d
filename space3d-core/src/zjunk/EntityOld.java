package zjunk;




public class EntityOld {

//	public final int id;
//
//	private LongMap<Component> components = new LongMap<Component>();
//	
//	private long mask = 0;
//
//	public EntityOld(int id) {
//		this.id = id;
//	}
//
//	public long getMask() {
//		return mask;
//	}
//
//	public void addComponent(Component component) {
//		long flag = component.getFlag();
//		if (!components.containsKey(flag)) {
//			mask |= flag;
//			components.put(flag, component);
//		}
//		throw new IllegalArgumentException(this.getDescription() + " can not contain more than one instance of the same component.");
//	}
//	
////	public Component getComponent(long componentFlag) {
////		return components.get(componentFlag, null);
////	}
//	
//	public <T extends Component> T getComponent(long componentFlag) {
//		Component component = components.get(componentFlag, null);
//		if (component instanceof T) {
//			
//		}
//		return null;
//	}
//
//	public Component removeComponent(long componentFlag) {
//		if ((mask & componentFlag) == componentFlag) {
//			mask &= ~componentFlag;
//			components.get(componentFlag);
//		}
//		throw new IllegalArgumentException(this.getDescription() + " can not remove a component it does not have.");
//	}
//
//	public void debug() {
//		for (Component component : components.values()) {
//			Gdx.app.log(this.getDescription() + component.getClass().getSimpleName(), component.debug());
//		}
//	}
//	
//	private String getDescription() {
//		return getClass().getSimpleName() + "[" + id + "].";
//	}
}
