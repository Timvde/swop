package controllers;

import gui.GUI;
import item.IItem;
import item.identitydisk.ChargedIdentityDisk;
import item.identitydisk.IdentityDisk;
import player.IPlayerDataBase;
import ObjectronExceptions.CannotPlaceLightGrenadeException;

/**
 * A controller for handling the use item actions of the GUI.
 * 
 * 
 */
public class UseItemController {
	
	private IPlayerDataBase	playerDB;
	private GUI				gui;
	
	/**
	 * Create a new use item controller with a given player database. After
	 * creating an new use item controller, a graphical user interface should be
	 * set for this object. If this is not, the behaviour of this object is
	 * unspecified.
	 * 
	 * @param db
	 *        The player database.
	 */
	public UseItemController(IPlayerDataBase db) {
		this.playerDB = db;
	}
	
	/**
	 * Set the specified user interface as the user interface for this
	 * controller.
	 * 
	 * @param gui
	 *        the user interface to set
	 */
	public void setGUI(GUI gui) {
		this.gui = gui;
	}
	
	/**
	 * Use the given item. The item must be in the inventory of the current
	 * player.
	 * 
	 * @param item
	 *        The item that will be used.
	 * @throws CannotPlaceLightGrenadeException 
	 * @throws IllegalStateException 
	 * @throws IllegalArgumentException 
	 */
	public void useItem(IItem item) throws IllegalArgumentException, IllegalStateException, CannotPlaceLightGrenadeException {
		// If the item used is an identity disk, a direction must be set before
		// the item
		// can be used. As described in the documentation of identity disk.
		if (item instanceof IdentityDisk)
			((ChargedIdentityDisk) item).setDirection(gui.getBasicDirection());
		
		playerDB.getCurrentPlayer().useItem(item);
	}
}
