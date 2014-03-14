package game.components;

import engine.artemis.Component;

public class Expires implements Component {
	private int lifeTime;
	
	public Expires(int lifeTime) {
		this.lifeTime = lifeTime;
	}
	
	public int getLifeTime() {
		return lifeTime;
	}
	
	public void setLifeTime(int lifeTime) {
		this.lifeTime = lifeTime;
	}
	
	public void reduceLifeTime(int lifeTime) {
		this.lifeTime -= lifeTime;
	}
	
	public boolean isExpired() {
		return lifeTime <= 0;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	

}
