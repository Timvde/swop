package controllers;

import item.IItem;
import player.IPlayerDataBase;

public class PickUpItemController {
	
	private IPlayerDataBase	playerDB;
	
	public PickUpItemController(IPlayerDataBase db) {
		this.playerDB = db;
	}
	
	public void pickUpItem(IItem item) {
		playerDB.getCurrentPlayer().pickUpItem(item);
	}
	
}
