package com.conquest.game.logic.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.conquest.game.logic.Player;
import com.conquest.game.screens.GameScreen;

public class Unite{
	public int x,y;
	public int vie;
	public int attaque;
	public int defense;
	public int portee;
	public int deplacement;
	public boolean isSelected = false;
	public Player joueur;
	
	public Unite(int x, int y) {
		this.x = x;
		this.y = y;
	} 
	
	public void update(GameScreen monde) {
		if(isSelected) {
			monde.font.draw(monde.batch, "Selectionné!", 50, 300);
		}else {
			monde.font.draw(monde.batch, "Non selectionné!", 50, 300);
		}
		if(Gdx.input.justTouched()) {
			Ray ray = monde.camera.getPickRay(Gdx.input.getX(), Gdx.input.getY());
			Tile tile = null;
			
			for(Tile temp:monde.monde) {
				if(temp.x==x && temp.y==y) {
					tile = temp;
					System.out.println("test");
				}
			}
			if(Intersector.intersectRayBounds(ray, tile.bounds, null)) {
				isSelected = !isSelected;
			}
		}
		
	}
}
