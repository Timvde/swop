package controllers;

import player.PlayerDatabase;
import grid.Direction;

public class MoveController {
	
	private PlayerDatabase playerDB;
	
	public MoveController(PlayerDatabase db) {
		this.playerDB = db;
	}
	
	public void move(Direction direction) {
		playerDB.getCurrentPlayer().move(direction);
	}
	
}
