package controllers;

import player.IPlayerDataBase;
import player.actions.EndTurnAction;

/**
 * This controller will manage the end turn action
 * 
 * @author Tom
 * 
 */
public class EndTurnController {
	
	private IPlayerDataBase	playerDB; 
	
	/**
	 * Create a new end turn controller.
	 * 
	 * @param db
	 *        The player database this end turn controller uses.
	 * @throws IllegalArgumentException
	 *         The specified argument cannot be null.
	 */
	public EndTurnController(IPlayerDataBase db) throws IllegalArgumentException {
		if (db == null)
			throw new IllegalArgumentException("the specified argument cannot be null");
		this.playerDB = db;
	}
	
	/**
	 * End the turn of the current player.
	 */
	public void endTurn() {
		playerDB.getCurrentPlayer().performAction(new EndTurnAction());
	}
	
}
