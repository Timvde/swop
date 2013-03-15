package controllers;

import player.IPlayer;
import player.IPlayerDataBase;

public class EndTurnController {
	
	private IPlayerDataBase	playerDB;
	
	public EndTurnController(IPlayerDataBase db) {
		this.playerDB = db;
	}
	
	public void endTurn() throws IllegalStateException {
		IPlayer currentPlayer = playerDB.getCurrentPlayer();
		
		currentPlayer.endTurn();
	}
	
}
