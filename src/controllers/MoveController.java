package controllers;

import ObjectronExceptions.IllegalMoveException;
import player.PlayerDataBase;
import grid.Direction;

public class MoveController {
	
	private PlayerDataBase playerDB;
	
	public MoveController(PlayerDataBase db) {
		this.playerDB = db;
	}
	
	public void move(Direction direction) throws IllegalStateException, IllegalArgumentException, IllegalMoveException {
		playerDB.getCurrentPlayer().moveInDirection(direction);
	}
	
}
