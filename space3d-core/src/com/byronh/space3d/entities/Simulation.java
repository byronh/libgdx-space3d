package com.byronh.space3d.entities;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;


public class Simulation {
	
	private Array<Entity> objects = new Array<Entity>();
	
	public void mainLoop(float delta) {
		
		for (Entity object : objects) {
			if (object instanceof Planet) {
				object.transform.rotate(Vector3.Y, 2.5f * delta);
			}
		}
		
	}

}
