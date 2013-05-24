package controllers;

import gui.ArgumentsHandler;
import gui.GUI;
import item.IItem;
import player.IPlayerDataBase;
import player.actions.UseAction;
import ObjectronExceptions.IllegalUseException;

/**
 * A controller for handling the use item actions of the GUI.
 */
public class UseItemController {
	
	private IPlayerDataBase		playerDB;
	private ArgumentsHandler	argumentsHandler;
	
	/**
	 * Create a new use item controller with a given player database. After
	 * creating an new use item controller, a graphical user interface should be
	 * set for this object. If this is not, the behaviour of this object is
	 * unspecified.
	 * 
	 * @param db
	 *        The playerdatabase the controller uses.
	 */
	public UseItemController(IPlayerDataBase db) {
		if (db == null)
			throw new IllegalArgumentException("the specified argument cannot be null");
		this.playerDB = db;
	}
	
	/**
	 * Set an ArgumentsHandler for this controller
	 * 
	 * @param argumentsHandler
	 *        The handler to set
	 */
	public void setArgumentsHandler(ArgumentsHandler argumentsHandler) {
		this.argumentsHandler = argumentsHandler;
	}
	
	/**
	 * Use the given item. The item must be in the inventory of the current
	 * player.
	 * 
	 * @param item
	 *        The item that will be used.
	 * @throws IllegalUseException
	 *         The item cannot be used.
	 */
	public void useItem(IItem item) throws IllegalUseException {
		// If the item used is an identity disk, a direction must be set before
		// the item
		// can be used. As described in the documentation of identity disk.
		
		playerDB.getCurrentPlayer().performAction(new UseAction(item, argumentsHandler));
	}
}
