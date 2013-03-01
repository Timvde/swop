package controllers;

import grid.Direction;
import game.Game;
import actions.MoveAction;

public class MoveController {
	
	private Game game;
	
	public MoveController(Game game) {
		this.game = game;
	}
	
	public void movePlayer(Direction direction) {
		MoveAction action = new MoveAction(direction);
		game.executeGridAction(action);
	}
	
}
