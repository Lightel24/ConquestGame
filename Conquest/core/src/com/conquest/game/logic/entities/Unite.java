package com.conquest.game.logic.entities;

import java.util.ArrayList;

import com.conquest.game.logic.Player;
import com.conquest.game.logic.actions.Action;

public abstract class Unite{
	int x,y;
	int vie;
	int attaque;
	int defense;
	int portee;
	int deplacement;
	Player joueur;
	ArrayList<Action> actions;
	
	public Unite() {
		
	}
}
