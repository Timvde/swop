package controllers;

import item.IItem;
import player.IPlayerDataBase;

public class UseItemController {
	
	private IPlayerDataBase	playerDB;
	
	public UseItemController(IPlayerDataBase db) {
		this.playerDB = db;
	}
	
	public void useItem(IItem item) {
		playerDB.getCurrentPlayer().useItem(item);
	}
}
