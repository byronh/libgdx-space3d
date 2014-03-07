package com.byronh.space3d;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.byronh.space3d.entities.GameObject;
import com.byronh.space3d.entities.Planet;


public class Simulation {
	
	private Array<GameObject> objects = new Array<GameObject>();
	
	public void mainLoop(float delta) {
		
		for (GameObject object : objects) {
			if (object instanceof Planet) {
				object.transform.rotate(Vector3.Y, 2.5f * delta);
			}
		}
		
	}

}
