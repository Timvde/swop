package controllers;

import player.PlayerDataBase;
import item.IItem;

public class PickUpItemController {
	
	private PlayerDataBase	playerDB;
	
	public PickUpItemController(PlayerDataBase db) {
		this.playerDB = db;
	}
	
	public void pickUpItem(IItem item) {
		playerDB.getCurrentPlayer().pickUpItem(item);
	}
	
}
