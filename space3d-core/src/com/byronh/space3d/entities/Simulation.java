package com.byronh.space3d.entities;

import com.badlogic.gdx.math.Vector3;
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
		TerranPlanet earth = new TerranPlanet();
		earth.worldTransform.setToTranslationAndScaling(-15f, 0.5f, 0f, 2f, 2f, 2f);
		
		addEntity(mars);
		addEntity(earth);
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
