package controllers;

import ObjectronExceptions.IllegalPickUpException;
import item.IItem;
import player.IPlayerDataBase;

/**
 * A controller that will handle the GUI pick up commands.
 * 
 * @author tom
 * 
 */
public class PickUpItemController {
	
	private IPlayerDataBase	playerDB;
	
	/**
	 * Create a new pick up item controller with a given player database.
	 * 
	 * @param db
	 *        The playerdatabase the controller uses.
	 * @throws IllegalArgumentException
	 *         The specified argument cannot be null
	 */
	public PickUpItemController(IPlayerDataBase db) throws IllegalArgumentException {
		if (db == null)
			throw new IllegalArgumentException("the specified argument cannot be null");
		this.playerDB = db;
	}
	
	/**
	 * Pick up the selected item on the current square.
	 * 
	 * @param item
	 *        The item to pick up.
	 * @throws IllegalPickUpException
	 *         The current player must be able to pickup the specified item
	 * @throws IllegalArgumentException
	 *         The specified item cannot be null
	 */
	public void pickUpItem(IItem item) throws IllegalPickUpException, IllegalArgumentException {
		playerDB.getCurrentPlayer().pickUpItem(item);
	}
	
}
