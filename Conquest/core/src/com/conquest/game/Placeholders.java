package com.conquest.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class Placeholders {
	
	public static Model TILE_DESERT;
	public static Model TILE_PLAINE;
	public static Model TILE_EAU;
	private ModelBuilder primitiveBuilder;
	
	public Placeholders() {
		primitiveBuilder= new ModelBuilder();
		TILE_DESERT = primitiveBuilder.createBox(2f, 2f, 2f, new Material(ColorAttribute.createDiffuse(Color.valueOf("#EDC9AF"))),
				VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal);
		TILE_PLAINE = primitiveBuilder.createBox(2f, 2f, 2f, new Material(ColorAttribute.createDiffuse(Color.valueOf("#567d46"))),
				VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal);
		TILE_EAU = primitiveBuilder.createBox(2f, 2f, 2f, new Material(ColorAttribute.createDiffuse(Color.valueOf("#d4f1f9"))),
				VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal);
	}
}
