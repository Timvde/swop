package controllers;

import player.IPlayerDataBase;
import player.actions.MoveAction;
import square.Direction;
import ObjectronExceptions.IllegalMoveException;

/**
 * This class will handle the move actions of the GUI.
 * 
 * @author tom
 * 
 */
public class MoveController {
	
	private IPlayerDataBase	playerDB;
	
	/**
	 * Create a new move controller with a reference to a given player database.
	 * 
	 * @param db
	 *        The playerdatabase the controller uses.
	 */
	public MoveController(IPlayerDataBase db) {
		if (db == null)
			throw new IllegalArgumentException("the specified argument cannot be null");
		this.playerDB = db;
	}
	
	/**
	 * Move the current player. The IllegalMoveException must be caught in the
	 * GUI to notify players about move problems.
	 * 
	 * @param direction
	 *        The direction to move the player in.
	 * @throws IllegalMoveException
	 *         If the given direction is not a legal move. This can be caught in
	 *         the GUI to display feedback.
	 */
	public void move(Direction direction) throws IllegalMoveException {
		playerDB.getCurrentPlayer().performAction(new MoveAction(direction));
	}
	
}
