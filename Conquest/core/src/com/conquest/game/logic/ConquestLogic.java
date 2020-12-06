package com.conquest.game.logic;

import com.conquest.game.logic.actions.Action;
import com.conquest.game.logic.entities.Tile;

public class ConquestLogic {
	
	private Tile[] monde;
	private Player[] participants;
	private int joueurAutorise;
	
	public ConquestLogic(Tile[] monde,Player[] participants,int celuiQuiCommence) {
		this.monde = monde;
		this.participants = participants;
		this.joueurAutorise = celuiQuiCommence % participants.length;
	}
	
	public Tile[] getWorld() {
		return monde;
	}
	
	public Action[] interact(float x, float y) {
		return null;
	}
}
