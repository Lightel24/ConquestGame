package com.conquest.game.logic.actions;

import com.conquest.game.logic.ConquestLogic;

public abstract class Action {
	public abstract void execute(ConquestLogic context);
	public abstract void render(ConquestLogic context);
}