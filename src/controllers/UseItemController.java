package controllers;

import item.IItem;
import player.IPlayerDataBase;

/**
 * A controller for handling the use actions of the GUI.
 * 
 * @author tom
 *
 */
public class UseItemController {
	
	private IPlayerDataBase	playerDB;
	
	/**
	 * Create a new use item controller with a given player database.
	 * 
	 * @param db
	 * 			The player database.
	 */
	public UseItemController(IPlayerDataBase db) {
		this.playerDB = db;
	}
	
	/**
	 * Use the given item. The item must be in the inventory of the current player.
	 * 
	 * @param item
	 * 			The item that will be used.
	 */
	public void useItem(IItem item) {
		playerDB.getCurrentPlayer().useItem(item);
	}
}
