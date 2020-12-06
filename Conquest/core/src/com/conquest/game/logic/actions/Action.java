package com.conquest.game.logic.actions;

import com.conquest.game.logic.ConquestLogic;

public abstract class Action {
	//Execute l'action : Lance l'animation + influence l'etat du jeu(tue une unite, spawn une autre)
	public abstract void execute(ConquestLogic context);
	//Affiche la proposition d'action -> Où est ce que l'on interagit avec les proposition?
	//Dans un premier temps on interagit ici...
	//Ce qui signifie que seul renderProposition peut appeler execute...
	public abstract void renderProposition(ConquestLogic context);
	
}