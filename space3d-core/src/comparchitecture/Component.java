package comparchitecture;

import components.MovementComponent;
import components.RenderComponent;
import components.WorldComponent;

public interface Component {
	
	public static final Class<WorldComponent> World = WorldComponent.class;
	public static final Class<MovementComponent> Movement = MovementComponent.class;
	public static final Class<RenderComponent> Render = RenderComponent.class;

	public abstract String debug();

}
