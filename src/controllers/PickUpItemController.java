package controllers;

import player.PlayerDatabase;
import item.IItem;

public class PickUpItemController {
	
	private PlayerDatabase	playerDB;
	
	public PickUpItemController(PlayerDatabase db) {
		this.playerDB = db;
	}
	
	public void pickUpItem(IItem item) {
		playerDB.getCurrentPlayer().pickUpItem(item);
	}
	
}
