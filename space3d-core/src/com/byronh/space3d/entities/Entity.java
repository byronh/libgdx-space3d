package com.byronh.space3d.entities;

import com.badlogic.gdx.math.Matrix4;


public abstract class Entity {

	public Matrix4 worldTransform;
	
	public boolean selected = false;
	
	public Entity() {
		worldTransform = new Matrix4();
	}

	public Entity(Matrix4 transform) {
		worldTransform = transform;
	}

}
