package controllers;

import ObjectronExceptions.IllegalMoveException;
import player.PlayerDataBase;
import grid.Direction;

/**
 * This class will handle the move actions of the GUI.
 * 
 * @author tom
 * 
 */
public class MoveController {
	
	private PlayerDataBase	playerDB;
	
	/**
	 * Create a new move controller with a reference to a given player database.
	 * 
	 * @param db
	 */
	public MoveController(PlayerDataBase db) {
		this.playerDB = db;
	}
	
	/**
	 * Move the current player.
	 * 
	 * @param direction
	 *        The direction to move the player in.
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 * @throws IllegalMoveException
	 *         If the given direction is not a legal move.
	 */
	public void move(Direction direction) throws IllegalStateException, IllegalArgumentException,
			IllegalMoveException {
		playerDB.getCurrentPlayer().moveInDirection(direction);
	}
	
}
