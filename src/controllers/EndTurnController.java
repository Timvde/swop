package controllers;

import player.IPlayer;
import player.PlayerDataBase;

public class EndTurnController {
	
	private PlayerDataBase	playerDB;
	
	public EndTurnController(PlayerDataBase db) {
		this.playerDB = db;
	}
	
	public void endTurn() {
		IPlayer currentPlayer = playerDB.getCurrentPlayer();
		
		int actionsLeft = currentPlayer.getAllowedNumberOfActions();
		currentPlayer.skipNumberOfActions(actionsLeft);
	}
	
}
