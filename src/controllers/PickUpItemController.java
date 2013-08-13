package controllers;

import ObjectronExceptions.IllegalPickUpException;
import ObjectronExceptions.InventoryAlreadyContainsFlagException;
import ObjectronExceptions.InventoryFullException;
import item.IItem;
import player.IPlayerDataBase;
import player.actions.PickupItemAction;

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
	 * Pick up the selected item on the current square. Use the
	 * InventoryFullException to notify players about a full inventory. Use the
	 * InventoryAlreadyContainsFlagException to notify players about their
	 * inventory already containing a flag.
	 * 
	 * @param item
	 *        The item to pick up.
	 * @throws IllegalPickUpException
	 *         The current player must be able to pickup the specified item
	 * @throws IllegalArgumentException
	 *         The specified item cannot be null
	 * @throws InventoryFullException
	 *         The inventory of the player is full. This can be caught in the
	 *         GUI for displaying messages.
	 * @throws InventoryAlreadyContainsFlagException
	 *         The player is already carrying a flag and cannot pick another one
	 *         up. This can be caught in the GUI for displaying messages.
	 * 
	 */
	public void pickUpItem(IItem item) throws IllegalPickUpException, IllegalArgumentException,
			InventoryFullException, InventoryAlreadyContainsFlagException {
		playerDB.getCurrentPlayer().performAction(new PickupItemAction(item));
	}
	
}
