package zjunk;

import components.Movement;
import components.RenderComponent;
import components.Position;

public interface Component {
	
	public static final Class<Position> World = Position.class;
	public static final Class<Movement> Movement = Movement.class;
	public static final Class<RenderComponent> Render = RenderComponent.class;

	public abstract String debug();

}
