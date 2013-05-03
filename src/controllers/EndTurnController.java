package controllers;

import player.IPlayer;
import player.IPlayerDataBase;

/**
 * This controller will manage the end turn action.
 * 
 * @author tom
 *
 */
public class EndTurnController {
	
	private IPlayerDataBase	playerDB;
	
	/**
	 * Create a new end turn controller.
	 * 
	 * @param db
	 * 			The player database this end turn controller uses.
	 */
	public EndTurnController(IPlayerDataBase db) {
		this.playerDB = db;
	}
	
	/**
	 * End the turn of the given player.
	 */
	public void endTurn() {
		IPlayer currentPlayer = playerDB.getCurrentPlayer();
		
		currentPlayer.endTurn();
	}
	
}
