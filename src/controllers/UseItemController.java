package controllers;

import player.PlayerDataBase;
import item.IItem;

public class UseItemController {
	
	private PlayerDataBase	playerDB;
	
	public UseItemController(PlayerDataBase db) {
		this.playerDB = db;
	}
	
	public void useItem(IItem item) {
		playerDB.getCurrentPlayer().useItem(item);
	}
}
