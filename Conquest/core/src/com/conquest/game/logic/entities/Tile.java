package com.conquest.game.logic.entities;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Tile {
	public static final Unite empty = null;
	int x,y;
	public Unite entitySlot;
	public ModelInstance instance;
	public BoundingBox bounds;
	
	public Tile(ModelInstance instance,int x, int y) {
		this.instance = instance;
		bounds = new BoundingBox();
		bounds = instance.calculateBoundingBox(bounds).mul(instance.transform);
		this.x = x;
		this.y = y;
	}
}
