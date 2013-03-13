package controllers;

import player.PlayerDatabase;
import item.IItem;

public class UseItemController {
	
	private PlayerDatabase	playerDB;
	
	public UseItemController(PlayerDatabase db) {
		this.playerDB = db;
	}
	
	public void useItem(IItem item) {
		playerDB.getCurrentPlayer().useItem(item);
	}
}
