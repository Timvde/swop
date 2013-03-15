package controllers;

import player.IPlayer;
import player.PlayerDataBase;

public class EndTurnController {
	
	private PlayerDataBase	playerDB;
	
	public EndTurnController(PlayerDataBase db) {
		this.playerDB = db;
	}
	
	public void endTurn() throws IllegalStateException {
		IPlayer currentPlayer = playerDB.getCurrentPlayer();
		
		currentPlayer.endTurn();
	}
	
}
