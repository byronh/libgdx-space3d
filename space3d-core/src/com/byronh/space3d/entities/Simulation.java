package com.byronh.space3d.entities;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;


public class Simulation {

	private Array<Entity> entities = new Array<Entity>();
	private Array<SimulationListener> listeners = new Array<SimulationListener>();

	public void mainLoop(float delta) {

		for (Entity object : entities) {
			if (object instanceof Planet) {
				object.worldTransform.rotate(Vector3.Y, 2.5f * delta);
			}
		}

	}

	public void init() {
		DesertPlanet mars = new DesertPlanet();
		mars.worldTransform.setToTranslation(0f, 0f, 0f);
		
//		for (float z=-50; z<=50; z+=2) {
//			for (float x=-50; x<=50; x+=2) {
//				Planet p = new DesertPlanet();
//				p.worldTransform.trn(x, 0.5f, z);
//				addEntity(p);
//			}
//		}
		
		IcePlanet icy = new IcePlanet();
		icy.worldTransform.setToTranslation(-1.75f, 0.5f, -1f);
		
		addEntity(mars);
		addEntity(icy);
	}
	
	public void addListener(SimulationListener listener) {
		listeners.add(listener);
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
		for (SimulationListener listener : listeners) {
			listener.onEntityAdded(entity);
		}
	}

	public Array<Entity> getEntities() {
		return entities;
	}

}
