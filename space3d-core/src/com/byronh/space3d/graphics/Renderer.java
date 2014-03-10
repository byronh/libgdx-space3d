package com.byronh.space3d.graphics;

import com.badlogic.gdx.graphics.Camera;


public interface Renderer {

	public void init();
	
	public void render(Camera cam);
	
	public void dispose();
	
}
