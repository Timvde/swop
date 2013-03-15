package controllers;

import item.IItem;
import player.IPlayerDataBase;

/**
 * A controller that will handle the GUI pick up commands.
 * @author tom
 *
 */
public class PickUpItemController { 
	
	private IPlayerDataBase	playerDB;
	
	/**
	 * Create a new pick up item controller with a given player database.
	 * 
	 * @param db
	 * 			The playerdatabase the controller uses.
	 */
	public PickUpItemController(IPlayerDataBase db) {
		this.playerDB = db;
	}
	
	/**
	 * Pick up the selected item on the current square.
	 * 
	 * @param item
	 * 			The item to pick up.
	 */
	public void pickUpItem(IItem item) {
		playerDB.getCurrentPlayer().pickUpItem(item);
	}
	
}
