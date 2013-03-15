package controllers;

import grid.Direction;
import player.IPlayerDataBase;
import ObjectronExceptions.IllegalMoveException;

public class MoveController {
	
	private IPlayerDataBase playerDB;
	
	public MoveController(IPlayerDataBase db) {
		this.playerDB = db;
	}
	
	public void move(Direction direction) throws IllegalStateException, IllegalArgumentException, IllegalMoveException {
		playerDB.getCurrentPlayer().moveInDirection(direction);
	}
	
}
